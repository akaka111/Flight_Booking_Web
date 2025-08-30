package controller.user;

import DAO.Admin.BookingDAO;
import DAO.Admin.SeatDAO;
import model.Seat;
import model.Booking;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import jakarta.servlet.RequestDispatcher;

@WebServlet(name = "ManageSeatController", urlPatterns = {"/manageSeatController"})
public class ManageSeatController extends HttpServlet {

    private final SeatDAO seatDAO = new SeatDAO();
    private final BookingDAO bookingDAO = new BookingDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("=== [doGet] ===");

        String bookingIdStr = request.getParameter("bookingId");
        String flightIdStr = request.getParameter("flightId");
        HttpSession session = request.getSession();

        System.out.println("bookingIdStr: " + bookingIdStr);
        System.out.println("flightIdStr: " + flightIdStr);

        try {
            List<Seat> seats;
            int flightId;
            String seatClass;
            int classId;

            // --- LUỒNG SỬA ĐỔI VÉ ---
            if (bookingIdStr != null) {
                int bookingId = Integer.parseInt(bookingIdStr);
                Booking booking = bookingDAO.getBookingById(bookingId);
                if (booking == null) {
                    System.out.println("Booking not found with ID: " + bookingId);
                    return;
                }

                flightId = booking.getFlightId();
                seatClass = booking.getSeatClass();
                classId = seatDAO.getClassId(seatClass);

                System.out.println("Editing Booking: flightId=" + flightId + ", seatClass=" + seatClass + ", classId=" + classId);

                seats = seatDAO.getSeatsByFlightAndClass(flightId, classId);

                int currentSeatId = booking.getSeatId();
                System.out.println("Current seatId: " + currentSeatId);

                request.setAttribute("currentSeatId", currentSeatId);
                request.setAttribute("bookingId", bookingId);

            } else if (flightIdStr != null) {
                // --- LUỒNG ĐẶT VÉ MỚI ---
                flightId = Integer.parseInt(flightIdStr);
                Booking tempBooking = (Booking) session.getAttribute("tempBooking");
                System.out.println("TempBooking from session: " + tempBooking);

                if (tempBooking == null) {
                    System.out.println("Temp booking not found in session.");
                    return;
                }

                seatClass = tempBooking.getSeatClass();
                classId = seatDAO.getClassId(seatClass);

                System.out.println("New Booking: flightId=" + flightId + ", seatClass=" + seatClass + ", classId=" + classId);

                seats = seatDAO.getSeatsByFlightAndClass(flightId, classId);

            } else {
                System.out.println("Missing flightId and bookingId.");
                return;
            }

            System.out.println("Total seats fetched: " + seats.size());
            for (Seat seat : seats) {
                System.out.println("Seat ID: " + seat.getSeatId() + ", Booked: " + seat.isBooked());
            }

            request.setAttribute("seatList", seats);
            request.setAttribute("flightId", flightId);

            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/user/seatSelection.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("errorPage.jsp");
        }
    }

    private int convertSeatClassToId(String seatClass) {
        if (seatClass == null) {
            return 0;
        }

        if ("Economy".equalsIgnoreCase(seatClass)) {
            return 1;
        } else if ("Business".equalsIgnoreCase(seatClass)) {
            return 2;
        } else if ("Deluxe".equalsIgnoreCase(seatClass)) {
            return 3;
        }

        return 0;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("=== [doPost] ===");
        String bookingIdStr = request.getParameter("bookingId");
        System.out.println("POST bookingId: " + bookingIdStr);

        if (bookingIdStr != null) {
            handleUpdateSeat(request, response);
        } else {
            handleNewBookingSeat(request, response);
        }
    }

    private void handleNewBookingSeat(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        System.out.println("=== [handleNewBookingSeat] ===");

        String[] selectedSeatIds = request.getParameterValues("selectedSeats");

        if (selectedSeatIds == null || selectedSeatIds.length == 0) {
            System.out.println("No seat selected.");
            response.sendRedirect("manageSeatController?error=NoSeatSelected");
            return;
        }

        System.out.println("Selected Seat IDs: " + String.join(",", selectedSeatIds));

        try {
            HttpSession session = request.getSession();
            Booking tempBooking = (Booking) session.getAttribute("tempBooking");

            System.out.println("TempBooking from session: " + tempBooking);

            if (tempBooking != null && selectedSeatIds.length == 1) {
                int selectedSeatId = Integer.parseInt(selectedSeatIds[0]);
                System.out.println("Selected Seat ID: " + selectedSeatId);

                // KHÔNG cập nhật DB ở đây nữa, chỉ lưu vào session
                tempBooking.setSeatId(selectedSeatId);
                session.setAttribute("tempBooking", tempBooking);
                System.out.println("Seat " + selectedSeatId + " temporarily set in session");

                tempBooking.setSeatId(selectedSeatId);
                session.setAttribute("tempBooking", tempBooking);

                request.getRequestDispatcher("/WEB-INF/user/payment.jsp").forward(request, response);
            } else {
                System.out.println("Invalid booking or multiple seats selected.");
                response.sendRedirect("errorPage.jsp");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("errorPage.jsp");
        }
    }

    private void handleUpdateSeat(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        System.out.println("=== [handleUpdateSeat] ===");

        String bookingIdStr = request.getParameter("bookingId");
        String selectedSeatIdStr = request.getParameter("selectedSeatId");

        System.out.println("bookingIdStr: " + bookingIdStr + ", selectedSeatIdStr: " + selectedSeatIdStr);

        if (bookingIdStr == null || selectedSeatIdStr == null) {
            System.out.println("Missing bookingId or selectedSeatId");
            return;
        }

        try {
            int bookingId = Integer.parseInt(bookingIdStr);
            int newSeatId = Integer.parseInt(selectedSeatIdStr);

            Booking booking = bookingDAO.getBookingById(bookingId);
            if (booking == null) {
                System.out.println("Booking not found: ID = " + bookingId);
                return;
            }

            int oldSeatId = booking.getSeatId();
            System.out.println("Old Seat ID: " + oldSeatId + ", New Seat ID: " + newSeatId);

            if (newSeatId != oldSeatId) {
                seatDAO.updateSeatBooking(oldSeatId, false);
                System.out.println("Seat " + oldSeatId + " updated to booked = false");

                seatDAO.updateSeatBooking(newSeatId, true);
                System.out.println("Seat " + newSeatId + " updated to booked = true");

                bookingDAO.updateBookingSeat(bookingId, newSeatId);
                System.out.println("Booking " + bookingId + " updated with new seat ID = " + newSeatId);
            } else {
                System.out.println("User selected the same seat. No update needed.");
            }

            response.sendRedirect("bookingDetail?bookingId=" + bookingId + "&success=SeatChanged");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("errorPage.jsp");
        }
    }
}
