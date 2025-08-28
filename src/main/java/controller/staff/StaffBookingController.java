package controller.staff;

import DAO.Admin.AccountDAO;
import DAO.Admin.BookingDAO;
import model.Account;
import model.Booking;
import model.BookingHistory;
import model.CheckIn;
import model.Passenger;
import utils.DBContext;
import model.Flight;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Arrays;
import model.Seat;

/**
 * Servlet xử lý các yêu cầu liên quan đến quản lý booking cho nhân viên.
 */
@WebServlet("/staff/booking/*")
public class StaffBookingController extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(StaffBookingController.class.getName());
    private BookingDAO bookingDAO;
    private AccountDAO accountDAO;
    private Connection connection;
    private DBContext dbContext;

    @Override
    public void init() throws ServletException {
        try {
            dbContext = new DBContext();
            connection = dbContext.getConnection();
            bookingDAO = new BookingDAO(connection);
            accountDAO = new AccountDAO();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Không thể khởi tạo kết nối cơ sở dữ liệu", e);
            throw new ServletException("Không thể khởi tạo kết nối cơ sở dữ liệu", e);
        }
    }

    @Override
    public void destroy() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Lỗi khi đóng kết nối cơ sở dữ liệu", e);
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getPathInfo() != null ? request.getPathInfo() : "/list";
        switch (action) {
            case "/list":
                handleListBookings(request, response);
                break;
            case "/search":
                handleSearchBookings(request, response);
                break;
            case "/details":
                handleBookingDetails(request, response);
                break;
            case "/flight/bookings":
                handleFlightBookings(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/staff/booking/list");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getPathInfo() != null ? request.getPathInfo() : "/list";
        switch (action) {
            case "/update":
                handleUpdateBooking(request, response);
                break;
            case "/cancel":
                handleCancelBooking(request, response);
                break;
            case "/checkin":
                handlePerformCheckIn(request, response);
                break;
            case "/updatePassenger":
                handleUpdatePassenger(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/staff/booking/list");
                break;
        }
    }

    // Sửa handleListBookings
    private void handleListBookings(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Không dùng statusFilter nữa vì giờ là list flights
        List<Flight> flights = bookingDAO.getAllFlights();
        request.setAttribute("flights", flights);
        request.getRequestDispatcher("/WEB-INF/staff/flightList.jsp").forward(request, response);
    }

// Thêm method mới
    private void handleFlightBookings(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String flightIdStr = request.getParameter("flightId");
        String statusFilter = request.getParameter("statusFilter");
        // Xóa các thông báo cũ khỏi session
        request.getSession().removeAttribute("error");
        request.getSession().removeAttribute("success");
        if (flightIdStr == null) {
            request.setAttribute("error", "Vui lòng cung cấp flightId!");
            response.sendRedirect(request.getContextPath() + "/staff/booking/list");
            return;
        }
        int flightId = Integer.parseInt(flightIdStr);

        
        // Lấy thông tin chuyến bay để hiển thị tiêu đề
        Flight flight =  bookingDAO.getFlightDetails2(flightId);
        
        List<Booking> bookings = bookingDAO.getBookingsByFlightId(flightId, statusFilter);

        request.removeAttribute("error");
        request.setAttribute("bookings", bookings);
        request.setAttribute("flight", flight != null ? flight : new Flight());
        request.setAttribute("flightId", flightId); // Để dùng trong JSP (ví dụ filter form)
        
        request.getRequestDispatcher("/WEB-INF/staff/bookingList.jsp").forward(request, response);
    }

    private void handleSearchBookings(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String searchTerm = request.getParameter("searchTerm");
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            request.getSession().setAttribute("error", "Vui lòng nhập từ khóa tìm kiếm!");
            response.sendRedirect(request.getContextPath() + "/staff/booking/list");
            return;
        }
        List<Booking> bookings = bookingDAO.searchBookings(searchTerm);
        request.setAttribute("bookings", bookings);
        request.getRequestDispatcher("/WEB-INF/staff/bookingList.jsp").forward(request, response);
    }

    private void handleBookingDetails(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String bookingIdStr = request.getParameter("bookingId");
        if (bookingIdStr == null || bookingIdStr.trim().isEmpty()) {
            request.getSession().setAttribute("error", "Vui lòng cung cấp bookingId!");
            response.sendRedirect(request.getContextPath() + "/staff/booking/list");
            return;
        }

        try {
            int bookingId = Integer.parseInt(bookingIdStr);
            Booking booking = bookingDAO.getBookingDetails(bookingId);
            Passenger passenger = bookingDAO.getPassengerDetails(bookingId);
            Flight flight = bookingDAO.getFlightDetails(bookingId);
            List<BookingHistory> history = bookingDAO.getBookingHistory(bookingId);
            List<CheckIn> checkIns = bookingDAO.getCheckInDetails(bookingId);
            // Thêm danh sách ghế khả dụng
            List<Seat> availableSeats = bookingDAO.getAvailableSeats(booking.getFlightId());

            if (booking == null) {
                LOGGER.warning("Booking không tồn tại: bookingId=" + bookingId);
                request.getSession().setAttribute("error", "Booking không tồn tại hoặc đã bị xóa!");
                response.sendRedirect(request.getContextPath() + "/staff/booking/list");
                return;
            }

            booking.setUserFullName(passenger != null ? passenger.getFullName() : "N/A");

            request.getSession().removeAttribute("error");
            request.getSession().removeAttribute("success");

            request.setAttribute("booking", booking);
            request.setAttribute("passenger", passenger != null ? passenger : new Passenger());
            request.setAttribute("flight", flight != null ? flight : new Flight());
            request.setAttribute("history", history != null ? history : new ArrayList<>());
            request.setAttribute("checkIns", checkIns != null ? checkIns : new ArrayList<>());
            request.setAttribute("flightId", booking.getFlightId());
            request.setAttribute("availableSeats", availableSeats);
            LOGGER.info("Forwarding to bookingDetails.jsp with bookingId: " + bookingId + ", checkIns size: " + (checkIns != null ? checkIns.size() : 0));
            request.getRequestDispatcher("/WEB-INF/staff/bookingDetails.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "bookingId không hợp lệ: " + bookingIdStr, e);
            request.getSession().setAttribute("error", "bookingId không hợp lệ: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/staff/booking/list");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy chi tiết booking: " + e.getMessage(), e);
            request.getSession().setAttribute("error", "Lỗi hệ thống khi lấy chi tiết booking: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/staff/booking/list");
        }
    }

    private void handleUpdateBooking(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain; charset=UTF-8");
        try {
            HttpSession session = request.getSession();
            Integer userId = (Integer) session.getAttribute("userId");
            String role = (String) session.getAttribute("role");
            String fullName = (String) session.getAttribute("fullname");

            if (userId == null || userId <= 0 || role == null || fullName == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Thông tin nhân viên không hợp lệ, vui lòng đăng nhập lại.");
                return;
            }

            String bookingIdStr = request.getParameter("bookingId");
            String status = request.getParameter("status");

            // Kiểm tra null hoặc chuỗi rỗng
            if (bookingIdStr == null || bookingIdStr.trim().isEmpty() || status == null || status.trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Dữ liệu không hợp lệ: Thiếu mã booking hoặc trạng thái.");
                return;
            }

            int bookingId;
            try {
                bookingId = Integer.parseInt(bookingIdStr);
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Mã booking không hợp lệ: " + bookingIdStr);
                return;
            }

            // Kiểm tra trạng thái hợp lệ
            List<String> validStatuses = Arrays.asList("CONFIRMED", "CANCELLED", "COMPLETED", "PENDING");
            if (!validStatuses.contains(status)) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Trạng thái không hợp lệ: " + status);
                return;
            }

            // Kiểm tra trạng thái chuyến bay
            Flight flight = bookingDAO.getFlightDetails(bookingId);
            if (flight != null && ("CANCELLED".equals(flight.getStatus()) || "DELAYED".equals(flight.getStatus()))) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Không thể cập nhật booking vì chuyến bay bị hủy hoặc trì hoãn.");
                return;
            }

            LOGGER.info("Dữ liệu nhận được - bookingId: " + bookingId + ", status: " + status + ", staffId: " + userId);

            Booking booking = bookingDAO.getBookingDetails(bookingId);
            if (booking == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Booking không tồn tại.");
                return;
            }

            booking.setStatus(status);
            boolean success = bookingDAO.updateBooking(booking, userId);
            if (success) {
                BookingHistory history = new BookingHistory();
                history.setBookingId(bookingId);
                history.setAction("CẬP NHẬT");
                history.setDescription("Cập nhật trạng thái booking thành " + status);
                history.setActionTime(Timestamp.from(Instant.now()));
                history.setRole(role);
                history.setUserName(fullName);
                history.setUserId(userId);
                bookingDAO.addBookingHistory(history);
                response.getWriter().write("Cập nhật booking thành công!");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Cập nhật booking thất bại! Không tìm thấy booking hoặc lỗi cơ sở dữ liệu.");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Lỗi hệ thống khi cập nhật booking: " + e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Lỗi hệ thống: " + e.getMessage());
        }
    }

    private void handleCancelBooking(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            HttpSession session = request.getSession();
            Integer userId = (Integer) session.getAttribute("userId");
            String role = (String) session.getAttribute("role");
            String fullname = (String) session.getAttribute("fullname");

            if (userId == null || userId <= 0 || role == null || fullname == null) {
                throw new IllegalStateException("Thông tin nhân viên không hợp lệ, vui lòng đăng nhập lại.");
            }

            int bookingId = Integer.parseInt(request.getParameter("bookingId"));
            String reason = request.getParameter("reason");

            // Kiểm tra trạng thái chuyến bay
            Flight flight = bookingDAO.getFlightDetails(bookingId);
            if (flight != null && "CANCELLED".equals(flight.getStatus())) {
                throw new IllegalStateException("Không thể hủy booking vì chuyến bay đã bị hủy.");
            }

            LOGGER.info("Dữ liệu nhận được - bookingId: " + bookingId + ", reason: " + reason + ", userId: " + userId);

            if (bookingId <= 0) {
                throw new IllegalArgumentException("Dữ liệu không hợp lệ! bookingId: " + bookingId);
            }

            // Lấy thông tin booking để lấy seat_id
            Booking booking = bookingDAO.getBookingDetails(bookingId);
            if (booking == null) {
                throw new IllegalStateException("Booking không tồn tại hoặc đã bị xóa!");
            }

            if ("CANCELLED".equals(booking.getStatus())) {
                throw new IllegalStateException("Booking đã bị hủy trước đó!");
            }

            // Cập nhật trạng thái booking
            booking.setStatus("CANCELLED");
            booking.setStaffNote(reason);

            boolean success = bookingDAO.updateBooking(booking, userId);
            if (success) {
                // Cập nhật is_booked trong bảng Seat
                if (booking.getSeat() != null && booking.getSeat().getSeatId() > 0) {
                    bookingDAO.updateSeatBookingStatus(booking.getSeat().getSeatId(), false);
                } else {
                    LOGGER.warning("Không tìm thấy seat_id cho bookingId: " + bookingId);
                }

                // Ghi lịch sử hủy
                BookingHistory history = new BookingHistory();
                history.setBookingId(bookingId);
                history.setAction("HỦY");
                history.setDescription("Hủy booking: " + reason);
                history.setActionTime(Timestamp.from(Instant.now()));
                history.setRole(role);
                history.setUserName(fullname);
                history.setUserId(userId);
                bookingDAO.addBookingHistory(history);

                request.getSession().setAttribute("success", "Hủy booking thành công!");
            } else {
                request.getSession().setAttribute("error", "Hủy booking thất bại! Vui lòng kiểm tra log.");
            }
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Lỗi định dạng số: " + e.getMessage(), e);
            request.getSession().setAttribute("error", "Dữ liệu không hợp lệ: " + e.getMessage());
        } catch (IllegalArgumentException | IllegalStateException e) {
            LOGGER.log(Level.SEVERE, "Lỗi dữ liệu không hợp lệ: " + e.getMessage(), e);
            request.getSession().setAttribute("error", "Dữ liệu không hợp lệ: " + e.getMessage());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi hủy booking: " + e.getMessage(), e);
            request.getSession().setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/staff/booking/flight/bookings?flightId=" + bookingDAO.getFlightDetails(Integer.parseInt(request.getParameter("bookingId"))).getFlightId());
    }

    private void handlePerformCheckIn(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain; charset=UTF-8");
        try {
            HttpSession session = request.getSession();
            Integer userId = (Integer) session.getAttribute("userId");
            String role = (String) session.getAttribute("role");
            String fullname = (String) session.getAttribute("fullname");

            if (userId == null || userId <= 0 || role == null || fullname == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Thông tin nhân viên không hợp lệ, vui lòng đăng nhập lại.");
                return;
            }

            int bookingId = Integer.parseInt(request.getParameter("bookingId"));
            int passengerId = Integer.parseInt(request.getParameter("passengerId"));
            int flightId = Integer.parseInt(request.getParameter("flightId"));
            String status = request.getParameter("checkInStatus");

            // Kiểm tra trạng thái không undefined/null
            if (status == null || status.trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Trạng thái check-in không được để trống!");
                return;
            }

            // Kiểm tra trạng thái chuyến bay
            Flight flight = bookingDAO.getFlightDetails(bookingId);
            if (flight == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Không tìm thấy chuyến bay cho booking này.");
                return;
            }
            if ("CANCELLED".equals(flight.getStatus()) || "DELAYED".equals(flight.getStatus())) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Không thể thực hiện check-in vì chuyến bay bị hủy hoặc trì hoãn.");
                return;
            }

            // Kiểm tra trạng thái check-in hợp lệ
            List<String> validCheckinStatuses = Arrays.asList("NOT CHECKED-IN","CHECKED-IN", "BOARDED");
            if (!validCheckinStatuses.contains(status)) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Trạng thái check-in không hợp lệ: " + status);
                return;
            }

            LOGGER.info("Dữ liệu nhận được - bookingId: " + bookingId + ", passengerId: " + passengerId + ", flightId: " + flightId
                    + ", status: " + status + ", staffId: " + userId);

            if (bookingId <= 0 || passengerId <= 0 || flightId <= 0) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Dữ liệu không hợp lệ! bookingId: " + bookingId + ", passengerId: " + passengerId
                        + ", flightId: " + flightId);
                return;
            }

            CheckIn checkIn = new CheckIn();
            checkIn.setPassengerId(passengerId);
            checkIn.setBookingId(bookingId);
            checkIn.setFlightId(flightId);
            checkIn.setCheckinTime(Timestamp.from(Instant.now()));
            checkIn.setStatus(status);
            checkIn.setUserId(userId);

            boolean success = bookingDAO.addCheckIn(checkIn);
            if (success) {
                BookingHistory history = new BookingHistory();
                history.setBookingId(bookingId);
                history.setAction(status);
                history.setDescription("Cập nhật trạng thái check-in thành " + status + " cho hành khách ID: " + passengerId);
                history.setActionTime(Timestamp.from(Instant.now()));
                history.setRole(role);
                history.setUserName(fullname);
                history.setUserId(userId);
                bookingDAO.addBookingHistory(history);
                response.getWriter().write("Check-in thành công!");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Check-in thất bại! Không tìm thấy hành khách hoặc lỗi cơ sở dữ liệu.");
            }
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Lỗi định dạng số: " + e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Dữ liệu không hợp lệ: " + e.getMessage());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi thực hiện check-in: " + e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Lỗi hệ thống: " + e.getMessage());
        }
    }

    private void handleUpdatePassenger(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain; charset=UTF-8");
        try {
            HttpSession session = request.getSession();
            Integer userId = (Integer) session.getAttribute("userId");
            String role = (String) session.getAttribute("role");
            String fullName = (String) session.getAttribute("fullname");

            if (userId == null || userId <= 0 || role == null || fullName == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Thông tin nhân viên không hợp lệ, vui lòng đăng nhập lại.");
                return;
            }

            int passengerId = Integer.parseInt(request.getParameter("passengerId"));
            String fullNamePassenger = request.getParameter("fullName");
            String passportNumber = request.getParameter("passportNumber");
            String dobStr = request.getParameter("dob");
            String gender = request.getParameter("gender");
            String phoneNumber = request.getParameter("phoneNumber");
            String email = request.getParameter("email");
            String country = request.getParameter("country");
            String address = request.getParameter("address");
            int bookingId = Integer.parseInt(request.getParameter("bookingId"));

            LOGGER.info("Dữ liệu nhận được - passengerId: " + passengerId + ", fullName: " + fullNamePassenger + ", dobStr: " + dobStr
                    + ", bookingId: " + bookingId + ", userId: " + userId);

            if (passengerId <= 0 || fullNamePassenger == null || passportNumber == null || dobStr == null || gender == null
                    || phoneNumber == null || email == null || country == null || address == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Dữ liệu không hợp lệ! Vui lòng kiểm tra thông tin hành khách.");
                return;
            }

            Date dob = null;
            java.sql.Date sqlDob = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            dob = sdf.parse(dobStr);
            sqlDob = new java.sql.Date(dob.getTime());

            Passenger passenger = new Passenger();
            passenger.setPassengerId(passengerId);
            passenger.setFullName(fullNamePassenger);
            passenger.setPassportNumber(passportNumber);
            passenger.setDob(sqlDob);
            passenger.setGender(gender);
            passenger.setPhoneNumber(phoneNumber);
            passenger.setEmail(email);
            passenger.setCountry(country);
            passenger.setAddress(address);

            boolean success = bookingDAO.updatePassenger(passenger);
            if (success) {
                BookingHistory history = new BookingHistory();
                history.setBookingId(bookingId);
                history.setAction("CẬP NHẬT HÀNH KHÁCH");
                history.setDescription("Cập nhật thông tin hành khách cho " + fullNamePassenger);
                history.setActionTime(Timestamp.from(Instant.now()));
                history.setRole(role);
                history.setUserName(fullName);
                history.setUserId(userId);
                bookingDAO.addBookingHistory(history);
                response.getWriter().write("Cập nhật thông tin hành khách thành công!");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Cập nhật thông tin hành khách thất bại! Không tìm thấy hành khách hoặc lỗi cơ sở dữ liệu.");
            }
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Lỗi định dạng số: " + e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Dữ liệu không hợp lệ: " + e.getMessage());
        } catch (java.text.ParseException e) {
            LOGGER.log(Level.SEVERE, "Lỗi định dạng ngày: " + e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Ngày sinh không hợp lệ: " + e.getMessage());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi cập nhật hành khách: " + e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Lỗi hệ thống: " + e.getMessage());
        }
    }
}
