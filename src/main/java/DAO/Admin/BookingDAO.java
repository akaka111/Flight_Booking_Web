package DAO.Admin;

import model.Booking;
import model.BookingHistory;
import model.CheckIn;
import model.Passenger;
import model.Flight;
import utils.DBContext;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author LienXuanThinh - CE182117
 */
public class BookingDAO {

    private Connection conn;
    private DBContext dbContext;
    private static final Logger LOGGER = Logger.getLogger(BookingDAO.class.getName());

    // Constructor giữ nguyên để hỗ trợ code hiện tại
    public BookingDAO(Connection conn) {
        this.conn = conn;
        this.dbContext = new DBContext();
    }

    public BookingDAO() {
        try {
            this.conn = new DBContext().getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Phương thức này thêm một bản ghi Booking mới vào cơ sở dữ liệu và trả về
     * booking_id (khóa chính tự động tăng) của bản ghi vừa được tạo.
     *
     * @param booking Đối tượng Booking chứa thông tin cần lưu.
     * @return ID của booking vừa được tạo, hoặc -1 nếu có lỗi.
     */
    public int createBooking(Booking booking) {
        int generatedId = -1;
        String sql = "INSERT INTO Booking (user_id, flight_id, booking_time, seat_class, total_price, status, booking_code) VALUES (?, ?, ?, ?, ?, ?, ?)";

        // Sinh mã đặt chỗ ngẫu nhiên, ví dụ: BK-20250730-XXXX
        String bookingCode = generateBookingCode();

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, booking.getUserId());
            ps.setInt(2, booking.getFlightId());
            ps.setTimestamp(3, booking.getBookingDate());
            ps.setString(4, booking.getSeatClass());
            ps.setDouble(5, booking.getTotalPrice());
            ps.setString(6, booking.getStatus());
            ps.setString(7, bookingCode);  // Thêm booking_code

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedId = rs.getInt(1);
                        System.out.println("==> Booking ID được tạo: " + generatedId);
                        System.out.println("==> Mã đặt chỗ: " + bookingCode);
                    }
                }
            } else {
                System.err.println("==> Không có dòng nào được thêm vào bảng Booking.");
            }

        } catch (SQLException e) {
            System.err.println("==> SQLException khi tạo booking: " + e.getMessage());
            e.printStackTrace();
        }

        return generatedId;
    }

    // Trong file BookingDAO.java
