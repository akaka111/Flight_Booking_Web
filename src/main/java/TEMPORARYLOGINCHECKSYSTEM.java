/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Account;

/**
 *
 * @author ADMIN
 */
@WebServlet(name = "TEMPORARYLOGINCHECKSYSTEM", urlPatterns = {"/TEMPORARYLOGINCHECKSYSTEM"})
public class TEMPORARYLOGINCHECKSYSTEM extends HttpServlet {

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
            out.println("<title>Servlet TEMPORARYLOGINCHECKSYSTEM</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet TEMPORARYLOGINCHECKSYSTEM at " + request.getContextPath() + "</h1>");
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
        // Lấy role từ parameter (có thể là CUSTOMER, STAFF, ADMIN)
        String role = request.getParameter("role");
        if ("STAFF".equals(role)) {
            Account staffUser = new Account();
            staffUser.setUserId(999);
            staffUser.setUsername("staff");
            staffUser.setEmail("staff@example.com");
            staffUser.setRole("STAFF");
            // Lưu vào session
            HttpSession session = request.getSession();
            session.setAttribute("account", staffUser);
            System.out.println("Đăng nhập tạm với vai trò STAFF: " + staffUser.getEmail());
            // Chuyển hướng đến trang của staff
            request.getRequestDispatcher("/WEB-INF/staff/staff.jsp").forward(request, response);;
            return;
        }
        if ("CUSTOMER".equals(role))  {
            // Tạo user giả lập
            Account fakeUser = new Account();
            fakeUser.setUserId(999); // hoặc bất kỳ ID nào
            fakeUser.setUsername("testuser");
            fakeUser.setEmail("test2@example.com");
            fakeUser.setRole(role); // Gán role theo parameter

            // Lưu vào session
            HttpSession session = request.getSession();
            session.setAttribute("account", fakeUser);

            // Chuyển hướng đến trang cần test
            request.getRequestDispatcher("/WEB-INF/views/home.jsp").forward(request, response);
            System.out.println("Đã login tạm role: " + fakeUser.getRole());
            System.out.println("Email: " + fakeUser.getEmail());
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
        processRequest(request, response);
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
