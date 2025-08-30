///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
// */
//package controller.admin;
//
//import DAO.Admin.StatisticPerMonth;
//import java.io.IOException;
//import java.io.PrintWriter;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.sql.SQLException;
//import java.util.List;
//import model.Flight;
//
///**
// *
// * @author ADMIN
// */
//@WebServlet(name = "StatsStatus", urlPatterns = {"/StatsStatus"})
//public class StatsStatus extends HttpServlet {
//
//    StatisticPerMonth dao = new StatisticPerMonth();
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
//            out.println("<title>Servlet StatsStatus</title>");
//            out.println("</head>");
//            out.println("<body>");
//            out.println("<h1>Servlet StatsStatus at " + request.getContextPath() + "</h1>");
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
//        String status = request.getParameter("status");
//        String sqlStatus = null;
//        switch (status) {
//            case "onTime":
//                sqlStatus = "ON TIME";
//                break;
//            case "delayed":
//                sqlStatus = "DELAYED";
//                break;
//            case "cancelled":
//                sqlStatus = "CANCELLED";
//                break;
//        }
//
//        if (sqlStatus != null) {
//            try {
//                List<Flight> flightList = dao.getFlightsByStatus(sqlStatus);
//                System.out.println("Flight list size: " + flightList.size());
//                request.setAttribute("flightList", flightList);
//                request.setAttribute("flightStatus", sqlStatus);
//                request.getRequestDispatcher("/WEB-INF/admin/statisticsFlightstatus.jsp").forward(request, response);
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        } else {
//            response.sendRedirect("Stats");
//        }
//
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
//        processRequest(request, response);
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
