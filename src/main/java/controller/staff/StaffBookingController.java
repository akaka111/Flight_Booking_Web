package controller.staff;

import DAO.Admin.BookingDAO;
import model.Booking;
import model.BookingHistory;
import model.CheckIn;
import model.Passenger;
import utils.DBContext;
import model.Flight;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servlet xử lý các yêu cầu liên quan đến quản lý booking cho nhân viên.
 */
@WebServlet("/staff/booking/*")
public class StaffBookingController extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(StaffBookingController.class.getName());
    private BookingDAO bookingDAO;
    private Connection connection;
    private DBContext dbContext;

    @Override
    public void init() throws ServletException {
        try {
            dbContext = new DBContext();
            connection = dbContext.getConnection();
            bookingDAO = new BookingDAO(connection);
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

    private void handleListBookings(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String statusFilter = request.getParameter("statusFilter");
        List<Booking> bookings = bookingDAO.getAllBookings(statusFilter);
        request.setAttribute("bookings", bookings);
        request.getRequestDispatcher("/WEB-INF/staff/bookingList.jsp").forward(request, response);
    }

    private void handleSearchBookings(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String searchTerm = request.getParameter("searchTerm");
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/staff/booking/list");
            return;
        }
        List<Booking> bookings = bookingDAO.searchBookings(searchTerm);
        request.setAttribute("bookings", bookings);
        request.getRequestDispatcher("/WEB-INF/staff/bookingList.jsp").forward(request, response);
    }

    private void handleBookingDetails(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String bookingIdStr = request.getParameter("bookingId");
        if (bookingIdStr == null) {
            response.sendRedirect(request.getContextPath() + "/staff/booking/list");
            return;
        }
        int bookingId = Integer.parseInt(bookingIdStr);
        Booking booking = bookingDAO.getBookingDetails(bookingId);
        Passenger passenger = bookingDAO.getPassengerDetails(bookingId);
        Flight flight = bookingDAO.getFlightDetails(bookingId);
        List<BookingHistory> history = bookingDAO.getBookingHistory(bookingId);

        if (booking == null) {
            request.getSession().setAttribute("error", "Booking không tồn tại!");
            response.sendRedirect(request.getContextPath() + "/staff/booking/list");
            return;
        }

        request.getSession().removeAttribute("error");
        request.getSession().removeAttribute("success");

        request.setAttribute("booking", booking);
        request.setAttribute("passenger", passenger);
        request.setAttribute("flight", flight);
        request.setAttribute("history", history);
        request.getRequestDispatcher("/WEB-INF/staff/bookingDetails.jsp").forward(request, response);
    }

    private void handleUpdateBooking(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int bookingId = Integer.parseInt(request.getParameter("bookingId"));
            String status = request.getParameter("status");
            String seatClass = request.getParameter("seatClass");
            double totalPrice = Double.parseDouble(request.getParameter("totalPrice"));
            String staffNote = request.getParameter("staffNote");
            String checkinStatus = request.getParameter("checkinStatus");
            int staffId = getStaffIdFromCookie(request);
            String staffRole = getStaffRoleFromCookie(request); // Giả định lấy vai trò từ cookie
            String staffName = getStaffNameFromCookie(request); // Giả định lấy tên từ cookie

            LOGGER.info("Dữ liệu nhận được - bookingId: " + bookingId + ", status: " + status + ", seatClass: " + seatClass +
                       ", totalPrice: " + totalPrice + ", staffId: " + staffId);

            if (bookingId <= 0 || status == null || seatClass == null || totalPrice <= 0 || staffId <= 0) {
                throw new IllegalArgumentException("Dữ liệu không hợp lệ! bookingId: " + bookingId + ", totalPrice: " + totalPrice + ", staffId: " + staffId);
            }

            Booking booking = new Booking();
            booking.setBookingId(bookingId);
            booking.setStatus(status);
            booking.setSeatClass(seatClass);
            booking.setTotalPrice(totalPrice);
            booking.setStaffNote(staffNote);
            booking.setCheckinStatus(checkinStatus);

            boolean success = bookingDAO.updateBooking(booking, staffId);
            if (success) {
                BookingHistory history = new BookingHistory();
                history.setBookingId(bookingId);
                history.setAction("CẬP NHẬT");
                history.setDescription("Cập nhật trạng thái booking thành " + status + ", hạng ghế thành " + seatClass + ", ghi chú: " + staffNote);
                history.setActionTime(LocalDateTime.now());
                history.setRole(staffRole); // Thêm vai trò
                history.setStaffName(staffName); // Thêm tên nhân viên
                bookingDAO.addBookingHistory(history);
                request.getSession().setAttribute("success", "Cập nhật booking thành công!");
            } else {
                request.getSession().setAttribute("error", "Cập nhật booking thất bại! Vui lòng kiểm tra log.");
            }
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Lỗi định dạng số: " + e.getMessage(), e);
            request.getSession().setAttribute("error", "Dữ liệu không hợp lệ: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.SEVERE, "Lỗi dữ liệu không hợp lệ: " + e.getMessage(), e);
            request.getSession().setAttribute("error", "Dữ liệu không hợp lệ: " + e.getMessage());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Lỗi hệ thống khi cập nhật booking: " + e.getMessage(), e);
            request.getSession().setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
        }
        request.getSession().removeAttribute("error");
        request.getSession().removeAttribute("success");
        response.sendRedirect(request.getContextPath() + "/staff/booking/details?bookingId=" + request.getParameter("bookingId"));
    }

    private void handleCancelBooking(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int bookingId = Integer.parseInt(request.getParameter("bookingId"));
            String reason = request.getParameter("reason");
            int staffId = getStaffIdFromCookie(request);
            String staffRole = getStaffRoleFromCookie(request); // Giả định lấy vai trò từ cookie
            String staffName = getStaffNameFromCookie(request); // Giả định lấy tên từ cookie

            LOGGER.info("Dữ liệu nhận được - bookingId: " + bookingId + ", reason: " + reason + ", staffId: " + staffId);

            if (bookingId <= 0 || staffId <= 0) {
                throw new IllegalArgumentException("Dữ liệu không hợp lệ! bookingId: " + bookingId + ", staffId: " + staffId);
            }

            Booking booking = new Booking();
            booking.setBookingId(bookingId);
            booking.setStatus("CANCELLED");
            booking.setStaffNote(reason);
            booking.setCheckinStatus("NOT CHECKED-IN");

            boolean success = bookingDAO.updateBooking(booking, staffId);
            if (success) {
                BookingHistory history = new BookingHistory();
                history.setBookingId(bookingId);
                history.setAction("HỦY");
                history.setDescription("Hủy booking: " + reason);
                history.setActionTime(LocalDateTime.now());
                history.setRole(staffRole); // Thêm vai trò
                history.setStaffName(staffName); // Thêm tên nhân viên
                bookingDAO.addBookingHistory(history);
                request.getSession().setAttribute("success", "Hủy booking thành công!");
            } else {
                request.getSession().setAttribute("error", "Hủy booking thất bại! Vui lòng kiểm tra log.");
            }
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Lỗi định dạng số: " + e.getMessage(), e);
            request.getSession().setAttribute("error", "Dữ liệu không hợp lệ: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.SEVERE, "Lỗi dữ liệu không hợp lệ: " + e.getMessage(), e);
            request.getSession().setAttribute("error", "Dữ liệu không hợp lệ: " + e.getMessage());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi hủy booking: " + e.getMessage(), e);
            request.getSession().setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
        }
        request.getSession().removeAttribute("error");
        request.getSession().setAttribute("success", "Hủy booking thành công!");
        response.sendRedirect(request.getContextPath() + "/staff/booking/list");
    }

    private void handlePerformCheckIn(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int bookingId = Integer.parseInt(request.getParameter("bookingId"));
            int passengerId = Integer.parseInt(request.getParameter("passengerId"));
            int flightId = Integer.parseInt(request.getParameter("flightId"));
            String status = request.getParameter("checkinStatus");
            int staffId = getStaffIdFromCookie(request);
            String staffRole = getStaffRoleFromCookie(request); // Giả định lấy vai trò từ cookie
            String staffName = getStaffNameFromCookie(request); // Giả định lấy tên từ cookie

            LOGGER.info("Dữ liệu nhận được - bookingId: " + bookingId + ", passengerId: " + passengerId + ", flightId: " + flightId +
                       ", status: " + status + ", staffId: " + staffId);

            if (bookingId <= 0 || passengerId <= 0 || flightId <= 0 || staffId <= 0 || !status.equals("CHECKED-IN") && !status.equals("BOARDED")) {
                throw new IllegalArgumentException("Dữ liệu không hợp lệ! bookingId: " + bookingId + ", passengerId: " + passengerId + 
                                                  ", flightId: " + flightId + ", staffId: " + staffId);
            }

            CheckIn checkIn = new CheckIn();
            checkIn.setPassengerId(passengerId);
            checkIn.setBookingId(bookingId);
            checkIn.setFlightId(flightId);
            checkIn.setCheckinTime(LocalDateTime.now());
            checkIn.setStatus(status);
            checkIn.setStaffId(staffId);

            boolean success = bookingDAO.addCheckIn(checkIn);
            if (success) {
                Booking booking = new Booking();
                booking.setBookingId(bookingId);
                booking.setCheckinStatus(status);
                booking.setLastUpdatedBy(staffId);
                booking.setLastUpdatedAt(Timestamp.from(Instant.now()));
                bookingDAO.updateBooking(booking, staffId);

                BookingHistory history = new BookingHistory();
                history.setBookingId(bookingId);
                history.setAction(status);
                history.setDescription("Cập nhật trạng thái check-in thành " + status);
                history.setActionTime(LocalDateTime.now());
                history.setRole(staffRole); // Thêm vai trò
                history.setStaffName(staffName); // Thêm tên nhân viên
                bookingDAO.addBookingHistory(history);
                request.getSession().setAttribute("success", "Check-in thành công!");
            } else {
                request.getSession().setAttribute("error", "Check-in thất bại! Vui lòng kiểm tra log.");
            }
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Lỗi định dạng số: " + e.getMessage(), e);
            request.getSession().setAttribute("error", "Dữ liệu không hợp lệ: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.SEVERE, "Lỗi dữ liệu không hợp lệ: " + e.getMessage(), e);
            request.getSession().setAttribute("error", "Dữ liệu không hợp lệ: " + e.getMessage());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi thực hiện check-in: " + e.getMessage(), e);
            request.getSession().setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
        }
        request.getSession().removeAttribute("error");
        request.getSession().removeAttribute("success");
        response.sendRedirect(request.getContextPath() + "/staff/booking/details?bookingId=" + request.getParameter("bookingId"));
    }

    private void handleUpdatePassenger(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int passengerId = Integer.parseInt(request.getParameter("passengerId"));
            String fullName = request.getParameter("fullName");
            String passportNumber = request.getParameter("passportNumber");
            String dobStr = request.getParameter("dob");
            String gender = request.getParameter("gender");
            String phoneNumber = request.getParameter("phoneNumber");
            String email = request.getParameter("email");
            String country = request.getParameter("country");
            String address = request.getParameter("address");
            int staffId = getStaffIdFromCookie(request);
            int bookingId = Integer.parseInt(request.getParameter("bookingId"));
            String staffRole = getStaffRoleFromCookie(request); // Giả định lấy vai trò từ cookie
            String staffName = getStaffNameFromCookie(request); // Giả định lấy tên từ cookie

            LOGGER.info("Dữ liệu nhận được - passengerId: " + passengerId + ", fullName: " + fullName + ", dobStr: " + dobStr +
                       ", bookingId: " + bookingId + ", staffId: " + staffId);

            if (passengerId <= 0 || fullName == null || passportNumber == null || dobStr == null || gender == null ||
                phoneNumber == null || email == null || country == null || address == null || staffId <= 0) {
                throw new IllegalArgumentException("Dữ liệu không hợp lệ! passengerId: " + passengerId + ", staffId: " + staffId);
            }

            Date dob = null;
            java.sql.Date sqlDob = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            dob = sdf.parse(dobStr);
            sqlDob = new java.sql.Date(dob.getTime());

            Passenger passenger = new Passenger();
            passenger.setPassengerId(passengerId);
            passenger.setFullName(fullName);
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
                history.setDescription("Cập nhật thông tin hành khách cho " + fullName);
                history.setActionTime(LocalDateTime.now());
                history.setRole(staffRole); // Thêm vai trò
                history.setStaffName(staffName); // Thêm tên nhân viên
                bookingDAO.addBookingHistory(history);
                request.getSession().setAttribute("success", "Cập nhật thông tin hành khách thành công!");
            } else {
                request.getSession().setAttribute("error", "Cập nhật thông tin hành khách thất bại! Vui lòng kiểm tra log.");
            }
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Lỗi định dạng số: " + e.getMessage(), e);
            request.getSession().setAttribute("error", "Dữ liệu không hợp lệ: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.SEVERE, "Lỗi dữ liệu không hợp lệ: " + e.getMessage(), e);
            request.getSession().setAttribute("error", "Dữ liệu không hợp lệ: " + e.getMessage());
        } catch (java.text.ParseException e) {
            LOGGER.log(Level.SEVERE, "Lỗi định dạng ngày: " + e.getMessage(), e);
            request.getSession().setAttribute("error", "Ngày sinh không hợp lệ: " + e.getMessage());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi cập nhật hành khách: " + e.getMessage(), e);
            request.getSession().setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
        }
        request.getSession().removeAttribute("error");
        request.getSession().removeAttribute("success");
        response.sendRedirect(request.getContextPath() + "/staff/booking/details?bookingId=" + request.getParameter("bookingId"));
    }

    private int getStaffIdFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("staffId".equals(cookie.getName())) {
                    try {
                        int staffId = Integer.parseInt(cookie.getValue());
                        LOGGER.info("Staff ID from cookie: " + staffId);
                        return staffId;
                    } catch (NumberFormatException e) {
                        LOGGER.log(Level.SEVERE, "Lỗi định dạng staffId từ cookie: " + e.getMessage(), e);
                    }
                }
            }
        }
        LOGGER.warning("Không tìm thấy staffId trong cookie, trả về -1");
        return -1;
    }

    // Giả định phương thức lấy vai trò từ cookie (cần triển khai theo logic thực tế)
    private String getStaffRoleFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("staffRole".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        LOGGER.warning("Không tìm thấy staffRole trong cookie, trả về mặc định");
        return "STAFF"; // Giá trị mặc định, cần thay bằng logic thực tế
    }

    // Giả định phương thức lấy tên nhân viên từ cookie (cần triển khai theo logic thực tế)
    private String getStaffNameFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("staffName".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        LOGGER.warning("Không tìm thấy staffName trong cookie, trả về mặc định");
        return "Unknown Staff"; // Giá trị mặc định, cần thay bằng logic thực tế
    }
}