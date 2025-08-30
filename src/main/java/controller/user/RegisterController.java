/*
 * Click nbfs://SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.user;

import DAO.Admin.AccountDAO;
import java.io.IOException;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.Random;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.sql.Date;
import model.Account;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeUtility;


/**
 *
 * @author Admin
 */
@WebServlet(name = "RegisterController", urlPatterns = {"/register", "/verify-otp"})
public class RegisterController extends HttpServlet {

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
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet RegisterController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet RegisterController at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

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
        String path = request.getServletPath();
        if ("/verify-otp".equals(path)) {
            request.getRequestDispatcher("/WEB-INF/user/otp_verification.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("/WEB-INF/user/register.jsp").forward(request, response);
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
        String path = request.getServletPath();
        if ("/register".equals(path)) {
            handleUserRegistration(request, response);
        } else if ("/verify-otp".equals(path)) {
            handleOtpVerification(request, response);
        }
    }

    private void handleUserRegistration(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String numberPhone = request.getParameter("numberPhone");
        String dobRaw = request.getParameter("dob");

        // Kiểm tra input rỗng
        if (username == null || password == null || confirmPassword == null || fullName == null || email == null
                || numberPhone == null || dobRaw == null
                || username.trim().isEmpty() || password.trim().isEmpty() || confirmPassword.trim().isEmpty()
                || fullName.trim().isEmpty() || email.trim().isEmpty() || numberPhone.trim().isEmpty() || dobRaw.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng điền đầy đủ tất cả các trường.");
            request.getRequestDispatcher("/WEB-INF/user/register.jsp").forward(request, response);
            return;
        }

        // Kiểm tra username phải có ít nhất 3 ký tự
        if (username.length() < 3) {
            request.setAttribute("error", "Tài khoản phải có ít nhất 3 ký tự.");
            request.getRequestDispatcher("/WEB-INF/user/register.jsp").forward(request, response);
            return;
        }

        // Kiểm tra mật khẩu nhập lại có giống nhau không
        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Mật khẩu và xác nhận mật khẩu không khớp.");
            request.getRequestDispatcher("/WEB-INF/user/register.jsp").forward(request, response);
            return;
        }

        // Kiểm tra email có đúng định dạng @gmail.com không
        if (!email.matches("^[a-zA-Z0-9._%+-]+@gmail\\.com$")) {
            request.setAttribute("error", "Email phải là địa chỉ Gmail hợp lệ (ví dụ: example@gmail.com).");
            request.getRequestDispatcher("/WEB-INF/user/register.jsp").forward(request, response);
            return;
        }

        // Kiểm tra xem số điện thoại có phải số không
        if (!numberPhone.matches("\\d+")) {
            request.setAttribute("error", "Số điện thoại phải được nhập bằng số.");
            request.getRequestDispatcher("/WEB-INF/user/register.jsp").forward(request, response);
            return;
        }

        AccountDAO userDao = new AccountDAO();

        // Kiểm tra xem username hoặc email đã tồn tại chưa
        if (userDao.isUserExist(username, email)) {
            request.setAttribute("error", "Tài khoản hoặc email đã tồn tại.");
            request.getRequestDispatcher("/WEB-INF/user/register.jsp").forward(request, response);
            return;
        }

        Date dob = null;
        try {
            dob = Date.valueOf(dobRaw); // Chuyển chuỗi từ input sang java.sql.Date
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "Định dạng ngày sinh không hợp lệ.");
            request.getRequestDispatcher("/WEB-INF/user/register.jsp").forward(request, response);
            return;
        }

        // Tạo OTP
        String otp = generateOtp();
        HttpSession session = request.getSession();
        session.setAttribute("otp", otp);
        session.setAttribute("tempUser", new Account(0, username, password, email, numberPhone, dob, "CUSTOMER", true, fullName));

