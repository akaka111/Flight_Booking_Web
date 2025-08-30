
package controller.user;

import DAO.Admin.FlightDAO;
import DAO.Admin.TicketClassDAO;
import model.Flight;
import model.TicketClass;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "SearchFlightController", urlPatterns = {"/search"})
public class SearchFlightController extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(SearchFlightController.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        FlightDAO flightDAO = new FlightDAO();
        TicketClassDAO ticketDAO = new TicketClassDAO();
        try {
            String routeIdStr = request.getParameter("routeId");
            String departureDateStr = request.getParameter("departureDate");
            if (routeIdStr != null && departureDateStr != null && !routeIdStr.isEmpty() && !departureDateStr.isEmpty()) {
                try {
                    int routeId = Integer.parseInt(routeIdStr);
                    Date departureDate = Date.valueOf(departureDateStr);
                    List<Flight> flights = flightDAO.searchFlightsByRoute(routeId, departureDate);
                    Map<Integer, List<TicketClass>> ticketClassesMap = new HashMap<>();
                    if (flights != null && !flights.isEmpty()) {
                        for (Flight flight : flights) {
                            List<TicketClass> ticketClasses = ticketDAO.getTicketClassesByFlightId(flight.getFlightId());
                            ticketClassesMap.put(flight.getFlightId(), ticketClasses);
                        }
                        request.setAttribute("flight", flights.get(0)); // Hỗ trợ tương thích ngược
                        request.setAttribute("ticketClasses", ticketClassesMap.get(flights.get(0).getFlightId())); // Hỗ trợ tương thích ngược
                    }
                    request.setAttribute("flights", flights);
                    request.setAttribute("ticketClassesMap", ticketClassesMap);
                    request.setAttribute("routeId", routeIdStr);
                    request.setAttribute("departureDate", departureDateStr);
                    request.getRequestDispatcher("/WEB-INF/user/flight-detail.jsp").forward(request, response);
                } catch (NumberFormatException e) {
                    LOGGER.log(Level.SEVERE, "Invalid routeId format: " + routeIdStr, e);
                    response.sendRedirect("home?error=" + URLEncoder.encode("Invalid route ID", StandardCharsets.UTF_8.toString()));
                    return;
                } catch (IllegalArgumentException e) {
                    LOGGER.log(Level.SEVERE, "Invalid date format: " + departureDateStr, e);
                    response.sendRedirect("home?error=" + URLEncoder.encode("Invalid date format", StandardCharsets.UTF_8.toString()));
                    return;
                }
            } else {
                response.sendRedirect("home?error=" + URLEncoder.encode("Missing or empty search parameters", StandardCharsets.UTF_8.toString()));
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving flight data", e);
            String errorMessage = "Failed to retrieve flight data: " + e.getMessage();
            response.sendRedirect("home?error=" + URLEncoder.encode(errorMessage, StandardCharsets.UTF_8.toString()));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "SearchFlightController for searching flights by route";
    }
}
