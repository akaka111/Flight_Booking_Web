/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller.staff;

import DAO.Staff.TicketPriceDAO;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.SeatClass;
import model.TicketClass;

@WebServlet(name = "TicketPriceController", urlPatterns = {"/ticketPrice"})
public class TicketPriceController extends HttpServlet {
    private TicketPriceDAO ticketPriceDAO;

    @Override
    public void init() throws ServletException {
        ticketPriceDAO = new TicketPriceDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        try {
            if (action == null || action.isEmpty()) {
                // List all flights
                List<Object[]> flights = ticketPriceDAO.getFlightsWithPriceStatus();
                request.setAttribute("flights", flights);
                request.getRequestDispatcher("/WEB-INF/staff/ticketPriceList.jsp").forward(request, response);
            } else if (action.equals("update")) {
                // Show update form for a specific flight
                int flightId = Integer.parseInt(request.getParameter("flightId"));
                List<SeatClass> seatClasses = ticketPriceDAO.getSeatClassesForFlight(flightId);
                List<TicketClass> ticketClasses = ticketPriceDAO.getTicketPricesForFlight(flightId);
                request.setAttribute("flightId", flightId);
                request.setAttribute("seatClasses", seatClasses);
                request.setAttribute("ticketClasses", ticketClasses);
                request.getRequestDispatcher("/WEB-INF/staff/updateTicketPrice.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
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
            response.sendRedirect("ticketPrice");
        } catch (SQLException e) {
            throw new ServletException("Error saving ticket prices", e);
        } catch (NumberFormatException e) {
            throw new ServletException("Invalid input data", e);
        }
    }
}