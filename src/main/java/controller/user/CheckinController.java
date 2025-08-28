package controller.user;

import DAO.Admin.BookingDAO;
import DAO.Admin.CheckInDAO;
import DAO.Admin.FlightDAO;
import DAO.Admin.PassengerDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import model.Booking;
import model.CheckIn;
import model.Flight;
import model.Passenger;

@WebServlet(name = "CheckinController", urlPatterns = {"/checkinController"})
public class CheckinController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String step = request.getParameter("step");
        if (step == null || step.equals("enter")) {
            request.getRequestDispatcher("/WEB-INF/user/checkin_enter_code.jsp").forward(request, response);
        } else if (step.equals("flight")) {
            String code = request.getParameter("code");
            BookingDAO bookingDAO = new BookingDAO();
            Booking booking = bookingDAO.getBookingByCode(code);

            System.out.println("DEBUG - Mã code nhập vào: " + code);
            System.out.println("DEBUG - Booking tìm được: " + (booking != null ? booking.getBookingCode() : "null"));

            if (booking == null) {
                request.setAttribute("error", "Không tìm thấy mã đặt chỗ.");
                request.getRequestDispatcher("/WEB-INF/user/checkin_enter_code.jsp").forward(request, response);
                return;
            }

            Flight flight = new FlightDAO().getFlightById(booking.getFlightId());
            System.out.println("DEBUG - Flight tìm được: " + (flight != null ? flight.getFlightNumber() : "null"));

            request.setAttribute("booking", booking);
            request.setAttribute("flight", flight);
            request.getRequestDispatcher("/WEB-INF/user/checkin_flight_info.jsp").forward(request, response);

        } else if (step.equals("confirm")) {
            int bookingId = Integer.parseInt(request.getParameter("bookingId"));
            BookingDAO bookingDAO = new BookingDAO();
            Booking booking = bookingDAO.getBookingById(bookingId);
            System.out.println("DEBUG - Booking tìm theo ID: " + bookingId + " → " + (booking != null ? booking.getBookingCode() : "null"));

            Flight flight = new FlightDAO().getFlightById(booking.getFlightId());
            System.out.println("DEBUG - Flight tìm được: " + (flight != null ? flight.getFlightNumber() : "null"));

            Passenger passenger = new PassengerDAO().getPassengerByBookingId(bookingId);
            System.out.println("DEBUG - Passenger tìm được: " + (passenger != null ? passenger.getFullName() : "null"));

            request.setAttribute("booking", booking);
            request.setAttribute("flight", flight);
            request.setAttribute("passenger", passenger);
            request.getRequestDispatcher("/WEB-INF/user/checkin-passenger.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String step = request.getParameter("step");

        if ("complete".equals(step)) {
            int bookingId = Integer.parseInt(request.getParameter("bookingId"));
            BookingDAO bookingDAO = new BookingDAO();
            Booking booking = bookingDAO.getBookingById(bookingId);
            System.out.println("DEBUG - POST bookingId: " + bookingId);

            if (booking == null || "Checked-in".equalsIgnoreCase(booking.getCheckInStatus())) {
                System.out.println("DEBUG - Booking null hoặc đã check-in.");
                response.sendRedirect("checkinController?step=confirm&bookingId=" + bookingId);
                return;
            }

            Flight flight = new FlightDAO().getFlightById(booking.getFlightId());
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime departure = flight.getDepartureTime().toLocalDateTime();

            System.out.println("DEBUG - Giờ hiện tại: " + now);
            System.out.println("DEBUG - Giờ khởi hành: " + departure);

            if (now.isBefore(departure.minusHours(24)) || now.isAfter(departure.minusHours(2))) {
                System.out.println("DEBUG - Không nằm trong khoảng 24h - 2h trước khi bay.");
                response.sendRedirect("checkinController?step=confirm&bookingId=" + bookingId);
                return;
            }

            // Check-in cho tất cả hành khách
            CheckInDAO checkInDAO = new CheckInDAO();
            PassengerDAO passengerDAO = new PassengerDAO();
            for (Passenger passenger : passengerDAO.getPassengersByBookingId(bookingId)) {
                CheckIn checkIn = new CheckIn();
                checkIn.setPassengerId(passenger.getPassengerId());
                checkIn.setBookingId(bookingId);
                checkIn.setFlightId(flight.getFlightId());
                checkIn.setCheckinTime(Timestamp.from(Instant.now()));
                checkIn.setStatus("Checked-in");
                checkInDAO.insertCheckin(checkIn);
            }

            bookingDAO.updateCheckinStatus(bookingId, "Checked-in");
            System.out.println("DEBUG - Đã check-in tất cả hành khách và cập nhật trạng thái.");

            // Chuyển sang trang success
            request.setAttribute("booking", booking);
            request.setAttribute("flight", flight);
            request.setAttribute("passengers", passengerDAO.getPassengersByBookingId(bookingId));
            request.getRequestDispatcher("/WEB-INF/user/checkin-success.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Check-in Controller";
    }
}
