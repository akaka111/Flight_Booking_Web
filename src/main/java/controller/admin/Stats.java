/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.admin;

import DAO.Admin.StatisticPerMonth;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import utils.DBContext;

/**
 *
 * @author ADMIN
 */
@WebServlet(name = "Stats", urlPatterns = {"/Stats"})
public class Stats extends HttpServlet {

    StatisticPerMonth dao = new StatisticPerMonth();

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
            out.println("<title>Servlet Stats</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Stats at " + request.getContextPath() + "</h1>");
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

        // --- Lấy month/year từ request hoặc mặc định tháng hiện tại ---
        int m = Integer.parseInt(
                request.getParameter("month") != null ? request.getParameter("month")
                : String.valueOf(LocalDate.now().getMonthValue())
        );
        int y = Integer.parseInt(
                request.getParameter("year") != null ? request.getParameter("year")
                : String.valueOf(LocalDate.now().getYear())
        );
        String airlineIdParam = request.getParameter("airlineId");
        Integer airlineId = (airlineIdParam != null && !airlineIdParam.isEmpty()) ? Integer.parseInt(airlineIdParam) : null;

        // --- Set dữ liệu chart (theo tháng của cả năm) ---
        Map<Integer, Double> revenueByMonth = new HashMap<>();
        Map<Integer, Integer> accountByMonth = new HashMap<>();

        for (int month = 1; month <= 12; month++) {
            revenueByMonth.put(month, dao.getRevenueMonthYear(month, y));
            accountByMonth.put(month, dao.countUsersInMonthYear(month, y));
        }
        // Dữ liệu tổng quan theo hãng (nếu chọn)
        double revenue = (airlineId != null) ? dao.getRevenueByAirlineMonthYear(airlineId, m, y) : dao.getRevenueMonthYear(m, y);
        int completedFlights = (airlineId != null) ? dao.countFlightsByAirlineAndStatus(airlineId, "ON TIME", m, y) : dao.getCompletedFlightsByYear(y);
        int cancelledFlights = (airlineId != null) ? dao.countFlightsByAirlineAndStatus(airlineId, "CANCELLED", m, y) : dao.getCancelledFlightsByYear(y);
        int delayedFlights = (airlineId != null) ? dao.countFlightsByAirlineAndStatus(airlineId, "DELAYED", m, y) : dao.getDelayedFlightsByYear(y);
        int ticketsSold = (airlineId != null) ? dao.countTicketsSoldByAirline(airlineId, m, y) : dao.countTicketsSold(m, y);

        request.setAttribute("selectedMonth", m);
        request.setAttribute("selectedYear", y);
        request.setAttribute("revenue", revenue);
        request.setAttribute("completedFlights", completedFlights);
        request.setAttribute("cancelFlights", cancelledFlights);
        request.setAttribute("delayFlights", delayedFlights);
        request.setAttribute("ticketsSold", ticketsSold);
        request.setAttribute("revenueByMonth", revenueByMonth);
        request.setAttribute("accountByMonth", accountByMonth);
        request.setAttribute("airlineId", airlineId);

        // --- Lấy tham số trạng thái (nếu có) ---
        String status = request.getParameter("status");
        String sqlStatus = null;
        if (status != null) {
            switch (status) {
                case "onTime":
                    sqlStatus = "ON TIME";
                    break;
                case "delayed":
                    sqlStatus = "DELAYED";
                    break;
                case "cancelled":
                    sqlStatus = "CANCELLED";
                    break;
            }
        }
        // --- Lấy số liệu chung ---
        int accountCount = dao.countUsersInMonthYear(m, y);
        double totalRevenue = dao.getRevenueMonthYear(m, y);

        int cancelFlights = dao.getCancelledFlightsByYear(y);
        int delayFlights = dao.getDelayedFlightsByYear(y);

        // --- Lấy số liệu theo trạng thái cụ thể (nếu có) ---
        if (sqlStatus != null) {
            int flightCount = dao.countFlightsByStatus(sqlStatus);
            request.setAttribute("flightStatus", sqlStatus);
            request.setAttribute("flightCount", flightCount);
        }

        // --- Set attribute để render JSP ---
        request.setAttribute("selectedMonth", m);
        request.setAttribute("selectedYear", y);
        request.setAttribute("accountCount", accountCount);
        request.setAttribute("revenue", totalRevenue);
        request.setAttribute("ticketsSold", ticketsSold);

        request.setAttribute("completedFlights", completedFlights);
        request.setAttribute("cancelFlights", cancelFlights);
        request.setAttribute("delayFlights", delayFlights);
        request.setAttribute("airlineList", dao.getAllAirlines());

        // --- Forward về trang JSP duy nhất ---
        request.getRequestDispatcher("/WEB-INF/admin/statistics.jsp").forward(request, response);
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
