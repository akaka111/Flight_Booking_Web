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
        // Thêm seat_id vào câu lệnh INSERT
        String sql = "INSERT INTO Booking (user_id, flight_id, seat_id, booking_time, seat_class, total_price, status, booking_code) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        String bookingCode = generateBookingCode();

        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, booking.getUserId());
            ps.setInt(2, booking.getFlightId());
            // <-- THAY ĐỔI QUAN TRỌNG: LƯU SEAT_ID VÀO DATABASE -->
            ps.setInt(3, booking.getSeatId());
            ps.setTimestamp(4, booking.getBookingDate());
            ps.setString(5, booking.getSeatClass());
            ps.setDouble(6, booking.getTotalPrice());
            ps.setString(7, booking.getStatus());
            ps.setString(8, bookingCode);

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedId = rs.getInt(1);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi tạo booking mới", e);
        }
        return generatedId;
    }

    // Trong file BookingDAO.java
    // Phương thức để lấy một booking từ DB bằng ID
    public Booking getBookingById(int bookingId) {
        Booking booking = null;
        // Sửa câu SQL để bao gồm tất cả các cột cần thiết, đặc biệt là seat_id
        String sql = "SELECT * FROM Booking WHERE booking_id = ?";

        // Sử dụng try-with-resources để tự động quản lý kết nối
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    booking = new Booking();
                    booking.setBookingId(rs.getInt("booking_id"));
                    booking.setUserId(rs.getInt("user_id"));
                    booking.setFlightId(rs.getInt("flight_id"));
                    // <-- THAY ĐỔI QUAN TRỌNG: LẤY SEAT_ID TỪ DATABASE -->
                    if (rs.getObject("seat_id") != null) {
                        booking.setSeatId(rs.getInt("seat_id"));
                    } else {
                        booking.setSeatId(0); // Hoặc một giá trị mặc định nào đó nếu cần
                    }
                    booking.setBookingDate(rs.getTimestamp("booking_time"));
                    booking.setSeatClass(rs.getString("seat_class"));
                    booking.setTotalPrice(rs.getDouble("total_price"));
                    booking.setStatus(rs.getString("status"));
                    booking.setBookingCode(rs.getString("booking_code"));
                    // Lấy các cột khác nếu cần...
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy booking theo ID: " + bookingId, e);
        }
        return booking;
    }

    // Phương thức để cập nhật trạng thái của một booking
    public boolean updateBookingSeat(int bookingId, int newSeatId) {
        String sql = "UPDATE Booking SET seat_id = ? WHERE booking_id = ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newSeatId);
            ps.setInt(2, bookingId);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi cập nhật ghế cho booking ID: " + bookingId, e);
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

    public boolean updateBookingStatus(int bookingId, String newStatus) {
        String sql = "UPDATE Booking SET status = ? WHERE booking_id = ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, bookingId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi cập nhật trạng thái booking", e);
            return false;
        }
    }

    public void updateCheckinStatus(int bookingId, String status) {
        String sql = "UPDATE Booking SET checkin_status = ? WHERE booking_id = ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, bookingId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Booking getBookingByCode(String bookingCode) {
        String sql = "SELECT * FROM Booking WHERE booking_code = ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, bookingCode);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Booking booking = new Booking();
                booking.setBookingId(rs.getInt("booking_id"));
                booking.setUserId(rs.getInt("user_id"));
                booking.setFlightId(rs.getInt("flight_id"));
                booking.setBookingDate(rs.getTimestamp("booking_time"));
                booking.setStatus(rs.getString("status"));
                booking.setSeatClass(rs.getString("seat_class"));
                booking.setTotalPrice(rs.getDouble("total_price"));
                booking.setCheckinStatus(rs.getString("checkin_status"));
                booking.setBookingCode(rs.getString("booking_code"));
                // ... thêm các thuộc tính khác nếu cần
                return booking;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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

    public List<Booking> getAllBookings(String statusFilter) {
        List<Booking> bookings = new ArrayList<>();
        String query = "SELECT b.booking_id, b.user_id, b.flight_id, b.booking_time, b.status, b.seat_class, b.total_price, b.booking_code, b.staff_note, b.last_updated_by, b.last_updated_at, b.checkin_status, "
                + "p.passenger_id, p.full_name, p.passport_number, p.dob, p.gender, p.phone_number, p.email, p.country, p.address "
                + "FROM Booking b "
                + "LEFT JOIN Passenger p ON b.booking_id = p.booking_id";
        Object[] params = null;
        if (statusFilter != null && !statusFilter.isEmpty()) {
            query += " WHERE b.status = ? OR b.checkin_status = ?";
            params = new Object[]{statusFilter, statusFilter};
        }

        try (ResultSet rs = dbContext.execSelectQuery(query, params)) {
            while (rs.next()) {
                Booking booking = new Booking();
                booking.setBookingId(rs.getInt("booking_id"));
                booking.setUserId(rs.getInt("user_id"));
                booking.setFlightId(rs.getInt("flight_id"));
                booking.setBookingDate(rs.getTimestamp("booking_time"));
                booking.setStatus(rs.getString("status"));
                booking.setSeatClass(rs.getString("seat_class"));
                booking.setTotalPrice(rs.getBigDecimal("total_price").doubleValue());
                booking.setBookingCode(rs.getString("booking_code"));
                booking.setStaffNote(rs.getString("staff_note"));
                booking.setLastUpdatedBy(rs.getInt("last_updated_by"));
                booking.setLastUpdatedAt(rs.getTimestamp("last_updated_at"));
                booking.setCheckinStatus(rs.getString("checkin_status"));
                bookings.add(booking);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy danh sách booking", e);
        }
        return bookings;
    }

    public List<Booking> searchBookings(String searchTerm) {
        List<Booking> bookings = new ArrayList<>();
        String searchPattern = "%" + searchTerm + "%";
        String query = "SELECT b.booking_id, b.user_id, b.flight_id, b.booking_time, b.status, b.seat_class, b.total_price, b.booking_code, b.staff_note, b.last_updated_by, b.last_updated_at, b.checkin_status, "
                + "p.passenger_id, p.full_name, p.passport_number, p.dob, p.gender, p.phone_number, p.email, p.country, p.address "
                + "FROM Booking b "
                + "LEFT JOIN Passenger p ON b.booking_id = p.booking_id "
                + "WHERE b.booking_code LIKE ? OR p.full_name LIKE ? OR p.passport_number LIKE ?";
        Object[] params = new Object[]{searchPattern, searchPattern, searchPattern};

        try (ResultSet rs = dbContext.execSelectQuery(query, params)) {
            while (rs.next()) {
                Booking booking = new Booking();
                booking.setBookingId(rs.getInt("booking_id"));
                booking.setUserId(rs.getInt("user_id"));
                booking.setFlightId(rs.getInt("flight_id"));
                booking.setBookingDate(rs.getTimestamp("booking_time"));
                booking.setStatus(rs.getString("status"));
                booking.setSeatClass(rs.getString("seat_class"));
                booking.setTotalPrice(rs.getBigDecimal("total_price").doubleValue());
                booking.setBookingCode(rs.getString("booking_code"));
                booking.setStaffNote(rs.getString("staff_note"));
                booking.setLastUpdatedBy(rs.getInt("last_updated_by"));
                booking.setLastUpdatedAt(rs.getTimestamp("last_updated_at"));
                booking.setCheckinStatus(rs.getString("checkin_status"));
                bookings.add(booking);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi tìm kiếm booking", e);
        }
        return bookings;
    }

    public Booking getBookingDetails(int bookingId) {
        String query = "SELECT b.booking_id, b.user_id, b.flight_id, b.booking_time, b.status, b.seat_class, b.total_price, b.booking_code, b.staff_note, b.last_updated_by, b.last_updated_at, b.checkin_status, "
                + "p.passenger_id, p.full_name, p.passport_number, p.dob, p.gender, p.phone_number, p.email, p.country, p.address, "
                + "f.flight_id, f.airline_id, f.flight_number, f.route_from, f.route_to, f.departure_time, f.arrival_time, f.aircraft, f.status "
                + "FROM Booking b "
                + "LEFT JOIN Passenger p ON b.booking_id = p.booking_id "
                + "LEFT JOIN Flight f ON b.flight_id = f.flight_id "
                + "WHERE b.booking_id = ?";
        try (ResultSet rs = dbContext.execSelectQuery(query, new Object[]{bookingId})) {
            if (rs.next()) {
                Booking booking = new Booking();
                booking.setBookingId(rs.getInt("booking_id"));
                booking.setUserId(rs.getInt("user_id"));
                booking.setFlightId(rs.getInt("flight_id"));
                booking.setBookingDate(rs.getTimestamp("booking_time"));
                booking.setStatus(rs.getString("status"));
                booking.setSeatClass(rs.getString("seat_class"));
                booking.setTotalPrice(rs.getBigDecimal("total_price").doubleValue());
                booking.setBookingCode(rs.getString("booking_code"));
                booking.setStaffNote(rs.getString("staff_note"));
                booking.setLastUpdatedBy(rs.getInt("last_updated_by"));
                booking.setLastUpdatedAt(rs.getTimestamp("last_updated_at"));
                booking.setCheckinStatus(rs.getString("checkin_status"));
                return booking;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy chi tiết booking", e);
        }
        return null;
    }

    public Passenger getPassengerDetails(int bookingId) {
        String query = "SELECT b.booking_id, b.user_id, b.flight_id, b.booking_time, b.status, b.seat_class, b.total_price, b.staff_note, b.last_updated_by, b.last_updated_at, b.checkin_status, "
                + "p.passenger_id, p.full_name, p.passport_number, p.dob, p.gender, p.phone_number, p.email, p.country, p.address, "
                + "f.flight_id, f.airline_id, f.flight_number, f.route_from, f.route_to, f.departure_time, f.arrival_time, f.aircraft, f.status "
                + "FROM Booking b "
                + "LEFT JOIN Passenger p ON b.booking_id = p.booking_id "
                + "LEFT JOIN Flight f ON b.flight_id = f.flight_id "
                + "WHERE b.booking_id = ?";
        try (ResultSet rs = dbContext.execSelectQuery(query, new Object[]{bookingId})) {
            if (rs.next()) {
                Passenger passenger = new Passenger();
                passenger.setPassengerId(rs.getInt("passenger_id"));
                passenger.setFullName(rs.getString("full_name"));
                passenger.setPassportNumber(rs.getString("passport_number"));
                passenger.setDob(rs.getDate("dob"));
                passenger.setGender(rs.getString("gender"));
                passenger.setPhoneNumber(rs.getString("phone_number"));
                passenger.setEmail(rs.getString("email"));
                passenger.setCountry(rs.getString("country"));
                passenger.setAddress(rs.getString("address"));
                return passenger;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy thông tin hành khách", e);
        }
        return null;
    }

    public Flight getFlightDetails(int bookingId) {
        String query = "SELECT b.booking_id, b.user_id, b.flight_id, b.booking_time, b.status, b.seat_class, b.total_price, b.staff_note, b.last_updated_by, b.last_updated_at, b.checkin_status, "
                + "p.passenger_id, p.full_name, p.passport_number, p.dob, p.gender, p.phone_number, p.email, p.country, p.address, "
                + "f.flight_id, f.airline_id, f.flight_number, f.route_from, f.route_to, f.departure_time, f.arrival_time, f.aircraft, f.status "
                + "FROM Booking b "
                + "LEFT JOIN Passenger p ON b.booking_id = p.booking_id "
                + "LEFT JOIN Flight f ON b.flight_id = f.flight_id "
                + "WHERE b.booking_id = ?";
        try (ResultSet rs = dbContext.execSelectQuery(query, new Object[]{bookingId})) {
            if (rs.next()) {
                Flight flight = new Flight();
                flight.setFlightId(rs.getInt("flight_id"));
                flight.setAirlineId(rs.getInt("airline_id"));
                flight.setFlightNumber(rs.getString("flight_number"));
                flight.setRouteFrom(rs.getString("route_from"));
                flight.setRouteTo(rs.getString("route_to"));
                flight.setDepartureTime(rs.getTimestamp("departure_time"));
                flight.setArrivalTime(rs.getTimestamp("arrival_time"));
                flight.setAircraft(rs.getString("aircraft"));
                flight.setStatus(rs.getString("status"));
                return flight;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy chi tiết chuyến bay", e);
        }
        return null;
    }

    public boolean updateBooking(Booking booking, int staffId) {
        String query = "UPDATE Booking SET status = ?, seat_class = ?, total_price = ?, staff_note = ?, last_updated_by = ?, last_updated_at = ?, checkin_status = ? WHERE booking_id = ?";
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
            int affectedRows = dbContext.execQuery(query, params);
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi cập nhật booking ID " + booking.getBookingId() + ": " + e.getMessage(), e);
            return false;
        }
    }

    public boolean updatePassenger(Passenger passenger) {
        String query = "UPDATE Passenger SET full_name = ?, passport_number = ?, dob = ?, gender = ?, phone_number = ?, email = ?, country = ?, address = ? WHERE passenger_id = ?";
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
            int affectedRows = dbContext.execQuery(query, params);
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi cập nhật hành khách ID " + passenger.getPassengerId() + ": " + e.getMessage(), e);
            return false;
        }
    }

    public boolean addCheckIn(CheckIn checkIn) {
        String query = "INSERT INTO CheckIn (passenger_id, booking_id, flight_id, checkin_time, status, staff_id) VALUES (?, ?, ?, ?, ?, ?)";
        Object[] params = new Object[]{
            checkIn.getPassengerId(),
            checkIn.getBookingId(),
            checkIn.getFlightId(),
            new Timestamp(checkIn.getCheckinTime().atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()),
            checkIn.getStatus(),
            checkIn.getStaffId()
        };
        try {
            int affectedRows = dbContext.execQuery(query, params);
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi thêm check-in cho booking ID " + checkIn.getBookingId() + ": " + e.getMessage(), e);
            return false;
        }
    }

    public boolean addBookingHistory(BookingHistory history) {
        String query = "INSERT INTO BookingHistory (booking_id, staff_id, action, description, action_time ) VALUES (?, ?, ?, ?, ?)";
        Object[] params = new Object[]{
            history.getBookingId(),
            history.getAction(),
            history.getDescription(),
            history.getActionTime(),
            history.getStaffId()
        };
        try {
            int affectedRows = dbContext.execQuery(query, params);
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi thêm lịch sử cho booking ID " + history.getBookingId() + ": " + e.getMessage(), e);
            return false;
        }
    }

    public List<BookingHistory> getBookingHistory(int bookingId) {
        List<BookingHistory> history = new ArrayList<>();
        String query = "SELECT bh.history_id, bh.booking_id, bh.action, bh.description, bh.action_time, "
                + "a.role AS role, a.fullname AS staff_name "
                + "FROM BookingHistory bh "
                + "LEFT JOIN Account a ON bh.staff_id = a.user_id "
                + "WHERE bh.booking_id = ?";
        Object[] params = new Object[]{bookingId};

        try (ResultSet rs = dbContext.execSelectQuery(query, params)) {
            while (rs.next()) {
                BookingHistory bh = new BookingHistory();
                bh.setHistoryId(rs.getInt("history_id"));
                bh.setBookingId(rs.getInt("booking_id"));
                bh.setAction(rs.getString("action"));
                bh.setDescription(rs.getString("description"));
                bh.setActionTime(rs.getTimestamp("action_time")); // Chuyển đổi sang LocalDateTime nếu cần
                bh.setRole(rs.getString("role")); // Lấy vai trò
                bh.setStaffName(rs.getString("staff_name")); // Lấy tên nhân viên
                history.add(bh);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy lịch sử booking", e);
        }
        return history;
    }

    public String getBookingCodeById(int bookingId) {
        String sql = "SELECT booking_code FROM Booking WHERE booking_id = ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bookingId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("booking_code");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Booking> getBookingsByUserId(int userId) {
        List<Booking> list = new ArrayList<>();
        String sql = "SELECT * FROM Booking WHERE user_id = ?";

        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Booking b = new Booking();
                b.setBookingId(rs.getInt("booking_id"));
                b.setUserId(rs.getInt("user_id"));
                b.setBookingCode(rs.getString("booking_code"));
                b.setStatus(rs.getString("status"));
                b.setTotalPrice(rs.getBigDecimal("total_price").doubleValue());
                b.setBookingDate(rs.getTimestamp("booking_time"));
                b.setSeatClass(rs.getString("seat_class"));
                b.setFlightId(rs.getInt("flight_id")); // <-- bổ sung dòng này
                list.add(b);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public void cancelBooking(int bookingId) {
        String sql = "UPDATE Booking SET status = 'Cancelled' WHERE booking_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
