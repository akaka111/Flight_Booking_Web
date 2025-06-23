/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.user;

import DAO.Admin.AccountDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Account;
import utils.GoogleUtils;

/**
 *
 * @author Admin
 */
// THAY ĐỔI 1: Đổi URL pattern cho ngắn gọn và chuẩn hơn
@WebServlet(name = "LoginController", urlPatterns = {"/login"})
public class LoginController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "default";
        }

        switch (action) {
            case "google":
                loginByGoogle(request, response);
                break;
            case "googlereceive":
                handleGoogleCallback(request, response);
                break;
            default:
                // Hiển thị trang login
                request.getRequestDispatcher("/WEB-INF/common/Login.jsp").forward(request, response);
        }
    }

    private void loginByGoogle(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String googleLoginUrl = GoogleUtils.getGoogleLoginUrl();
        response.sendRedirect(googleLoginUrl);
    }

    private void handleGoogleCallback(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String code = request.getParameter("code");
        if (code == null || code.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/login?error=Google login failed");
            return;
        }

        AccountDAO accountDao = new AccountDAO();
        com.google.api.services.oauth2.model.Userinfo userInfo = GoogleUtils.getUserInfo(code);
        String email = userInfo.getEmail();

        // Kiểm tra xem user đã tồn tại trong DB chưa
        Account user = accountDao.getUserByEmail(email);

        if (user == null) { // Nếu user chưa tồn tại
            // THAY ĐỔI 2: Tạo user mới nếu email đã được xác thực
            if (userInfo.getVerifiedEmail()) {

                // Sau khi tạo, phải lấy lại thông tin user từ DB để có đầy đủ dữ liệu (user_id, role,...)
                user = accountDao.getUserByEmail(email);
            } else {
                response.sendRedirect(request.getContextPath() + "/login?error=Email has not been verified");
                return;
            }
        }

        // Nếu user tồn tại (hoặc vừa được tạo thành công), lưu vào session
        if (user != null) {
            HttpSession session = request.getSession(true);

            session.setAttribute("user", user);

            String redirectURL = (String) session.getAttribute("redirectURL");
            session.removeAttribute("redirectURL");

            String role = user.getRole().toLowerCase();

            if (role != null) {
                switch (role) {
                    case "admin":
                        System.out.println("User is ADMIN. Redirecting to admin dashboard...");
                        response.sendRedirect(request.getContextPath() + "/admin");
                        break;
                    case "staff":
                        System.out.println("User is STAFF. Redirecting to staff dashboard...");
                        response.sendRedirect(request.getContextPath() + "/staff/dashboard");
                        break;
                    case "customer":
                    default:
                        System.out.println("User is CUSTOMER. Redirecting to home page...");
                        if (redirectURL != null && !redirectURL.isEmpty()) {
                            response.sendRedirect(redirectURL);
                        } else {
                            response.sendRedirect(request.getContextPath() + "/home");
                        }
                        break;
                }
            } else {
                // Trường hợp role là null (để phòng ngừa), cho về trang chủ
                response.sendRedirect(request.getContextPath() + "/home");
            }

        }
        System.out.println("User info: " + user);
        System.out.println("Role: " + user.getRole());

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleNormalLogin(request, response);
    }

    private void handleNormalLogin(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        HttpSession session = request.getSession(true);

        String redirectURL = (String) session.getAttribute("redirectURL");

        if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
            request.setAttribute("error", "Tên đăng nhập và mật khẩu không được để trống.");
            request.getRequestDispatcher("/WEB-INF/common/Login.jsp").forward(request, response);
            return;
        }

        AccountDAO userDao = new AccountDAO();
        Account user = userDao.authenticateUser(username, password);

        if (user == null) {
            request.setAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng.");
            request.getRequestDispatcher("/WEB-INF/common/Login.jsp").forward(request, response);
        } else {
            session.setAttribute("user", user);
            session.removeAttribute("redirectURL"); // Xóa URL cũ sau khi dùng

            // === PHẦN PHÂN QUYỀN VÀ CHUYỂN HƯỚNG ===
            String role = user.getRole().toLowerCase();

            if (role != null) {
                switch (role) {
                    case "admin":
                        // Chuyển hướng đến trang dashboard của admin
                        System.out.println("User is ADMIN. Redirecting to admin dashboard...");
                        response.sendRedirect(request.getContextPath() + "/admin");
                        break;
                    case "staff":
                        // Chuyển hướng đến trang dashboard của nhân viên
                        System.out.println("User is STAFF. Redirecting to staff dashboard...");
                        response.sendRedirect(request.getContextPath() + "/staff/dashboard");
                        break;
                    case "customer":
                    default:
                        // Chuyển hướng về trang chủ cho khách hàng
                        // hoặc về trang trước đó nếu có
                        System.out.println("User is CUSTOMER. Redirecting to home page...");
                        if (redirectURL != null && !redirectURL.isEmpty()) {
                            response.sendRedirect(redirectURL);
                        } else {
                            response.sendRedirect(request.getContextPath() + "/home");
                        }
                        break;
                }
            } else {
                // Trường hợp role là null (để phòng ngừa), cho về trang chủ
                response.sendRedirect(request.getContextPath() + "/home");
                // Ví dụ filter hoặc trong web.xml
                if (session.getAttribute("user") == null || !role.equals("admin")) {
                    response.sendRedirect("/login?error=permission");
                }

            }
            // ===========================================
        }
        System.out.println("User info: " + user);
        System.out.println("Role: " + user.getRole());

    }
}
