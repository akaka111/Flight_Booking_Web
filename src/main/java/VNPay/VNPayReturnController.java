/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package VNPay;

import DAO.Admin.BookingDAO;
import DAO.Admin.BookingVoucherDAO; // <-- IMPORT MỚI
import DAO.Admin.PassengerDAO;
import DAO.Admin.PaymentDAO;
import DAO.Admin.SeatDAO;
import DAO.Admin.VoucherDAO;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import model.Booking;
import model.BookingVoucher; // <-- IMPORT MỚI
import model.Passenger;
import model.Payment;
import model.Voucher; // <-- IMPORT MỚI
import utils.MailService;

/**
 *
 * @author Admin
 */
@WebServlet(name = "VNPayReturnController", urlPatterns = {"/vnpay_return"})
public class VNPayReturnController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet VNPayReturnController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet VNPayReturnController at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ConfigLoader.load(getServletContext());
        request.getParameterMap().forEach((key, value) -> System.out.println(" - " + key + " = " + value[0]));

        HttpSession session = request.getSession();
        try {
            Map<String, String> fields = new HashMap<>();
            for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
                String fieldName = entry.getKey();
                String fieldValue = entry.getValue()[0];
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    if (!fieldName.equals("vnp_SecureHash") && !fieldName.equals("vnp_SecureHashType")) {
                        fields.put(fieldName, fieldValue);
                    }
                }
            }

            String vnp_SecureHash = request.getParameter("vnp_SecureHash");
            String hash = Config.hashAllFields(fields);

            if (hash.equals(vnp_SecureHash)) {
                System.out.println(">>> KẾT QUẢ: Chữ ký HỢP LỆ.");

                String vnp_ResponseCode = request.getParameter("vnp_ResponseCode");
                String vnp_TransactionStatus = request.getParameter("vnp_TransactionStatus");
                String transactionId = request.getParameter("vnp_TransactionNo");

                if ("00".equals(vnp_ResponseCode) && "00".equals(vnp_TransactionStatus)) {

                    PaymentDAO paymentDAO = new PaymentDAO();
                    if (paymentDAO.existsByTransactionId(transactionId)) {
                        request.setAttribute("status", "true");
                    } else {

                        Booking booking = (Booking) session.getAttribute("tempBooking");
                        Passenger passenger = (Passenger) session.getAttribute("tempPassenger");

                        if (booking != null && passenger != null) {
                            BookingDAO bookingDAO = new BookingDAO();

                            int newBookingId = bookingDAO.insertAndGetId(booking);
                            String bookingCode = bookingDAO.getBookingCodeById(newBookingId);
                            booking.setBookingCode(bookingCode);

                            SeatDAO seatDAO = new SeatDAO();
                            seatDAO.updateSeatBooking(booking.getSeat().getSeatId(), true);
                            System.out.println(">>> Đã cập nhật ghế " + booking.getSeatId() + " là đã được đặt (is_booked = true)");

                            if (newBookingId == -1) {
                                throw new Exception("Không thể tạo booking trong DB.");
                            }

                            passenger.setBookingId(newBookingId);
                            PassengerDAO passengerDAO = new PassengerDAO();
                            passengerDAO.addPassenger(passenger);

                            BigDecimal amount = new BigDecimal(request.getParameter("vnp_Amount")).divide(new BigDecimal(100));
                            String paymentNote = request.getParameter("vnp_OrderInfo");

                            Payment payment = new Payment();
                            payment.setBookingId(newBookingId);
                            payment.setAmount(amount);
                            payment.setPaymentMethod("VNPay");
                            payment.setPaymentTime(LocalDateTime.now());
                            payment.setTransactionId(transactionId);
                            payment.setStatus("Success");
                            payment.setPaymentNote(paymentNote);
                            paymentDAO.insert(payment);

                            // GỬI EMAIL XÁC NHẬN
                            try {
                                String to = passenger.getEmail();
                                String subject = "Xác nhận đặt vé thành công - Mã đơn: " + newBookingId;
                                StringBuilder contentBuilder = new StringBuilder();
                                contentBuilder.append("<!DOCTYPE html>")
                                        .append("<html lang='vi'>")
                                        .append("<head>")
                                        .append("<meta charset='UTF-8'>")
                                        .append("<style>")
                                        .append("body { font-family: Arial, sans-serif; line-height: 1.6; }")
                                        .append(".container { padding: 20px; border: 1px solid #ddd; border-radius: 8px; }")
                                        .append("h2 { color: #2E86C1; }")
                                        .append("table { border-collapse: collapse; width: 100%; margin-top: 10px; }")
                                        .append("td, th { border: 1px solid #ddd; padding: 8px; }")
                                        .append("th { background-color: #f2f2f2; text-align: left; }")
                                        .append("</style>")
                                        .append("</head>")
                                        .append("<body>")
                                        .append("<div class='container'>")
                                        .append("<h2>Xác nhận đặt vé thành công</h2>")
                                        .append("<p>Xin chào <b>").append(passenger.getFullName()).append("</b>,</p>")
                                        .append("<p>Cảm ơn bạn đã đặt vé với chúng tôi. Dưới đây là chi tiết đơn hàng của bạn:</p>")
                                        // === Thông tin đặt vé ===
                                        .append("<h3>Thông tin đặt vé</h3>")
                                        .append("<table>")
                                        .append("<tr><th>Mã đặt chỗ</th><td>").append(booking.getBookingCode()).append("</td></tr>")
                                        .append("<tr><th>Tên hành khách</th><td>").append(passenger.getFullName()).append("</td></tr>")
                                        .append("<tr><th>Email</th><td>").append(passenger.getEmail()).append("</td></tr>")
                                        .append("<tr><th>Số điện thoại</th><td>").append(passenger.getPhoneNumber()).append("</td></tr>")
                                        .append("<tr><th>Số ghế</th><td>").append(booking.getSeat().getSeatNumber()).append("</td></tr>")
                                        .append("<tr><th>Chuyến bay</th><td>").append(booking.getFlightId()).append("</td></tr>")
                                        .append("<tr><th>Ngày đặt</th><td>").append(LocalDateTime.now()).append("</td></tr>");

                                if (booking.getVoucherCode() != null && !booking.getVoucherCode().isEmpty()) {
                                    contentBuilder.append("<tr><th>Voucher áp dụng</th><td>")
                                            .append(booking.getVoucherCode())
                                            .append("</td></tr>");
                                }

                                contentBuilder.append("</table>")
                                        // === Thông tin thanh toán ===
                                        .append("<h3>Thông tin thanh toán</h3>")
                                        .append("<table>")
                                        .append("<tr><th>Số tiền</th><td>").append(amount).append(" VND</td></tr>")
                                        .append("<tr><th>Phương thức</th><td>VNPay</td></tr>")
                                        .append("<tr><th>Mã giao dịch</th><td>").append(transactionId).append("</td></tr>")
                                        .append("<tr><th>Tình trạng</th><td><b style='color:green'>Thành công</b></td></tr>")
                                        .append("</table>")
                                        .append("<p style='margin-top:20px'>Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi.<br>")
                                        .append("Trân trọng,<br>Hệ thống đặt vé.</p>")
                                        .append("</div>")
                                        .append("</body></html>");

                                System.out.println(">>> DEBUG: Email hành khách: " + to);
                                MailService.sendEmail(to, subject, contentBuilder.toString());
                                System.out.println(">>> ĐÃ GỬI EMAIL đến " + to);
                            } catch (Exception ex) {
                                System.out.println(">>> LỖI khi gửi email xác nhận: " + ex.getMessage());
                                ex.printStackTrace();
                            }

                            String appliedVoucherCode = booking.getVoucherCode();
                            if (appliedVoucherCode != null && !appliedVoucherCode.isEmpty()) {

                                VoucherDAO vDAO = new VoucherDAO();
                                Voucher voucher = vDAO.getVoucherByCode(appliedVoucherCode);

                                if (voucher != null) {
                                    BookingVoucherDAO bvDAO = new BookingVoucherDAO();

                                    BookingVoucher bookingVoucher = new BookingVoucher();
                                    bookingVoucher.setBookingId(newBookingId);
                                    bookingVoucher.setVoucherId(voucher.getVoucher_id());

                                    bvDAO.insertBookingVoucher(bookingVoucher);
                                    seatDAO.updateSeatBooking(booking.getSeatId(), true);
                                    vDAO.decreaseUsageLimit(voucher.getVoucher_id());

                                } else {
                                    System.out.println(">>> CẢNH BÁO: Không tìm thấy voucher '" + appliedVoucherCode + "' trong DB để cập nhật. Có thể voucher đã bị xóa.");
                                }
                            }

                            request.setAttribute("status", "true");

                        } else {
                            request.setAttribute("status", "false");
                            request.setAttribute("error", "Phiên làm việc đã hết hạn, không thể lưu đơn hàng.");
                        }
                    }
                } else {
                    request.setAttribute("status", "false");
                    request.setAttribute("error", "Giao dịch thất bại. Mã lỗi VNPay: " + vnp_ResponseCode);
                }
            } else {
                request.setAttribute("status", "false");
                request.setAttribute("error", "invalid_signature");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("status", "false");
            request.setAttribute("error", "Đã xảy ra lỗi hệ thống. Vui lòng xem log của server.");
        } finally {
            session.removeAttribute("tempBooking");
            session.removeAttribute("tempPassenger");
            session.removeAttribute("appliedVoucher");

            request.getRequestDispatcher("/WEB-INF/user/orderStatus.jsp").forward(request, response);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
