/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.user;

import VNPay.Config;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Booking;

/**
 *
 * @author Admin
 */
@WebServlet(name = "PaymentProcessingController", urlPatterns = {"/process-payment"})
public class PaymentProcessingController extends HttpServlet {

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

        System.out.println("================== BẮT ĐẦU XỬ LÝ GỬI YÊU CẦU THANH TOÁN ==================");

        HttpSession session = request.getSession();
        Booking tempBooking = (Booking) session.getAttribute("tempBooking");

        if (tempBooking == null) {
            System.out.println(">>> LỖI: Không tìm thấy tempBooking trong session.");
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        // ================== BẮT ĐẦU SỬA LỖI TẠI ĐÂY ==================
        // 1. ĐỌC DỮ LIỆU TỪ FORM THAY VÌ CHỈ LẤY TỪ SESSION
        String finalPriceStr = request.getParameter("finalPrice");
        String voucherCode = request.getParameter("voucherCode");

        if (finalPriceStr == null || finalPriceStr.isEmpty()) {
            System.out.println(">>> LỖI: Không nhận được finalPrice từ form.");
            // Sử dụng giá gốc nếu không có giá cuối cùng
            finalPriceStr = String.valueOf(tempBooking.getTotalPrice());
        }

        double finalPrice = Double.parseDouble(finalPriceStr);

        // 2. CẬP NHẬT LẠI ĐỐI TƯỢNG BOOKING TRONG SESSION VỚI THÔNG TIN MỚI
        tempBooking.setTotalPrice(finalPrice); // Cập nhật lại giá tiền trong booking object
        tempBooking.setVoucherCode(voucherCode); // Lưu mã voucher vào booking object
        session.setAttribute("tempBooking", tempBooking); // Quan trọng: Lưu lại session

        System.out.println(">>> CHECKPOINT: Giá cuối cùng gửi đi: " + finalPrice + ", Voucher: " + voucherCode);

        // ================== KẾT THÚC SỬA LỖI ==================
        // Bắt đầu xử lý thanh toán VNPay
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";

        // Cập nhật OrderInfo để dễ theo dõi
        String vnp_OrderInfo = "Thanh toan ve may bay. Ma ve: " + tempBooking.getBookingCode();
        if (voucherCode != null && !voucherCode.isEmpty()) {
            vnp_OrderInfo += ". Voucher: " + voucherCode;
        }

        String orderType = "other";
        // 3. SỬ DỤNG GIÁ ĐÚNG (finalPrice) ĐỂ TẠO YÊU CẦU
        long amount = (long) finalPrice * 100;
        String vnp_TxnRef = Config.getRandomNumber(8);

        String vnp_IpAddr = Config.getIpAddress(request);
        String vnp_TmnCode = Config.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", Config.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        for (String fieldName : fieldNames) {
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (!fieldName.equals(fieldNames.get(fieldNames.size() - 1))) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }

        String queryUrl = query.toString();
        String vnp_SecureHash = Config.hmacSHA512(Config.vnp_HashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = Config.vnp_PayUrl + "?" + queryUrl;

        System.out.println(">>> CHECKPOINT: Chuyển hướng tới URL thanh toán của VNPay.");
        response.sendRedirect(paymentUrl);
    }
}
