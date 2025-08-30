///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
// */
//package controller.staff;
//
//import DAO.staff.messageDAO;
//import java.io.IOException;
//import java.io.PrintWriter;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import model.mailboxMessage;
//
///**
// *
// * @author ADMIN
// */
//@WebServlet(name = "messageDetail", urlPatterns = {"/messageDetail"})
//public class messageDetail extends HttpServlet {
//
//    /**
//     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
//     * methods.
//     *
//     * @param request servlet request
//     * @param response servlet response
//     * @throws ServletException if a servlet-specific error occurs
//     * @throws IOException if an I/O error occurs
//     */
//    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        response.setContentType("text/html;charset=UTF-8");
//        try (PrintWriter out = response.getWriter()) {
//            /* TODO output your page here. You may use following sample code. */
//            out.println("<!DOCTYPE html>");
//            out.println("<html>");
//            out.println("<head>");
//            out.println("<title>Servlet messageDetail</title>");
//            out.println("</head>");
//            out.println("<body>");
//            out.println("<h1>Servlet messageDetail at " + request.getContextPath() + "</h1>");
//            out.println("</body>");
//            out.println("</html>");
//        }
//    }
//
//    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
//    /**
//     * Handles the HTTP <code>GET</code> method.
//     *
//     * @param request servlet request
//     * @param response servlet response
//     * @throws ServletException if a servlet-specific error occurs
//     * @throws IOException if an I/O error occurs
//     */
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        String idStr = request.getParameter("id");
//        if (idStr != null) {
//            int id = Integer.parseInt(idStr);
//            messageDAO dao = new messageDAO();
//            mailboxMessage msg = dao.getMessageById(id);
//            request.setAttribute("message", msg);
//            request.getRequestDispatcher("/WEB-INF/staff/messageDetail.jsp").forward(request, response);
//        } else {
//            response.sendRedirect("textboxmailMessage");
//        }
//    }
//
//    /**
//     * Handles the HTTP <code>POST</code> method.
//     *
//     * @param request servlet request
//     * @param response servlet response
//     * @throws ServletException if a servlet-specific error occurs
//     * @throws IOException if an I/O error occurs
//     */
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//       String to = request.getParameter("to"); // recipient email
//        String subject = request.getParameter("subject");
//        String content = request.getParameter("content");
//        mailboxMessage reply = new mailboxMessage();
//        reply.setSenderEmail("A@staffexample.com"); 
//        reply.setRecipientEmail(to);
//        reply.setSubject(subject);
//        reply.setContent(content);
//        messageDAO dao = new messageDAO();
//        dao.insertMessage(reply);
//        response.sendRedirect("textboxmailMessage");
//    }
//
//    /**
//     * Returns a short description of the servlet.
//     *
//     * @return a String containing servlet description
//     */
//    @Override
//    public String getServletInfo() {
//        return "Short description";
//    }// </editor-fold>
//
//}
