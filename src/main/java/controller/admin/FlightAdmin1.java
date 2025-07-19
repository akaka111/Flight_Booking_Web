/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.admin;

import DAO.Admin.FlightDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Flight;

@WebServlet("/FlightAdmin1")
public class FlightAdmin1 extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AdminController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AdminController at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "listFlights";
        }

        switch (action) {
            case "listFlights":
                List<Flight> flights = new FlightDAO().getAllFlights();
                request.setAttribute("flights", flights);
                request.getRequestDispatcher("/WEB-INF/admin/manageFlights.jsp").forward(request, response);
                break;

            case "showAddForm":
                request.getRequestDispatcher("/WEB-INF/admin/addFlight.jsp").forward(request, response);
                break;

            case "editFlight":
                int id = Integer.parseInt(request.getParameter("id"));
                FlightDAO dao = new FlightDAO();
                Flight flight = dao.getFlightById(id);
                request.setAttribute("flight", flight);
                request.setAttribute("flights", dao.getAllFlights());
                request.getRequestDispatcher("/WEB-INF/admin/editFlight.jsp").forward(request, response);
                break;
            default:
                response.getWriter().println(" Action không hợp lệ trong doGet.");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if ("addFlight".equals(action)) {
                Flight f = new Flight();
                f.setFlightNumber(request.getParameter("flightNumber"));
                f.setRouteFrom(request.getParameter("routeFrom"));
                f.setRouteTo(request.getParameter("routeTo"));

                String rawDeparture = request.getParameter("departureTime").replace("T", " ") + ":00";
                f.setDepartureTime(Timestamp.valueOf(rawDeparture));

                String rawArrival = request.getParameter("arrivalTime").replace("T", " ") + ":00";
                f.setArrivalTime(Timestamp.valueOf(rawArrival));

                String airlineParam = request.getParameter("airlineId");
                if (airlineParam == null || airlineParam.trim().isEmpty()) {
                    request.setAttribute("error", "Vui lòng chọn hãng bay!");
                    request.getRequestDispatcher("/WEB-INF/admin/addFlight.jsp").forward(request, response);
                    return;
                }
                int airlineId = Integer.parseInt(airlineParam.trim());
                f.setAirlineId(airlineId);
                f.setSeatCount(Integer.parseInt(request.getParameter("seatCount")));
                f.setStatus(request.getParameter("status"));

                new FlightDAO().insertFlight(f);

                List<Flight> flights = new FlightDAO().getAllFlights();
                request.setAttribute("flights", flights);
                request.setAttribute("msg", "Thêm chuyến bay thành công!");
                request.getRequestDispatcher("/WEB-INF/admin/manageFlights.jsp").forward(request, response);

            } else if ("updateFlight".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                Flight f = new Flight();
                f.setFlightId(id);
                f.setFlightNumber(request.getParameter("flightNumber"));
                f.setRouteFrom(request.getParameter("routeFrom"));
                f.setRouteTo(request.getParameter("routeTo"));

                String rawDeparture = request.getParameter("departureTime").replace("T", " ") + ":00";
                f.setDepartureTime(Timestamp.valueOf(rawDeparture));

                String rawArrival = request.getParameter("arrivalTime").replace("T", " ") + ":00";
                f.setArrivalTime(Timestamp.valueOf(rawArrival));

                String airlineParam = request.getParameter("airlineId");
                if (airlineParam == null || airlineParam.trim().isEmpty()) {
                    request.setAttribute("error", "Vui lòng chọn hãng bay!");
                    request.getRequestDispatcher("/WEB-INF/admin/addFlight.jsp").forward(request, response);
                    return;
                }
                int airlineId = Integer.parseInt(airlineParam.trim());
                f.setAirlineId(airlineId);
                f.setSeatCount(Integer.parseInt(request.getParameter("seatCount")));
                f.setStatus(request.getParameter("status"));

                new FlightDAO().updateFlight(f);

                request.setAttribute("msg", "Cập nhật chuyến bay thành công!");
                List<Flight> flights = new FlightDAO().getAllFlights();
                request.setAttribute("flights", flights);
                request.getRequestDispatcher("/WEB-INF/admin/manageFlights.jsp").forward(request, response);

            } else if ("deleteFlight".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                new FlightDAO().deleteFlight(id);

                request.setAttribute("msg", "️ Đã xoá chuyến bay.");
                List<Flight> flights = new FlightDAO().getAllFlights();
                request.setAttribute("flights", flights);
                request.getRequestDispatcher("/WEB-INF/admin/manageFlights.jsp").forward(request, response);

            } else {
                response.getWriter().println("Action không hợp lệ trong doPost.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Đã xảy ra lỗi: " + e.getMessage());
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
