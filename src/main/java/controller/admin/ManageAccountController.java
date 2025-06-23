/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.admin;

import DAO.AccountDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.util.List;
import model.Account;

/**
 *
 * @author Admin
 */
@WebServlet(name = "ManageAccountController", urlPatterns = {"/manageAccountController"})
public class ManageAccountController extends HttpServlet {

    private final AccountDAO accountDAO = new AccountDAO();

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
            out.println("<title>Servlet ManageAccountController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ManageAccountController at " + request.getContextPath() + "</h1>");
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
        String action = request.getParameter("action");
        if (action == null) {
            action = "list"; // Mặc định là hiển thị danh sách
        }

        switch (action) {
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                deleteUser(request, response);
                break;
            case "add":
                request.getRequestDispatcher("/WEB-INF/admin/addUser.jsp").forward(request, response);
                break;
            default:
                listUsers(request, response);
                break;
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
        String action = request.getParameter("action");
        if (action == null) {
            response.sendRedirect("manageAccountController");
            return;
        }

        // Đảm bảo request encoding là UTF-8 để nhận tiếng Việt
        request.setCharacterEncoding("UTF-8");

        switch (action) {
            case "create":
                createUser(request, response);
                break;
            case "update":
                updateUser(request, response);
                break;
            default:
                response.sendRedirect("manageAccountController");
                break;
        }
    }

    private void listUsers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Account> list = accountDAO.getAllAccounts();
        request.setAttribute("accountList", list);
        request.getRequestDispatcher("/WEB-INF/admin/manageUsers.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int userId = Integer.parseInt(request.getParameter("userId"));
        Account user = accountDAO.getAccountById(userId);
        request.setAttribute("user", user);
        request.getRequestDispatcher("/WEB-INF/admin/editUser.jsp").forward(request, response);
    }

    private void createUser(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Account user = new Account();
        user.setUsername(request.getParameter("username"));
        user.setPassword(request.getParameter("password")); // DAO sẽ hash mật khẩu này
        user.setFullname(request.getParameter("fullname"));
        user.setEmail(request.getParameter("email"));
        user.setPhone(request.getParameter("phone"));
        user.setRole(request.getParameter("role"));
        user.setStatus(Boolean.parseBoolean(request.getParameter("status")));
        try {
            user.setDob(Date.valueOf(request.getParameter("dob")));
        } catch (IllegalArgumentException e) {
            user.setDob(null); // Xử lý nếu ngày sinh không hợp lệ
        }

        accountDAO.addAccountByAdmin(user);
        response.sendRedirect("manageAccountController");
    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Account user = new Account();
        user.setUserId(Integer.parseInt(request.getParameter("userId")));
        user.setFullname(request.getParameter("fullname"));
        user.setEmail(request.getParameter("email"));
        user.setPhone(request.getParameter("phone"));
        user.setRole(request.getParameter("role"));
        user.setStatus(Boolean.parseBoolean(request.getParameter("status")));
        try {
            user.setDob(Date.valueOf(request.getParameter("dob")));
        } catch (IllegalArgumentException e) {
            user.setDob(null);
        }

        accountDAO.updateAccountByAdmin(user);
        response.sendRedirect("manageAccountController");
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int userId = Integer.parseInt(request.getParameter("userId"));
        accountDAO.deleteAccount(userId);
        response.sendRedirect("manageAccountController");
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
