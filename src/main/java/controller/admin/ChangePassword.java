/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.admin;

import DAO.Admin.AccountDAO;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.SQLException;
import utils.DBContext;

/**
 *
 * @author ADMIN
 */
@WebServlet(name = "AdminChangePassword", urlPatterns = {"/AdminChangePassword"})
public class ChangePassword extends HttpServlet {

    DBContext dbconnect = new DBContext();

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
            out.println("<title>Servlet AdminChangePassword</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AdminChangePassword at " + request.getContextPath() + "</h1>");
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
        try (Connection conn = dbconnect.getConnection()) {
            if (conn != null) {
                System.out.println(" Kết nối thành công!");
            } else {
                System.out.println(" Kết nối thất bại!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println(" Lỗi: " + e.getMessage());
        }
        request.getRequestDispatcher("/WEB-INF/admin/AdminChangePassword.jsp").forward(request, response);
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
        String username = request.getParameter("username");
        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        String oldPasswordHashed = AccountDAO.hashSHA256(oldPassword);
        String newPasswordHashed = AccountDAO.hashSHA256(newPassword);

        System.out.println("oldPasswordHashed: " + oldPasswordHashed);
        System.out.println("newPasswordHashed: " + newPasswordHashed);

        try (Connection conn = dbconnect.getConnection()) {
            if (conn != null) {
                System.out.println(" Kết nối thành công!");
            } else {
                System.out.println(" Kết nối thất bại!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println(" Lỗi: " + e.getMessage());
        }

        AccountDAO dao = new AccountDAO();
        if (dao.checkOldPassword(username, oldPasswordHashed)) {
            boolean updated = dao.updatePassword(username, newPasswordHashed);
            if (updated) {
                request.setAttribute("message", "Đổi mật khẩu thành công.");
                request.setAttribute("messageType", "success");
            } else {
                request.setAttribute("message", "Đổi mật khẩu thất bại.");
                request.setAttribute("messageType", "error");
            }
        } else {
            request.setAttribute("message", "Mật khẩu cũ không đúng.");
            request.setAttribute("messageType", "error");
        }

        request.getRequestDispatcher("/WEB-INF/admin/AdminChangePassword.jsp").forward(request, response);

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
