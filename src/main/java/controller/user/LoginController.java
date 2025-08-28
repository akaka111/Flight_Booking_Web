package controller.user;

import DAO.Admin.AccountDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Cookie;
import model.Account;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet(name = "LoginController", urlPatterns = {"/login"})
public class LoginController extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(LoginController.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String error = request.getParameter("error");
        if (error != null) {
            request.setAttribute("error", error.equals("permission") ? "Bạn không có quyền truy cập." : "Đăng nhập thất bại.");
        }

        if (action == null) {
            action = "default";
        }

        switch (action) {
            case "google":
                
                break;
            case "googlereceive":
                
                break;
            default:
                request.getRequestDispatcher("/WEB-INF/common/Login.jsp").forward(request, response);
        }
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

        AccountDAO userDao = new AccountDAO();
        Account user = userDao.authenticateUser(username, password);

        if (user == null) {
            logger.warn("Login failed: Incorrect username or password for {}", username);
            request.setAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng.");
            request.getRequestDispatcher("/WEB-INF/common/Login.jsp").forward(request, response);
        } else {
            // Luôn lưu thông tin user vào session
            session.setAttribute("user", user);
            session.setAttribute("userId", user.getUserId());
            session.setAttribute("role", user.getRole() != null ? user.getRole().toLowerCase() : "customer");
            session.setAttribute("fullname", user.getFullname() != null ? user.getFullname() : "Unknown User");

            logger.info("Session lưu thành công: userId={}, role={}, fullname={}", 
            session.getAttribute("userId"), 
            session.getAttribute("role"), 
            session.getAttribute("fullname"));
            // Xóa redirectURL sau khi login thành công
            session.removeAttribute("redirectURL");

            String role = (String) session.getAttribute("role");

            // Chuyển hướng theo role
            switch (role) {
                case "admin":
                    logger.info("User {} (ADMIN) logged in. Redirecting to /admin", username);
                    response.sendRedirect(request.getContextPath() + "/admin");
                    break;
                case "staff":
                    logger.info("User {} (STAFF) logged in. Redirecting to /staff", username);
                    response.sendRedirect(request.getContextPath() + "/staff");
                    break;
                case "customer":
                default:
                    logger.info("User {} (CUSTOMER) logged in. Redirecting to {}", username, redirectURL != null ? redirectURL : "/home");
                    response.sendRedirect(redirectURL != null && !redirectURL.isEmpty() ? redirectURL : request.getContextPath() + "/home");
                    break;
            }
        }
    }

}