// Phương thức để lấy một booking từ DB bằng ID
    public Booking getBookingById(int bookingId) {
        Booking booking = null;
        String sql = "SELECT * FROM dbo.Booking WHERE booking_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    booking = new Booking();
                    booking.setBookingId(rs.getInt("booking_id"));
                    booking.setUserId(rs.getInt("user_id"));
                    // ... lấy các cột khác ...
                    booking.setSeatClass(rs.getString("seat_class"));
                    booking.setTotalPrice(rs.getDouble("total_price"));
                    booking.setStatus(rs.getString("CONFIRMED"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return booking;
    }

// Phương thức để cập nhật trạng thái của một booking
    public boolean updateBookingStatus(int bookingId, String newStatus) {
        String sql = "UPDATE dbo.Booking SET status = ? WHERE booking_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, bookingId);

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0; // Trả về true nếu có ít nhất 1 dòng được cập nhật
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String generateBookingCode() {
        String prefix = "BK";
        String datePart = new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date());
        String randomDigits = String.valueOf((int) (Math.random() * 9000 + 1000)); // 4 chữ số ngẫu nhiên
        return prefix + "-" + datePart + "-" + randomDigits;
    }

    public int insertAndGetId(Booking booking) {
        int generatedId = -1;
        String sql = "INSERT INTO Booking (user_id, flight_id, booking_time, seat_class, total_price, status, booking_code) VALUES (?, ?, ?, ?, ?, ?, ?)";

        // Sử dụng try-with-resources để đảm bảo kết nối luôn được đóng
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            System.out.println("==> [BookingDAO] Đang chuẩn bị thực thi câu lệnh INSERT...");

            ps.setInt(1, booking.getUserId());
            ps.setInt(2, booking.getFlightId());
            ps.setTimestamp(3, new java.sql.Timestamp(booking.getBookingDate().getTime()));
            ps.setString(4, booking.getSeatClass());
            ps.setDouble(5, booking.getTotalPrice());
            ps.setString(6, "CONFIRMED"); // Ví dụ, nếu 'CONFIRMED' được cho phép
            ps.setString(7, generateBookingCode());

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedId = rs.getInt(1);
                    }
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
        return generatedId;
    }

    public void updateBookingAmount(Booking booking) {
        String sql = "UPDATE Booking SET total_amount = ? WHERE booking_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDouble(1, booking.getTotalPrice()); // đúng kiểu double
            ps.setInt(2, booking.getBookingId());     // đúng getter
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Các phương thức mới sử dụng DBContext
    private static final String SELECT_ALL_BOOKINGS
            = "SELECT b.booking_id, b.user_id, b.flight_id, b.booking_time, b.status, b.seat_class, b.total_price, b.staff_note, b.last_updated_by, b.last_updated_at, b.checkin_status, "
            + "p.passenger_id, p.full_name, p.passport_number, p.dob, p.gender, p.phone_number, p.email, p.country, p.address "
            + "FROM Booking b "
            + "LEFT JOIN Passenger p ON b.booking_id = p.booking_id";
    private static final String SEARCH_BOOKINGS
            = "SELECT b.booking_id, b.user_id, b.flight_id, b.booking_time, b.status, b.seat_class, b.total_price, b.staff_note, b.last_updated_by, b.last_updated_at, b.checkin_status, "
            + "p.passenger_id, p.full_name, p.passport_number, p.dob, p.gender, p.phone_number, p.email, p.country, p.address "
            + "FROM Booking b "
            + "LEFT JOIN Passenger p ON b.booking_id = p.booking_id "
            + "WHERE b.booking_id LIKE ? OR p.full_name LIKE ? OR p.passport_number LIKE ?";
    private static final String SELECT_BOOKING_DETAILS
            = "SELECT b.booking_id, b.user_id, b.flight_id, b.booking_time, b.status, b.seat_class, b.total_price, b.staff_note, b.last_updated_by, b.last_updated_at, b.checkin_status, "
            + "p.passenger_id, p.full_name, p.passport_number, p.dob, p.gender, p.phone_number, p.email, p.country, p.address, "
            + "f.flight_id, f.airline_id, f.flight_number, f.route_from, f.route_to, f.departure_time, f.arrival_time, f.aircraft, f.status "
            + "FROM Booking b "
            + "LEFT JOIN Passenger p ON b.booking_id = p.booking_id "
            + "LEFT JOIN Flight f ON b.flight_id = f.flight_id "
            + "WHERE b.booking_id = ?";
    private static final String UPDATE_BOOKING
            = "UPDATE Booking SET status = ?, seat_class = ?, total_price = ?, staff_note = ?, last_updated_by = ?, last_updated_at = ?, checkin_status = ? WHERE booking_id = ?";
    private static final String UPDATE_PASSENGER
            = "UPDATE Passenger SET full_name = ?, passport_number = ?, dob = ?, gender = ?, phone_number = ?, email = ?, country = ?, address = ? WHERE passenger_id = ?";
    private static final String INSERT_BOOKING_HISTORY
            = "INSERT INTO BookingHistory (booking_id, staff_id, action, description, action_time) VALUES (?, ?, ?, ?, ?)";
    private static final String INSERT_CHECKIN
            = "INSERT INTO CheckIn (passenger_id, booking_id, flight_id, checkin_time, status, staff_id) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SELECT_BOOKING_HISTORY
            = "SELECT * FROM BookingHistory WHERE booking_id = ?";

    public List<Booking> getAllBookings(String statusFilter) {
        List<Booking> bookings = new ArrayList<>();
        String query = SELECT_ALL_BOOKINGS;
        Object[] params = null;
        if (statusFilter != null && !statusFilter.isEmpty()) {
            query += " WHERE b.status = ? OR b.checkin_status = ?";
            params = new Object[]{statusFilter, statusFilter};
        }

        ResultSet rs = null;
        try {
            rs = dbContext.execSelectQuery(query, params);
            while (rs.next()) {
                Booking booking = new Booking();
                booking.setBookingId(rs.getInt("booking_id"));
                booking.setUserId(rs.getInt("user_id"));
                booking.setFlightId(rs.getInt("flight_id"));
                booking.setBookingDate(new Timestamp(rs.getTimestamp("booking_time").getTime()));
                booking.setStatus(rs.getString("status"));
                booking.setSeatClass(rs.getString("seat_class"));
                booking.setTotalPrice(rs.getBigDecimal("total_price").doubleValue());
                booking.setStaffNote(rs.getString("staff_note"));
                booking.setLastUpdatedBy(rs.getInt("last_updated_by"));
                booking.setLastUpdatedAt(rs.getTimestamp("last_updated_at") != null
                        ? rs.getTimestamp("last_updated_at").toLocalDateTime() : null);
                booking.setCheckinStatus(rs.getString("checkin_status"));
                LOGGER.info("Booking ID: " + rs.getInt("booking_id") + ", Full Name: " + rs.getString("full_name"));
                bookings.add(booking);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy danh sách booking", e);
        } finally {
            DBContext.closeSelectResources(rs, null, null);
        }
        return bookings;
    }

    public List<Booking> searchBookings(String searchTerm) {
        List<Booking> bookings = new ArrayList<>();
        String searchPattern = "%" + searchTerm + "%";
        Object[] params = new Object[]{searchPattern, searchPattern, searchPattern};

        ResultSet rs = null;
        try {
            rs = dbContext.execSelectQuery(SEARCH_BOOKINGS, params);
            while (rs.next()) {
                Booking booking = new Booking();
                booking.setBookingId(rs.getInt("booking_id"));
                booking.setUserId(rs.getInt("user_id"));
                booking.setFlightId(rs.getInt("flight_id"));
                booking.setBookingDate(new Timestamp(rs.getTimestamp("booking_time").getTime()));
                booking.setStatus(rs.getString("status"));
                booking.setSeatClass(rs.getString("seat_class"));
                booking.setTotalPrice(rs.getBigDecimal("total_price").doubleValue());
                booking.setStaffNote(rs.getString("staff_note"));
                booking.setLastUpdatedBy(rs.getInt("last_updated_by"));
                booking.setLastUpdatedAt(rs.getTimestamp("last_updated_at") != null
                        ? rs.getTimestamp("last_updated_at").toLocalDateTime() : null);
                booking.setCheckinStatus(rs.getString("checkin_status"));
                bookings.add(booking);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi tìm kiếm booking", e);
        } finally {
            DBContext.closeSelectResources(rs, null, null);
        }
        return bookings;
    }

    public Booking getBookingDetails(int bookingId) {
        Booking booking = null;
        ResultSet rs = null;
        try {
            rs = dbContext.execSelectQuery(SELECT_BOOKING_DETAILS, new Object[]{bookingId});
            if (rs.next()) {
                booking = new Booking();
                booking.setBookingId(rs.getInt("booking_id"));
                booking.setUserId(rs.getInt("user_id"));
                booking.setFlightId(rs.getInt("flight_id"));
                booking.setBookingDate(new Timestamp(rs.getTimestamp("booking_time").getTime()));
                booking.setStatus(rs.getString("status"));
                booking.setSeatClass(rs.getString("seat_class"));
                booking.setTotalPrice(rs.getBigDecimal("total_price").doubleValue());
                booking.setStaffNote(rs.getString("staff_note"));
                booking.setLastUpdatedBy(rs.getInt("last_updated_by"));
                booking.setLastUpdatedAt(rs.getTimestamp("last_updated_at") != null
                        ? rs.getTimestamp("last_updated_at").toLocalDateTime() : null);
                booking.setCheckinStatus(rs.getString("checkin_status"));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy chi tiết booking", e);
        } finally {
            DBContext.closeSelectResources(rs, null, null);
        }
        return booking;
    }

    public Passenger getPassengerDetails(int bookingId) {
        Passenger passenger = null;
        ResultSet rs = null;
        try {
            rs = dbContext.execSelectQuery(SELECT_BOOKING_DETAILS, new Object[]{bookingId});
            if (rs.next()) {
                passenger = new Passenger();
                passenger.setPassengerId(rs.getInt("passenger_id"));
                passenger.setFullName(rs.getString("full_name"));
                passenger.setPassportNumber(rs.getString("passport_number"));
                passenger.setDob(rs.getDate("dob"));
                passenger.setGender(rs.getString("gender"));
                passenger.setPhoneNumber(rs.getString("phone_number"));
                passenger.setEmail(rs.getString("email"));
                passenger.setCountry(rs.getString("country"));
                passenger.setAddress(rs.getString("address"));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy thông tin hành khách", e);
        } finally {
            DBContext.closeSelectResources(rs, null, null);
        }
        return passenger;
    }

    public Flight getFlightDetails(int bookingId) {
        Flight flight = null;
        ResultSet rs = null;
        try {
            rs = dbContext.execSelectQuery(SELECT_BOOKING_DETAILS, new Object[]{bookingId});
            if (rs.next()) {
                flight = new Flight();
                flight.setFlightId(rs.getInt("flight_id"));
                flight.setAirlineId(rs.getInt("airline_id"));
                flight.setFlightNumber(rs.getString("flight_number"));
                flight.setRouteFrom(rs.getString("route_from"));
                flight.setRouteTo(rs.getString("route_to"));
                flight.setDepartureTime(new Timestamp(rs.getTimestamp("departure_time").getTime()));
                flight.setArrivalTime(new Timestamp(rs.getTimestamp("arrival_time").getTime()));
                flight.setAircraft(rs.getString("aircraft"));
                flight.setStatus(rs.getString("status"));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy chi tiết chuyến bay", e);
        } finally {
            DBContext.closeSelectResources(rs, null, null);
        }
        return flight;
    }

    public boolean updateBooking(Booking booking, int staffId) {
        Object[] params = new Object[]{
            booking.getStatus(),
            booking.getSeatClass(),
            new java.math.BigDecimal(booking.getTotalPrice()),
            booking.getStaffNote(),
            staffId,
            new Timestamp(System.currentTimeMillis()),
            booking.getCheckinStatus(),
            booking.getBookingId()
        };
        try {
            int affectedRows = dbContext.execQuery(UPDATE_BOOKING, params);
            if (affectedRows > 0) {
                LOGGER.info("Cập nhật booking ID " + booking.getBookingId() + " thành công.");
                return true;
            } else {
                LOGGER.warning("Không có dòng nào được cập nhật cho booking ID " + booking.getBookingId());
                return false;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi cập nhật booking ID " + booking.getBookingId() + ": " + e.getMessage(), e);
            return false;
        }
    }

    public boolean updatePassenger(Passenger passenger) {
        Object[] params = new Object[]{
            passenger.getFullName(),
            passenger.getPassportNumber(),
            passenger.getDob(),
            passenger.getGender(),
            passenger.getPhoneNumber(),
            passenger.getEmail(),
            passenger.getCountry(),
            passenger.getAddress(),
            passenger.getPassengerId()
        };
        try {
            int affectedRows = dbContext.execQuery(UPDATE_PASSENGER, params);
            if (affectedRows > 0) {
                LOGGER.info("Cập nhật hành khách ID " + passenger.getPassengerId() + " thành công.");
                return true;
            } else {
                LOGGER.warning("Không có dòng nào được cập nhật cho hành khách ID " + passenger.getPassengerId());
                return false;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi cập nhật hành khách ID " + passenger.getPassengerId() + ": " + e.getMessage(), e);
            return false;
        }
    }

    public boolean addCheckIn(CheckIn checkIn) {
        Object[] params = new Object[]{
            checkIn.getPassengerId(),
            checkIn.getBookingId(),
            checkIn.getFlightId(),
            new Timestamp(checkIn.getCheckinTime().atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()),
            checkIn.getStatus(),
            checkIn.getStaffId()
        };
        try {
            int affectedRows = dbContext.execQuery(INSERT_CHECKIN, params);
            if (affectedRows > 0) {
                LOGGER.info("Thêm check-in cho booking ID " + checkIn.getBookingId() + " thành công.");
                return true;
            } else {
                LOGGER.warning("Không có dòng nào được thêm cho check-in của booking ID " + checkIn.getBookingId());
                return false;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi thêm check-in cho booking ID " + checkIn.getBookingId() + ": " + e.getMessage(), e);
            return false;
        }
    }

    public boolean addBookingHistory(BookingHistory history) {
        Object[] params = new Object[]{
            history.getBookingId(),
            history.getStaffId(),
            history.getAction(),
            history.getDescription(),
            new Timestamp(history.getActionTime().atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli())
        };
        try {
            int affectedRows = dbContext.execQuery(INSERT_BOOKING_HISTORY, params);
            if (affectedRows > 0) {
                LOGGER.info("Thêm lịch sử cho booking ID " + history.getBookingId() + " thành công.");
                return true;
            } else {
                LOGGER.warning("Không có dòng nào được thêm cho lịch sử booking ID " + history.getBookingId());
                return false;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi thêm lịch sử cho booking ID " + history.getBookingId() + ": " + e.getMessage(), e);
            return false;
        }
    }

    public List<BookingHistory> getBookingHistory(int bookingId) {
        List<BookingHistory> history = new ArrayList<>();
        Object[] params = new Object[]{bookingId};

        ResultSet rs = null;
        try {
            rs = dbContext.execSelectQuery(SELECT_BOOKING_HISTORY, params);
            while (rs.next()) {
                BookingHistory bh = new BookingHistory();
                bh.setHistoryId(rs.getInt("history_id"));
                bh.setBookingId(rs.getInt("booking_id"));
                bh.setStaffId(rs.getInt("staff_id"));
                bh.setAction(rs.getString("action"));
                bh.setDescription(rs.getString("description"));
                bh.setActionTime(rs.getTimestamp("action_time").toLocalDateTime());
                history.add(bh);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy lịch sử booking", e);
        } finally {
            DBContext.closeSelectResources(rs, null, null);
        }
        return history;
    }
}
