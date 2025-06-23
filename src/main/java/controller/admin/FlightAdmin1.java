/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.admin;

import DAO.FlightDAO;
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

/**
 *
 * @author Admin
 */
@WebServlet("/FlightAdmin1")
public class FlightAdmin1 extends HttpServlet {

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
            out.println("<title>Servlet AdminController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AdminController at " + request.getContextPath() + "</h1>");
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
        String action = request.getParameter("action");
        if (action == null) {
            action = "listFlights"; // mặc định
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
                request.setAttribute("flights", dao.getAllFlights()); // để hiển thị lại list
                request.getRequestDispatcher("/WEB-INF/admin/editFlight.jsp").forward(request, response);
                break;
            default:
                response.getWriter().println(" Action không hợp lệ trong doGet.");
                break;
        }

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
        String action = request.getParameter("action");
        if ("addFlight".equals(action)) {
            Flight f = new Flight();
            f.setFlightNumber(request.getParameter("flightNumber"));
            f.setRouteFrom(request.getParameter("routeFrom"));
            f.setRouteTo(request.getParameter("routeTo"));

            String rawDeparture = request.getParameter("departureTime").replace("T", " ") + ":00";
            f.setDepartureTime(Timestamp.valueOf(rawDeparture));

            String rawArrival = request.getParameter("arrivalTime").replace("T", " ") + ":00";
            f.setArrivalTime(Timestamp.valueOf(rawArrival));

            f.setPrice(Double.parseDouble(request.getParameter("price")));
            f.setAircraft(request.getParameter("aircraft"));
            f.setStatus(request.getParameter("status"));

            new FlightDAO().insertFlight(f);

            // Có thể forward về lại trang JSP với message
            List<Flight> flights = new FlightDAO().getAllFlights();
            request.setAttribute("flights", flights);
            request.setAttribute("msg", "✅ Thêm chuyến bay thành công!");
            request.getRequestDispatcher("//WEB-INF/admin/manageFlights.jsp").forward(request, response);
        } 
        else if ("updateFlight".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            Flight f = new Flight();
            f.setFlightId(id);
            f.setFlightNumber(request.getParameter("flightNumber"));
            f.setRouteFrom(request.getParameter("routeFrom"));
            f.setRouteTo(request.getParameter("routeTo"));
            f.setDepartureTime(Timestamp.valueOf(request.getParameter("departureTime") + ":00"));
            f.setArrivalTime(Timestamp.valueOf(request.getParameter("arrivalTime") + ":00"));
            f.setPrice(Double.parseDouble(request.getParameter("price")));
            f.setAircraft(request.getParameter("aircraft"));
            f.setStatus(request.getParameter("status"));

            new FlightDAO().updateFlight(f);

            request.setAttribute("msg", "✅ Cập nhật chuyến bay thành công!");
            List<Flight> flights = new FlightDAO().getAllFlights();
            request.setAttribute("flights", flights);
            request.getRequestDispatcher("/WEB-INF/admin/manageFlights.jsp").forward(request, response);
        } 
        else if ("deleteFlight".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            new FlightDAO().deleteFlight(id);

            request.setAttribute("msg", "🗑️ Đã xoá chuyến bay.");
            List<Flight> flights = new FlightDAO().getAllFlights();
            request.setAttribute("flights", flights);
            request.getRequestDispatcher("/WEB-INF/admin/manageFlights.jsp").forward(request, response);
        } else {
            response.getWriter().println("❌ Action không hợp lệ trong doPost.");
        }
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
