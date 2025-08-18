package controller.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import utils.DBContext;
import utils.MailService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet(name = "ForgotPassword", urlPatterns = {"/ForgotPassword"})
public class ForgotPasswordServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/user/forgotPassword.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("sendOtp".equals(action)) {
            handleSendOtp(req, resp);
        } else if ("resetPassword".equals(action)) {
            handleResetPassword(req, resp);
        } else {
            req.setAttribute("error", "Hành động không hợp lệ.");
            req.getRequestDispatcher("/WEB-INF/user/forgotPassword.jsp").forward(req, resp);
        }
    }

    /** B1: Người dùng nhập email/username → gửi OTP */
    private void handleSendOtp(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String identifier = trim(req.getParameter("identifier"));

        if (identifier == null || identifier.isEmpty()) {
            req.setAttribute("error", "Vui lòng nhập email hoặc username.");
            req.getRequestDispatcher("/WEB-INF/user/forgotPassword.jsp").forward(req, resp);
            return;
        }

        try (Connection conn = new DBContext().getConnection()) {
            // Tìm user theo username/email
            String sql = "SELECT user_id, email FROM Account WHERE username = ? OR email = ?";
            Integer userId = null;
            String email = null;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, identifier);
                ps.setString(2, identifier);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        userId = rs.getInt("user_id");
                        email = rs.getString("email");
                    }
                }
            }

            if (userId == null || email == null || email.isEmpty()) {
                req.setAttribute("error", "Không tìm thấy tài khoản với thông tin đã nhập.");
                req.getRequestDispatcher("/WEB-INF/user/forgotPassword.jsp").forward(req, resp);
                return;
            }

            // Vô hiệu OTP cũ (nếu còn)
            try (PreparedStatement ps = conn.prepareStatement(
                    "UPDATE PasswordResetOTP SET is_used = 1 WHERE user_id = ? AND is_used = 0")) {
                ps.setInt(1, userId);
                ps.executeUpdate();
            }

            // Sinh OTP & lưu DB (hết hạn sau 5 phút)
            String otp = MailService.generateOTP(6);
            String insertSql = "INSERT INTO PasswordResetOTP(user_id, otp_code, expiry_time, is_used) "
                    + "VALUES (?, ?, DATEADD(MINUTE, 5, GETDATE()), 0)";
            try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                ps.setInt(1, userId);
                ps.setString(2, otp);
                ps.executeUpdate();
            }

            // Gửi email OTP
            MailService.sendOtpEmail(email, otp);

            req.setAttribute("message", "Mã OTP đã được gửi đến email của bạn.");
            req.setAttribute("step", "verifyOtp");   // để JSP hiển thị form nhập OTP + mật khẩu mới
            req.setAttribute("userId", userId);
            req.getRequestDispatcher("/WEB-INF/user/forgotPassword.jsp").forward(req, resp);

        } catch (Exception ex) {
            ex.printStackTrace();
            req.setAttribute("error", "Lỗi hệ thống: " + ex.getMessage());
            req.getRequestDispatcher("/WEB-INF/user/forgotPassword.jsp").forward(req, resp);
        }
    }

    /** B2: Người dùng nhập OTP + mật khẩu mới → xác thực & đổi mật khẩu */
    private void handleResetPassword(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String otpInput = req.getParameter("otp");
        String newPassword = req.getParameter("newPassword");
        String confirm = req.getParameter("confirmPassword");
        String userIdStr = req.getParameter("userId");

        if (otpInput == null || newPassword == null || confirm == null || userIdStr == null) {
            req.setAttribute("error", "Vui lòng nhập đầy đủ thông tin.");
            req.getRequestDispatcher("/WEB-INF/user/forgotPassword.jsp").forward(req, resp);
            return;
        }

        if (!newPassword.equals(confirm) || newPassword.length() < 6) {
            req.setAttribute("error", "Mật khẩu không hợp lệ hoặc xác nhận không khớp (>= 6 ký tự).");
            req.getRequestDispatcher("/WEB-INF/user/forgotPassword.jsp").forward(req, resp);
            return;
        }

        try (Connection conn = new DBContext().getConnection()) {
            int userId = Integer.parseInt(userIdStr);

            // Kiểm tra OTP còn hạn & chưa dùng (SQL Server)
            String sql = "SELECT id FROM PasswordResetOTP "
                       + "WHERE user_id = ? AND otp_code = ? AND is_used = 0 AND expiry_time > GETDATE()";
            Integer otpId = null;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, userId);
                ps.setString(2, otpInput);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        otpId = rs.getInt("id");
                    }
                }
            }

            if (otpId == null) {
                req.setAttribute("error", "OTP không hợp lệ hoặc đã hết hạn.");
                req.setAttribute("step", "verifyOtp");
                req.setAttribute("userId", userId);
                req.getRequestDispatcher("/WEB-INF/user/forgotPassword.jsp").forward(req, resp);
                return;
            }

            // Hash SHA-256
            String hashed = sha256Hex(newPassword);

            // Cập nhật mật khẩu
            try (PreparedStatement ps = conn.prepareStatement(
                    "UPDATE Account SET password = ? WHERE user_id = ?")) {
                ps.setString(1, hashed);
                ps.setInt(2, userId);
                ps.executeUpdate();
            }

            // Đánh dấu OTP đã dùng
            try (PreparedStatement ps = conn.prepareStatement(
                    "UPDATE PasswordResetOTP SET is_used = 1 WHERE id = ?")) {
                ps.setInt(1, otpId);
                ps.executeUpdate();
            }

            req.setAttribute("message", "Đổi mật khẩu thành công. Bạn có thể đăng nhập lại.");
            req.getRequestDispatcher("/WEB-INF/user/forgotPassword.jsp").forward(req, resp);

        } catch (Exception ex) {
            ex.printStackTrace();
            req.setAttribute("error", "Có lỗi hệ thống: " + ex.getMessage());
            req.getRequestDispatcher("/WEB-INF/user/forgotPassword.jsp").forward(req, resp);
        }
    }

    // ===== Helpers =====
    private static String trim(String s) {
        return s == null ? null : s.trim();
    }

    private static String sha256Hex(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] dig = md.digest(input.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder(dig.length * 2);
        for (byte b : dig) sb.append(String.format("%02x", b));
        return sb.toString();
    }
}
