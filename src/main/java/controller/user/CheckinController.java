/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
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
import java.time.LocalDateTime;
import model.Booking;
import model.CheckIn;
import model.Flight;
import model.Passenger;

/**
 *
 * @author Admin
 */
@WebServlet(name = "CheckinController", urlPatterns = {"/checkinController"})
public class CheckinController extends HttpServlet {

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
            out.println("<title>Servlet CheckinController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CheckinController at " + request.getContextPath() + "</h1>");
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
        String code = request.getParameter("code");

        if (code == null || code.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập mã đặt chỗ.");
            request.getRequestDispatcher("/WEB-INF/user/checkin.jsp").forward(request, response);
            return;
        }

        BookingDAO bookingDAO = new BookingDAO();
        Booking booking = bookingDAO.getBookingByCode(code); // phải có hàm này

        if (booking == null) {
            request.setAttribute("error", "Không tìm thấy thông tin đặt chỗ phù hợp.");
            request.getRequestDispatcher("/WEB-INF/user/checkin.jsp").forward(request, response);
            return;
        }

        FlightDAO flightDAO = new FlightDAO();
        Flight flight = flightDAO.getFlightById(booking.getFlightId());

        request.setAttribute("booking", booking);
        request.setAttribute("flight", flight);
        request.getRequestDispatcher("/WEB-INF/user/checkin.jsp").forward(request, response);
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

        int bookingId = Integer.parseInt(request.getParameter("bookingId"));
        System.out.println("=== DEBUG: bookingId = " + bookingId);

        BookingDAO bookingDAO = new BookingDAO();
        Booking booking = bookingDAO.getBookingById(bookingId);

        if (booking == null) {
            request.setAttribute("error", "Không tìm thấy thông tin đặt chỗ.");
            request.getRequestDispatcher("/WEB-INF/user/checkin.jsp").forward(request, response);
            return;
        }

        FlightDAO flightDAO = new FlightDAO();
        Flight flight = flightDAO.getFlightById(booking.getFlightId());

        LocalDateTime departureTime = flight.getDepartureTime().toLocalDateTime();
        LocalDateTime open = departureTime.minusHours(24);
        LocalDateTime close = departureTime.minusHours(2);
        LocalDateTime now = LocalDateTime.now();

        System.out.println("=== DEBUG TIME ===");
        System.out.println("Now: " + now);
        System.out.println("Open: " + open);
        System.out.println("Close: " + close);

        if (now.isBefore(open) || now.isAfter(close)) {
            request.setAttribute("error", "Check-in không khả dụng tại thời điểm này.");
            request.setAttribute("booking", booking);
            request.setAttribute("flight", flight);
            request.getRequestDispatcher("/WEB-INF/user/checkin.jsp").forward(request, response);
            return;
        }

        try {
            Passenger passenger = new Passenger();
            passenger.setBookingId(bookingId);
            passenger.setFullName(request.getParameter("fullName"));
            passenger.setPassportNumber(request.getParameter("passportNumber"));
            try {
                passenger.setDob(java.sql.Date.valueOf(request.getParameter("dob")));
            } catch (IllegalArgumentException e) {
                System.out.println("=== DOB FORMAT ERROR ===");
                e.printStackTrace();
                request.setAttribute("error", "Ngày sinh không hợp lệ. Định dạng đúng: yyyy-MM-dd");
                request.getRequestDispatcher("/WEB-INF/user/checkin.jsp").forward(request, response);
                return;
            }

            passenger.setGender(request.getParameter("gender"));
            passenger.setPhoneNumber(request.getParameter("phoneNumber"));
            passenger.setEmail(request.getParameter("email"));
            passenger.setCountry(request.getParameter("country"));
            passenger.setAddress(request.getParameter("address"));

            System.out.println("=== DEBUG Passenger ===");
            System.out.println("FullName: " + passenger.getFullName());
            System.out.println("Passport: " + passenger.getPassportNumber());
            System.out.println("DOB: " + passenger.getDob());
            System.out.println("Gender: " + passenger.getGender());
            System.out.println("Phone: " + passenger.getPhoneNumber());
            System.out.println("Email: " + passenger.getEmail());
            System.out.println("Country: " + passenger.getCountry());
            System.out.println("Address: " + passenger.getAddress());

            PassengerDAO passengerDAO = new PassengerDAO();
            int passengerId = passengerDAO.insertPassengerAndReturnId(passenger);
            System.out.println("=== DEBUG: passengerId after insert = " + passengerId);

            if (passengerId == -1) {
                throw new Exception("Không thể chèn hành khách.");
            }

            CheckIn checkIn = new CheckIn();
            checkIn.setPassengerId(passengerId);
            checkIn.setBookingId(bookingId);
            checkIn.setFlightId(flight.getFlightId());
            checkIn.setCheckinTime(LocalDateTime.now());
            checkIn.setStatus("Checked-in");

            System.out.println("=== DEBUG CheckIn ===");
            System.out.println("Passenger ID: " + checkIn.getPassengerId());
            System.out.println("Booking ID: " + checkIn.getBookingId());
            System.out.println("Flight ID: " + checkIn.getFlightId());
            System.out.println("Check-in Time: " + checkIn.getCheckinTime());
            System.out.println("Status: " + checkIn.getStatus());

            CheckInDAO checkInDAO = new CheckInDAO();
            checkInDAO.insertCheckin(checkIn);
System.out.println("=== DEBUG: Check-in record inserted successfully");
            bookingDAO.updateCheckinStatus(bookingId, "Checked-in");

            request.setAttribute("message", "Check-in thành công!");
            request.setAttribute("booking", booking);
            request.setAttribute("flight", flight);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Đã xảy ra lỗi khi check-in. Vui lòng thử lại.");
        }

        request.getRequestDispatcher("/WEB-INF/user/checkin.jsp").forward(request, response);
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
