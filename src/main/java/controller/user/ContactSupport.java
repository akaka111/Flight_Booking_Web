/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.user;

import DAO.user.SupportContact;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.Account;
import model.Message;

/**
 *
 * @author ADMIN
 */
@WebServlet(name = "ContactSupport", urlPatterns = {"/ContactSupport"})
public class ContactSupport extends HttpServlet {

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
            out.println("<title>Servlet ContactSupport</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ContactSupport at " + request.getContextPath() + "</h1>");
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
        // xem trang FAQ (không cần đăng nhập)
        request.getRequestDispatcher("/WEB-INF/views/SupportPage.jsp").forward(request, response);
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
        HttpSession session = request.getSession(false);

        if ("openForm".equals(action)) {
            if (session != null && session.getAttribute("account") != null) {
                request.getRequestDispatcher("/WEB-INF/views/sendMessage.jsp").forward(request, response);
            } else {
                request.setAttribute("message", "Vui lòng đăng nhập để liên hệ hỗ trợ.");
                request.getRequestDispatcher("/WEB-INF/common/Login.jsp").forward(request, response);
            }

        } else if ("send".equals(action)) {
            String email = ((Account) session.getAttribute("account")).getEmail();
            String subject = request.getParameter("subject");
            String content = request.getParameter("content");
            String recipient = request.getParameter("recipient");
            Message msg = new Message();
            msg.setSenderEmail(email);
            msg.setRecipientEmail(recipient);
            msg.setSubject(subject);
            msg.setContent(content);
            SupportContact dao = new SupportContact();
            dao.insertMessage(msg);
            request.setAttribute("notify", "✅ Tin nhắn của bạn đã được gửi thành công!");
            request.getRequestDispatcher("/WEB-INF/views/sendMessage.jsp").forward(request, response);
        } else if ("inbox".equals(action)) {
            if (session != null && session.getAttribute("account") != null) {
                Account acc = (Account) session.getAttribute("account");
                String email = acc.getEmail();
                SupportContact dao = new SupportContact();
                List<Message> inbox = dao.getMessagesByRecipient(email);
                request.setAttribute("messages", inbox);
                request.getRequestDispatcher("/WEB-INF/views/mailBox.jsp").forward(request, response);
            } else {
                request.setAttribute("message", "Vui lòng đăng nhập để xem hòm thư.");
                request.getRequestDispatcher("/WEB-INF/common/Login.jsp").forward(request, response);
            }
            request.getRequestDispatcher("/WEB-INF/views/mailBoxdetail.jsp").forward(request, response);
        }
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
