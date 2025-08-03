package controller.user;

import DAO.Admin.BookingDAO;
import DAO.Admin.FlightDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Account;
import model.Booking;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Flight;

@WebServlet(name = "ManageBooking", urlPatterns = {"/manageBooking"})
public class ManageBooking extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Account user = (session != null) ? (Account) session.getAttribute("user") : null;

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int userId = user.getUserId();
        BookingDAO bookingDAO = new BookingDAO();
        FlightDAO flightDAO = new FlightDAO();

        List<Booking> bookings = bookingDAO.getBookingsByUserId(userId);
        Map<Integer, Flight> flightMap = new HashMap<>();

        for (Booking booking : bookings) {
            int flightId = booking.getFlightId();
            if (!flightMap.containsKey(flightId)) {
                System.out.println("BookingId: " + booking.getBookingId() + " - FlightId: " + flightId);
                Flight flight = flightDAO.getFlightById(flightId);
                System.out.println("Flight: " + flight);
                if (flight != null) {
                    flightMap.put(flightId, flight);
                }
            }
        }

        request.setAttribute("bookings", bookings);
        request.setAttribute("flightMap", flightMap); // map flightId → Flight
        request.getRequestDispatcher("/WEB-INF/user/booking-list.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Account user = (session != null) ? (Account) session.getAttribute("user") : null;

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            int bookingId = Integer.parseInt(request.getParameter("bookingId"));
            BookingDAO dao = new BookingDAO();
            Booking booking = dao.getBookingById(bookingId);

            if (booking == null || booking.getUserId() != user.getUserId()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền hủy booking này.");
                return;
            }

            dao.cancelBooking(bookingId);
            response.sendRedirect(request.getContextPath() + "/manageBooking");

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID đặt chỗ không hợp lệ.");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi hệ thống.");
        }
    }

    @Override
    public String getServletInfo() {
        return "Quản lý đặt chỗ người dùng";
    }
}
