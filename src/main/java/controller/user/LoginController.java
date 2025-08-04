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
                loginByGoogle(request, response);
                break;
            case "googlereceive":
                handleGoogleCallback(request, response);
                break;
            default:
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
            logger.warn("Google login failed: No code received");
            request.setAttribute("error", "Đăng nhập bằng Google thất bại.");
            request.getRequestDispatcher("/WEB-INF/common/Login.jsp").forward(request, response);
            return;
        }

        try {
            AccountDAO accountDao = new AccountDAO();
            com.google.api.services.oauth2.model.Userinfo userInfo = GoogleUtils.getUserInfo(code);
            String email = userInfo.getEmail();

            Account user = accountDao.getUserByEmail(email);

            if (user == null) {
                if (userInfo.getVerifiedEmail()) {
                    // TODO: Implement logic to create new user if needed
                    user = accountDao.getUserByEmail(email);
                } else {
                    logger.warn("Google login failed: Email not verified for {}", email);
                    request.setAttribute("error", "Email chưa được xác thực.");
                    request.getRequestDispatcher("/WEB-INF/common/Login.jsp").forward(request, response);
                    return;
                }
            }

            if (user != null) {
                HttpSession session = request.getSession(true);
                logger.info("Session created with ID: {}", session.getId());
                session.setAttribute("user", user);
                String redirectURL = (String) session.getAttribute("redirectURL");
                session.removeAttribute("redirectURL");

                int userId = user.getUserId();
                String role = user.getRole() != null ? user.getRole().toLowerCase() : "customer";
                String fullname = user.getFullname() != null ? user.getFullname().trim().replaceAll("\\s+", "_") : "Unknown_Staff";
                logger.info("User details - userId: {}, role: {}, fullname: {}", userId, role, fullname);

                if ("staff".equalsIgnoreCase(role) && userId > 0) {
                    session.setAttribute("staffId", userId);
                    session.setAttribute("staffRole", user.getRole());
                    session.setAttribute("staffName", fullname);
                    logger.info("Saved session for staff - staffId: {}, staffRole: {}, staffName: {}", userId, user.getRole(), fullname);
                } else {
                    logger.info("No staff session saved - role: {}, userId: {}, fullname: {}", role, userId, fullname);
                }

                switch (role) {
                    case "admin":
                        logger.info("User {} (ADMIN) logged in. Redirecting to /admin", user.getUsername());
                        response.sendRedirect(request.getContextPath() + "/admin");
                        break;
                    case "staff":
                        logger.info("User {} (STAFF) logged in. Redirecting to /staff", user.getUsername());
                        response.sendRedirect(request.getContextPath() + "/staff");
                        break;
                    case "customer":
                    default:
                        logger.info("User {} (CUSTOMER) logged in. Redirecting to {}", user.getUsername(), redirectURL != null ? redirectURL : "/home");
                        response.sendRedirect(redirectURL != null && !redirectURL.isEmpty() ? redirectURL : request.getContextPath() + "/home");
                        break;
                }
            } else {
                logger.warn("Google login failed: User not found for email {}", email);
                request.setAttribute("error", "Đăng nhập bằng Google thất bại.");
                request.getRequestDispatcher("/WEB-INF/common/Login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            logger.error("Error in handleGoogleCallback: ", e);
            request.setAttribute("error", "Lỗi hệ thống. Vui lòng thử lại sau.");
            request.getRequestDispatcher("/WEB-INF/common/Login.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            handleNormalLogin(request, response);
        } catch (Exception e) {
            logger.error("Error in handleNormalLogin: ", e);
            request.setAttribute("error", "Lỗi hệ thống. Vui lòng thử lại sau.");
            request.getRequestDispatcher("/WEB-INF/common/Login.jsp").forward(request, response);
        }
    }

    private void handleNormalLogin(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        HttpSession session = request.getSession(true);
        logger.info("Session created with ID: {}", session.getId());
        String redirectURL = (String) session.getAttribute("redirectURL");

        AccountDAO userDao = new AccountDAO();
        Account user = userDao.authenticateUser(username, password);

        if (user == null) {
            logger.warn("Login failed: Incorrect username or password for {}", username);
            request.setAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng.");
            request.getRequestDispatcher("/WEB-INF/common/Login.jsp").forward(request, response);
        } else {
            session.setAttribute("user", user);
            session.removeAttribute("redirectURL");

            int userId = user.getUserId();
            String role = user.getRole() != null ? user.getRole().toLowerCase() : "customer";
            String fullname = user.getFullname() != null ? user.getFullname().trim().replaceAll("\\s+", "_") : "Unknown_Staff";
            logger.info("User details - userId: {}, role: {}, fullname: {}", userId, role, fullname);

            if ("staff".equalsIgnoreCase(role)) {
                session.setAttribute("staffId", userId);
                session.setAttribute("staffRole", user.getRole());
                session.setAttribute("staffName", fullname);
                logger.info("Saved session for staff - staffId: {}, staffRole: {}, staffName: {}", userId, user.getRole(), fullname);
            } else {
                logger.info("No staff session saved - role: {}, userId: {}, fullname: {}", role, userId, fullname);
            }

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