        // Gửi OTP qua email
        boolean emailSent = sendOtpEmail(email, otp);
        if (!emailSent) {
            request.setAttribute("error", "Không thể gửi OTP. Vui lòng thử lại.");
            request.getRequestDispatcher("/WEB-INF/user/register.jsp").forward(request, response);
            return;
        }

        // Chuyển hướng đến trang xác thực OTP
        request.setAttribute("email", email);
        request.getRequestDispatcher("/WEB-INF/user/otp_verification.jsp").forward(request, response);
    }

    private void handleOtpVerification(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String submittedOtp = request.getParameter("otp");
        String storedOtp = (String) session.getAttribute("otp");
        Account tempUser = (Account) session.getAttribute("tempUser");

        if (submittedOtp == null || submittedOtp.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập mã OTP.");
            request.setAttribute("email", tempUser.getEmail());
            request.getRequestDispatcher("/WEB-INF/user/otp_verification.jsp").forward(request, response);
            return;
        }

        if (!submittedOtp.equals(storedOtp)) {
            request.setAttribute("error", "Mã OTP không hợp lệ. Vui lòng thử lại.");
            request.setAttribute("email", tempUser.getEmail());
            request.getRequestDispatcher("/WEB-INF/user/otp_verification.jsp").forward(request, response);
            return;
        }

        // OTP hợp lệ, tiến hành đăng ký
        AccountDAO userDao = new AccountDAO();
        boolean isRegistered = userDao.registerUser(tempUser);

        if (isRegistered) {
            // Xóa các thuộc tính session
            session.removeAttribute("otp");
            session.removeAttribute("tempUser");
            request.setAttribute("message", "Đăng ký thành công! Vui lòng đăng nhập.");
            request.getRequestDispatcher("/WEB-INF/common/Login.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Đăng ký thất bại. Vui lòng thử lại.");
            request.setAttribute("email", tempUser.getEmail());
            request.getRequestDispatcher("/WEB-INF/user/otp_verification.jsp").forward(request, response);
        }
    }

    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // Tạo OTP 6 chữ số
        return String.valueOf(otp);
    }

    private boolean sendOtpEmail(String toEmail, String otp) {
        Properties config = new Properties();
        try (FileInputStream fis = new FileInputStream(getServletContext().getRealPath("/WEB-INF/config.properties"))) {
            config.load(fis);
        } catch (IOException e) {
            System.err.println("Không thể tải tệp cấu hình: " + e.getMessage());
            e.printStackTrace();
            return false;
        }

        String host = config.getProperty("smtp.host");
        String port = config.getProperty("smtp.port");
        final String username = config.getProperty("smtp.username");
        final String password = config.getProperty("smtp.password");

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(MimeUtility.encodeText("Xác thực OTP đăng ký tài khoản - Flight Booking Web", "UTF-8", null));
            String emailContent = "Kính gửi Quý khách,\n\n" +
                                 "Cảm ơn Quý khách đã đăng ký tài khoản trên hệ thống Flight Booking Web. Để hoàn tất quá trình đăng ký, vui lòng sử dụng mã OTP dưới đây:\n\n" +
                                 "Mã OTP của Quý khách là: " + otp + "\n\n" +
                                 "Vui lòng nhập mã này vào trang xác thực trong vòng 5 phút để hoàn tất đăng ký tài khoản. Mã OTP chỉ có hiệu lực trong thời gian này để đảm bảo an toàn.\n\n" +
                                 "Nếu Quý khách không thực hiện yêu cầu này, xin vui lòng bỏ qua email.\n\n" +
                                 "Trân trọng,\n" +
                                 "Đội ngũ Flight Booking Web";
            message.setText(emailContent, "UTF-8");
            Transport.send(message);
            System.out.println("Gửi email OTP thành công đến: " + toEmail);
            return true;
        } catch (MessagingException e) {
            System.err.println("Không thể gửi email OTP đến: " + toEmail);
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.err.println("Lỗi khi mã hóa tiêu đề email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}