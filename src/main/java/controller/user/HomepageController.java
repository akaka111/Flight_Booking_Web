package controller.user;

import DAO.Admin.FlightDAO;
import DAO.Admin.RouteDAO;
import DAO.Admin.TicketClassDAO;
import model.Flight;
import model.Route;
import model.TicketClass;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "HomepageController", urlPatterns = {"/home"})
public class HomepageController extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(HomepageController.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            FlightDAO flightDAO = new FlightDAO();
            RouteDAO routeDAO = new RouteDAO();
            TicketClassDAO ticketDAO = new TicketClassDAO();

            // Lấy danh sách tuyến bay cho dropdown
            List<Route> routes = routeDAO.getAllRoutes();
            request.setAttribute("routes", routes);

            // Lấy ngày hiện tại cho input date min
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String today = sdf.format(new java.util.Date());
            request.setAttribute("today", today);

            // Lấy danh sách chuyến bay hôm nay cho Featured Deals
            List<Flight> flights = flightDAO.getFlightsToday();
            // Map chứa flightId -> giá Eco
            Map<Integer, Double> ecoPrices = new HashMap<>();
            for (Flight flight : flights) {
                int flightId = flight.getFlightId();
                Double price = ticketDAO.getEcoPriceByFlightId(flightId);
                ecoPrices.put(flightId, price != null ? price : 0.0);
            }
            request.setAttribute("flights", flights);
            request.setAttribute("ecoPrices", ecoPrices);

            // Forward sang home.jsp
            request.getRequestDispatcher("/WEB-INF/views/home.jsp").forward(request, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving homepage data", e);
            response.sendRedirect("home?error=Failed to load homepage data: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response); // Gọi doGet để xử lý cả POST
    }

    @Override
    public String getServletInfo() {
        return "HomepageController for displaying homepage with flight search form and featured deals";
    }
}