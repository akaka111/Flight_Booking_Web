/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package VNPay;

import DAO.Admin.BookingDAO;
import DAO.Admin.BookingVoucherDAO; // <-- IMPORT MỚI
import DAO.Admin.PassengerDAO;
import DAO.Admin.PaymentDAO;
import DAO.Admin.voucherDAO; // <-- IMPORT MỚI
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

        System.out.println("================== BẮT ĐẦU XỬ LÝ VNPAY RETURN ==================");

        System.out.println(">>> CHECKPOINT 1: VNPayReturnController.doGet() đã được gọi.");
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

                System.out.println(" - vnp_ResponseCode: " + vnp_ResponseCode);
                System.out.println(" - vnp_TransactionStatus: " + vnp_TransactionStatus);

                if ("00".equals(vnp_ResponseCode) && "00".equals(vnp_TransactionStatus)) {
                    System.out.println(">>> KẾT QUẢ: Giao dịch THÀNH CÔNG từ VNPay.");

                    PaymentDAO paymentDAO = new PaymentDAO();
                    if (paymentDAO.existsByTransactionId(transactionId)) {
                        System.out.println(">>> CẢNH BÁO: Giao dịch này (VNPay TransactionNo: " + transactionId + ") đã được xử lý trước đó. Bỏ qua.");
                        request.setAttribute("status", "true");
                    } else {
                        System.out.println(">>> CHECKPOINT 4: Chuẩn bị lưu dữ liệu vào database.");

                        Booking booking = (Booking) session.getAttribute("tempBooking");
                        Passenger passenger = (Passenger) session.getAttribute("tempPassenger");

                        if (booking != null && passenger != null) {
                            BookingDAO bookingDAO = new BookingDAO();
                            int newBookingId = bookingDAO.insertAndGetId(booking);
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
                            System.out.println(">>> KẾT QUẢ: Đã lưu Booking, Passenger, Payment thành công.");

                            // ================== BẮT ĐẦU LOGIC XỬ LÝ VOUCHER ==================
                            String appliedVoucherCode = booking.getVoucherCode();
                            if (appliedVoucherCode != null && !appliedVoucherCode.isEmpty()) {
                                System.out.println(">>> CHECKPOINT 5: Xử lý voucher code: " + appliedVoucherCode);

                                voucherDAO vDAO = new voucherDAO();
                                Voucher voucher = vDAO.getVoucherByCode(appliedVoucherCode);

                                if (voucher != null) {
                                    BookingVoucherDAO bvDAO = new BookingVoucherDAO();

                                    BookingVoucher bookingVoucher = new BookingVoucher();
                                    bookingVoucher.setBookingId(newBookingId);
                                    bookingVoucher.setVoucherId(voucher.getVoucher_id());

                                    bvDAO.insertBookingVoucher(bookingVoucher);
                                    
                                    vDAO.decreaseUsageLimit(voucher.getVoucher_id()); 

                                    System.out.println(">>> KẾT QUẢ: Đã lưu liên kết và giảm số lượt dùng của voucher thành công.");
                                } else {
                                    System.out.println(">>> CẢNH BÁO: Không tìm thấy voucher '" + appliedVoucherCode + "' trong DB để cập nhật. Có thể voucher đã bị xóa.");
                                }
                            }
                            // ================== KẾT THÚC LOGIC XỬ LÝ VOUCHER ==================

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
            System.out.println(">>> CHECKPOINT 6: Dọn dẹp session và chuyển hướng tới trang kết quả.");
            session.removeAttribute("tempBooking");
            session.removeAttribute("tempPassenger");
            session.removeAttribute("appliedVoucher"); // Cũng nên xóa voucher trong session nếu có

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
