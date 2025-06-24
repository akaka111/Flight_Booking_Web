/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.user;

import DAO.Admin.AccountDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Date;
import model.Account;

/**
 *
 * @author Admin
 */
@WebServlet(name = "RegisterController", urlPatterns = {"/register"})
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
            /* TODO output your page here. You may use following sample code. */
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
        request.getRequestDispatcher("/WEB-INF/user/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        handleUserRegistration(request, response);
    }

    private void handleUserRegistration(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String numberPhone = request.getParameter("numberPhone");
        

        // Kiểm tra input rỗng
        if (username == null || password == null || confirmPassword == null || fullName == null || email == null
                || username.trim().isEmpty() || password.trim().isEmpty() || confirmPassword.trim().isEmpty()
                || fullName.trim().isEmpty() || email.trim().isEmpty() || numberPhone.trim().isEmpty()) {

            request.setAttribute("error", "All fields must be filled.");
            request.getRequestDispatcher("/WEB-INF/user/register.jsp").forward(request, response);
            return;
        }

        // Kiểm tra username phải có ít nhất 3 ký tự
        if (username.length() < 3) {
            request.setAttribute("error", "Username must be at least 3 characters long.");
            request.getRequestDispatcher("/WEB-INF/user/register.jsp").forward(request, response);
            return;
        }

        // Kiểm tra mật khẩu nhập lại có giống nhau không
        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Password must be the same.");
            request.getRequestDispatcher("/WEB-INF/user/register.jsp").forward(request, response);
            return;
        }

        // Kiểm tra email có đúng định dạng @gmail.com không
        if (!email.matches("^[a-zA-Z0-9._%+-]+@gmail\\.com$")) {
            request.setAttribute("error", "Email must be a valid Gmail address (e.g., example@gmail.com).");
            request.getRequestDispatcher("/WEB-INF/user/register.jsp").forward(request, response);
            return;
        }

        // Kiểm tra xem số điện thoại có phải số không
        if (!numberPhone.matches("\\d+")) {
            request.setAttribute("error", "Phone numbers must be entered numerically.");
            request.getRequestDispatcher("/WEB-INF/user/register.jsp").forward(request, response);
            return;
        }

        AccountDAO userDao = new AccountDAO();

        // Kiểm tra xem username hoặc email đã tồn tại chưa
        if (userDao.isUserExist(username, email)) {
            request.setAttribute("error", "Username or Email already exists.");
            request.getRequestDispatcher("/WEB-INF/user/register.jsp").forward(request, response);
            return;
        }

        String dobRaw = request.getParameter("dob");
        Date dob = null;
        try {
            dob = Date.valueOf(dobRaw); // Chuyển chuỗi từ input sang java.sql.Date
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "Invalid date of birth format.");
            request.getRequestDispatcher("/WEB-INF/user/register.jsp").forward(request, response);
            return;
        }

        // Tạo user mới
        Account newUser = new Account();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setEmail(email);
        newUser.setPhone(numberPhone);
        newUser.setDob(dob);
        newUser.setRole("CUSTOMER");
        newUser.setStatus(true);
        newUser.setFullname(fullName);


        boolean isRegistered = userDao.registerUser(newUser);

        if (isRegistered) {
            // ✅ Chuyển hướng hợp lý sau khi đăng ký
            request.setAttribute("message", "Registration successful! Please log in.");
            request.getRequestDispatcher("/WEB-INF/common/Login.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Registration failed. Try again.");
            request.getRequestDispatcher("/WEB-INF/user/register.jsp").forward(request, response);
        }

    }
}