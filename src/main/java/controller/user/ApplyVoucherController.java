/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.user;

import DAO.Admin.voucherDAO;
import com.google.gson.Gson; // <-- IMPORT MỚI: Dùng để tạo JSON
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate; // <-- IMPORT MỚI: Dùng để xử lý ngày tháng
import java.util.HashMap;
import java.util.Map;
import model.Voucher;

/**
 *
 * @author Admin
 */
@WebServlet(name = "ApplyVoucherController", urlPatterns = {"/applyVoucher"})
public class ApplyVoucherController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Lấy dữ liệu từ AJAX request (nên dùng tên tham số rõ ràng)
        String voucherCode = request.getParameter("voucherCode");

        // Sử dụng Map và Gson để tạo JSON an toàn, dễ bảo trì
        Map<String, Object> jsonResponse = new HashMap<>();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (voucherCode == null || voucherCode.trim().isEmpty()) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Vui lòng nhập mã giảm giá.");
            response.getWriter().write(new Gson().toJson(jsonResponse));
            return;
        }

        voucherDAO dao = new voucherDAO();
        Voucher voucher = dao.getVoucherByCode(voucherCode.trim());

        if (voucher == null) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Mã giảm giá không tồn tại.");
        } else if (voucher.getIs_active() == 0) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Mã giảm giá không còn hiệu lực.");
        } else if (voucher.getUsage_limit() <= 0) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Mã giảm giá đã hết lượt sử dụng.");

            // =================================================================
            // ====> THÊM LOGIC KIỂM TRA NGÀY HẾT HẠN TẠI ĐÂY <====
            // =================================================================
        } else if (isVoucherDateInvalid(voucher)) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Mã giảm giá đã hết hạn hoặc chưa đến ngày áp dụng.");
            // =================================================================

        } else {
            // Nếu mọi thứ hợp lệ, trả về thông tin thành công
            jsonResponse.put("success", true);
            jsonResponse.put("message", "Áp dụng mã giảm giá thành công!");
            jsonResponse.put("voucherId", voucher.getVoucher_id());
            jsonResponse.put("code", voucher.getCode());
            jsonResponse.put("discountPercent", voucher.getDiscount_percent());
        }

        // Gửi chuỗi JSON đã được tạo về cho client
        response.getWriter().write(new Gson().toJson(jsonResponse));
    }

    /**
     * Hàm kiểm tra xem ngày hiện tại có nằm ngoài khoảng thời gian hợp lệ của
     * voucher không.
     *
     * @param voucher Đối tượng Voucher cần kiểm tra.
     * @return true nếu voucher không hợp lệ về mặt thời gian, ngược lại false.
     */
    private boolean isVoucherDateInvalid(Voucher voucher) {
        // Giả sử cột valid_from và valid_to trong DB có định dạng "YYYY-MM-DD"
        try {
            LocalDate today = LocalDate.now();
            LocalDate validFrom = LocalDate.parse(voucher.getValid_from());
            LocalDate validTo = LocalDate.parse(voucher.getValid_to());

            // Trả về true nếu (hôm nay < ngày bắt đầu) HOẶC (hôm nay > ngày kết thúc)
            return today.isBefore(validFrom) || today.isAfter(validTo);
        } catch (Exception e) {
            // Nếu định dạng ngày trong DB sai, coi như voucher không hợp lệ để đảm bảo an toàn
            e.printStackTrace();
            return true;
        }
    }

    // Các phương thức khác giữ nguyên
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html><html><head><title>Servlet ApplyVoucherController</title></head><body>");
            out.println("<h1>Servlet ApplyVoucherController at " + request.getContextPath() + "</h1>");
            out.println("</body></html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
