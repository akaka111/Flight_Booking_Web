
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "FlightDetailController", urlPatterns = {"/flight-detail"})
public class FlightDetailController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu tham số id chuyến bay.");
            return;
        }
        int flightId;
        try {
            flightId = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Tham số id không hợp lệ.");
            return;
        }
        FlightDAO flightDAO = new FlightDAO();
        TicketClassDAO ticketDAO = new TicketClassDAO();
        Flight flight = flightDAO.getFlightById(flightId);
        Map<Integer, List<TicketClass>> ticketClassesMap = new HashMap<>();
        List<Flight> flights = new ArrayList<>();
        if (flight != null) {
            List<TicketClass> ticketClasses = ticketDAO.getTicketClassesByFlightId(flightId);
            ticketClassesMap.put(flightId, ticketClasses);
            flights.add(flight);
            request.setAttribute("flight", flight); // Hỗ trợ tương thích ngược
            request.setAttribute("ticketClasses", ticketClasses); // Hỗ trợ tương thích ngược
        }
        request.setAttribute("flights", flights);
        request.setAttribute("ticketClassesMap", ticketClassesMap);
        request.getRequestDispatcher("/WEB-INF/user/flight-detail.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "FlightDetailController for displaying flight details by ID";
    }
}
