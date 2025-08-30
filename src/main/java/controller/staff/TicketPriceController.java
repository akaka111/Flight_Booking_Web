/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller.staff;

import DAO.staff.TicketPriceDAO;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Flight;
import model.SeatClass;
import model.TicketClass;

/**
 *
 * @author Khoa
 */
@WebServlet(name = "TicketPriceController", urlPatterns = {"/ticketPrice"})
public class TicketPriceController extends HttpServlet {

    private TicketPriceDAO ticketPriceDAO;

    @Override
    public void init() throws ServletException {
        ticketPriceDAO = new TicketPriceDAO();
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>.
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        try {
            if (action == null || action.isEmpty()) {
                // Hiển thị danh sách chuyến bay với trạng thái giá vé
                List<Object[]> flights = ticketPriceDAO.getFlightsWithPriceStatus();
                request.setAttribute("flights", flights);
                request.getRequestDispatcher("/WEB-INF/staff/ticketPriceList.jsp").forward(request, response);

            } else if (action.equals("update")) {
                // Hiển thị form cập nhật giá vé cho một chuyến bay cụ thể
                int flightId = Integer.parseInt(request.getParameter("flightId"));
                List<SeatClass> seatClasses = ticketPriceDAO.getSeatClassesForFlight(flightId);
                List<TicketClass> ticketClasses = ticketPriceDAO.getTicketPricesForFlight(flightId);

                Flight flight = ticketPriceDAO.getFlightDetails2(flightId);
                
                request.setAttribute("flightId", flightId);
                request.setAttribute("seatClasses", seatClasses);
                request.setAttribute("ticketClasses", ticketClasses);
                 request.setAttribute("flight", flight); 

                request.getRequestDispatcher("/WEB-INF/staff/updateTicketPrice.jsp").forward(request, response);
            }

        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int flightId = Integer.parseInt(request.getParameter("flightId"));
            String[] seatClassIds = request.getParameterValues("seatClassId");
            String[] prices = request.getParameterValues("price");

            if (seatClassIds != null && prices != null && seatClassIds.length == prices.length) {
                for (int i = 0; i < seatClassIds.length; i++) {
                    int seatClassId = Integer.parseInt(seatClassIds[i]);
                    double price = Double.parseDouble(prices[i]);
                    ticketPriceDAO.saveTicketPrice(flightId, seatClassId, price);
                }
            }

            response.sendRedirect(request.getContextPath() + "/ticketPrice");

        } catch (SQLException e) {
            throw new ServletException("Error saving ticket prices", e);
        } catch (NumberFormatException e) {
            throw new ServletException("Invalid input data", e);
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet for managing ticket prices";
    }

}
