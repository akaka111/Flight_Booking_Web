package controller.admin;

import DAO.Admin.FlightDAO;
import DAO.Admin.AirlineDAO;
import DAO.Admin.RouteDAO;
import DAO.Admin.AircraftTypeDAO;
import model.Flight;
import model.Airline;
import model.Route;
import model.AircraftType;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "FlightAdmin1", urlPatterns = {"/FlightAdmin1"})
public class FlightAdmin1 extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(FlightAdmin1.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        FlightDAO flightDAO = new FlightDAO();
        String action = request.getParameter("action");
        if (action == null) {
            action = "listFlights";
        }

        try {
            switch (action) {
                case "listFlights":
                    List<Flight> flights = flightDAO.getAllFlights();
                    request.setAttribute("flights", flights);
                    request.getRequestDispatcher("/WEB-INF/admin/manageFlights.jsp").forward(request, response);
                    break;

                case "showAddForm":
                    AirlineDAO airlineDAO = new AirlineDAO();
                    RouteDAO routeDAO = new RouteDAO();
                    AircraftTypeDAO aircraftTypeDAO = new AircraftTypeDAO();
                    List<Airline> airlines = airlineDAO.getAllAirlines();
                    List<Route> routes = routeDAO.getAllRoutes();
                    List<AircraftType> aircraftTypes = aircraftTypeDAO.getAllAircraftTypes();
                    request.setAttribute("airlines", airlines);
                    request.setAttribute("routes", routes);
                    request.setAttribute("aircraftTypes", aircraftTypes);
                    request.getRequestDispatcher("/WEB-INF/admin/addFlight.jsp").forward(request, response);
                    break;

                case "editFlight":
                    int flightId = Integer.parseInt(request.getParameter("id"));
                    Flight flight = flightDAO.getFlightById(flightId);
                    if (flight != null) {
                        airlineDAO = new AirlineDAO();
                        routeDAO = new RouteDAO();
                        aircraftTypeDAO = new AircraftTypeDAO();
                        airlines = airlineDAO.getAllAirlines();
                        routes = routeDAO.getAllRoutes();
                        aircraftTypes = aircraftTypeDAO.getAllAircraftTypes();
                        request.setAttribute("flight", flight);
                        request.setAttribute("airlines", airlines);
                        request.setAttribute("routes", routes);
                        request.setAttribute("aircraftTypes", aircraftTypes);
                        request.getRequestDispatcher("/WEB-INF/admin/editFlight.jsp").forward(request, response);
                    } else {
                        request.setAttribute("error", "Không tìm thấy chuyến bay với ID: " + flightId);
                        request.setAttribute("flights", flightDAO.getAllFlights());
                        request.getRequestDispatcher("/WEB-INF/admin/manageFlights.jsp").forward(request, response);
                    }
                    break;

                default:
                    request.setAttribute("error", "Hành động không hợp lệ: " + action);
                    request.setAttribute("flights", flightDAO.getAllFlights());
                    request.getRequestDispatcher("/WEB-INF/admin/manageFlights.jsp").forward(request, response);
                    break;
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing GET request", e);
            request.setAttribute("error", "Lỗi khi xử lý yêu cầu: " + e.getMessage());
            request.setAttribute("flights", flightDAO.getAllFlights());
            request.getRequestDispatcher("/WEB-INF/admin/manageFlights.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        FlightDAO flightDAO = new FlightDAO();
        String action = request.getParameter("action");

        try {
            if ("addFlight".equals(action)) {
                Flight flight = new Flight();
                Airline airline = new Airline();
                airline.setAirlineId(Integer.parseInt(request.getParameter("airlineId")));
                flight.setAirline(airline);
                flight.setFlightNumber(request.getParameter("flightNumber"));
                flight.setDepartureTime(Timestamp.valueOf(request.getParameter("departureTime").replace("T", " ") + ":00"));
                flight.setArrivalTime(Timestamp.valueOf(request.getParameter("arrivalTime").replace("T", " ") + ":00"));
                flight.setStatus(request.getParameter("status"));
                Route route = new Route();
                route.setRouteId(Integer.parseInt(request.getParameter("routeId")));
                flight.setRoute(route);
                AircraftType aircraftType = new AircraftType();
                aircraftType.setAircraftTypeId(Integer.parseInt(request.getParameter("aircraftTypeId")));
                flight.setAircraftType(aircraftType);
                flightDAO.insertFlight(flight);
                request.setAttribute("msg", "Thêm chuyến bay thành công!");
            } else if ("deleteFlight".equals(action)) {
                int flightId = Integer.parseInt(request.getParameter("id"));
                flightDAO.deleteFlight(flightId);
                request.setAttribute("msg", "Xóa chuyến bay thành công!");
            } else if ("updateFlight".equals(action)) {
                Flight flight = new Flight();
                flight.setFlightId(Integer.parseInt(request.getParameter("flightId")));
                Airline airline = new Airline();
                airline.setAirlineId(Integer.parseInt(request.getParameter("airlineId")));
                flight.setAirline(airline);
                flight.setFlightNumber(request.getParameter("flightNumber"));
                flight.setDepartureTime(Timestamp.valueOf(request.getParameter("departureTime").replace("T", " ") + ":00"));
                flight.setArrivalTime(Timestamp.valueOf(request.getParameter("arrivalTime").replace("T", " ") + ":00"));
                flight.setStatus(request.getParameter("status"));
                Route route = new Route();
                route.setRouteId(Integer.parseInt(request.getParameter("routeId")));
                flight.setRoute(route);
                AircraftType aircraftType = new AircraftType();
                aircraftType.setAircraftTypeId(Integer.parseInt(request.getParameter("aircraftTypeId")));
                flight.setAircraftType(aircraftType);
                flightDAO.updateFlight(flight);
                request.setAttribute("msg", "Cập nhật chuyến bay thành công!");
            }
            List<Flight> flights = flightDAO.getAllFlights();
            request.setAttribute("flights", flights);
            request.getRequestDispatcher("/WEB-INF/admin/manageFlights.jsp").forward(request, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing POST request", e);
            request.setAttribute("error", "Lỗi khi xử lý yêu cầu: " + e.getMessage());
            request.setAttribute("flights", flightDAO.getAllFlights());
            request.getRequestDispatcher("/WEB-INF/admin/manageFlights.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "FlightAdmin1 Servlet for managing flights";
    }
}