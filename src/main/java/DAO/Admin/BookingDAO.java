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
import model.AircraftType;
import model.Airline;
import model.Route;
import model.Seat;
import model.SeatClass;

/**
 * @author LienXuanThinh - CE182117
 */
public class BookingDAO {

    private Connection conn;
    private DBContext dbContext;
    private static final Logger LOGGER = Logger.getLogger(BookingDAO.class.getName());

    public BookingDAO(Connection conn) {
        this.conn = conn;
        this.dbContext = new DBContext();
    }

    public BookingDAO() {
        try {
            this.conn = new DBContext().getConnection();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi khởi tạo kết nối cơ sở dữ liệu", e);
        }
    }

    public int createBooking(Booking booking) {
        int generatedId = -1;
        String sql = "INSERT INTO Booking (user_id, flight_id, seat_id, booking_time, seat_class, total_price, status, booking_code) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        String bookingCode = generateBookingCode();

        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, booking.getUserId());
            ps.setInt(2, booking.getFlightId());
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

    public Booking getBookingById(int bookingId) {
        Booking booking = null;
        String sql = "SELECT * FROM Booking WHERE booking_id = ?";

        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    booking = new Booking();
                    booking.setBookingId(rs.getInt("booking_id"));
                    booking.setUserId(rs.getInt("user_id"));
                    booking.setFlightId(rs.getInt("flight_id"));
                    if (rs.getObject("seat_id") != null) {
                        booking.setSeatId(rs.getInt("seat_id"));
                    } else {
                        booking.setSeatId(0);
                    }
                    booking.setBookingDate(rs.getTimestamp("booking_time"));
                    booking.setSeatClass(rs.getString("seat_class"));
                    booking.setTotalPrice(rs.getDouble("total_price"));
                    booking.setStatus(rs.getString("status"));
                    booking.setBookingCode(rs.getString("booking_code"));
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy booking theo ID: " + bookingId, e);
        }
        return booking;
    }

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
        String randomDigits = String.valueOf((int) (Math.random() * 9000 + 1000));
        return prefix + "-" + datePart + "-" + randomDigits;
    }

    public int insertAndGetId(Booking booking) {
        int generatedId = -1;
        String sql = "INSERT INTO Booking (user_id, flight_id, booking_time, seat_class, total_price, status, booking_code) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            LOGGER.info("Đang chuẩn bị thực thi câu lệnh INSERT...");
            ps.setInt(1, booking.getUserId());
            ps.setInt(2, booking.getFlightId());
            ps.setTimestamp(3, new java.sql.Timestamp(booking.getBookingDate().getTime()));
            ps.setString(4, booking.getSeatClass());
            ps.setDouble(5, booking.getTotalPrice());
            ps.setString(6, "CONFIRMED");
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
            LOGGER.log(Level.SEVERE, "Lỗi khi thêm booking", e);
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
            LOGGER.log(Level.SEVERE, "Lỗi khi cập nhật trạng thái check-in", e);
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
                booking.setCheckInStatus(rs.getString("checkin_status"));
                booking.setBookingCode(rs.getString("booking_code"));
                return booking;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy booking theo mã", e);
        }
        return null;
    }

    public void updateBookingAmount(Booking booking) {
        String sql = "UPDATE Booking SET total_amount = ? WHERE booking_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDouble(1, booking.getTotalPrice());
            ps.setInt(2, booking.getBookingId());
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi cập nhật số tiền booking", e);
        }
    }

    public List<Booking> getAllBookings(String statusFilter) {
        List<Booking> bookings = new ArrayList<>();
        String query = "SELECT b.booking_id, b.user_id, b.flight_id, b.booking_time, b.status, b.seat_id, b.total_price, b.booking_code, b.staff_note, b.last_updated_by, b.last_updated_at, "
                + "f.flight_number, f.origin_iata, f.dest_iata, f.origin_name, f.dest_name, f.route_id, "
                + "c.status AS checkin_status, "
                + "s.seat_number, s.class_id, s.is_booked, sc.Name AS seat_class_name "
                + "FROM Booking b "
                + "JOIN v_FlightWithRoute f ON b.flight_id = f.flight_id "
                + "LEFT JOIN CheckIn c ON b.booking_id = c.booking_id "
                + "LEFT JOIN Seat s ON b.seat_id = s.seat_id "
                + "LEFT JOIN TicketClass tc ON s.class_id = tc.class_id "
                + "LEFT JOIN SeatClass sc ON tc.SeatClassID = sc.SeatClassID";
        Object[] params = null;
        if (statusFilter != null && !statusFilter.isEmpty()) {
            if (statusFilter.equals("CHECKED-IN") || statusFilter.equals("BOARDED")) {
                query += " WHERE c.status = ?";
                params = new Object[]{statusFilter};
            } else {
                query += " WHERE b.status = ?";
                params = new Object[]{statusFilter};
            }
        }

        try (ResultSet rs = dbContext.execSelectQuery(query, params)) {
            while (rs.next()) {
                Booking booking = new Booking();
                booking.setBookingId(rs.getInt("booking_id"));
                booking.setUserId(rs.getInt("user_id"));
                booking.setFlightId(rs.getInt("flight_id"));
                booking.setBookingDate(rs.getTimestamp("booking_time"));
                booking.setStatus(rs.getString("status"));
                booking.setTotalPrice(rs.getBigDecimal("total_price").doubleValue());
                booking.setBookingCode(rs.getString("booking_code"));
                booking.setStaffNote(rs.getString("staff_note"));
                booking.setLastUpdatedBy(rs.getInt("last_updated_by"));
                booking.setLastUpdatedAt(rs.getTimestamp("last_updated_at"));
                booking.setCheckInStatus(rs.getString("checkin_status") != null ? rs.getString("checkin_status") : "NOT CHECKED-IN");

                // Gán đối tượng Seat
                Seat seat = new Seat(rs.getInt("seat_id"), rs.getInt("flight_id"), rs.getInt("class_id"), rs.getString("seat_number"), rs.getBoolean("is_booked"));
                seat.setSeatId(rs.getInt("seat_id"));
                seat.setSeatNumber(rs.getString("seat_number"));
                seat.setClassId(rs.getInt("class_id"));
                seat.setIsBooked(rs.getBoolean("is_booked"));

                SeatClass seatClass = new SeatClass();
                seatClass.setName(rs.getString("seat_class_name"));
                seat.setSeatClass(seatClass);

                booking.setSeat(seat);

                booking.setUserFullName(getPassengerFullNames(rs.getInt("booking_id")));

                // Tạo đối tượng Flight
                Flight flight = new Flight();
                flight.setFlightNumber(rs.getString("flight_number"));

                // Tạo đối tượng Route
                Route route = new Route();
                route.setRouteId(rs.getInt("route_id"));
                route.setOriginIata(rs.getString("origin_iata"));
                route.setDestIata(rs.getString("dest_iata"));
                route.setOriginName(rs.getString("origin_name"));
                route.setDestName(rs.getString("dest_name"));
                flight.setRoute(route);

                booking.setFlight(flight);
                bookings.add(booking);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy danh sách booking: " + e.getMessage(), e);
        }
        return bookings;
    }

    public List<Flight> getAllFlights() {
        List<Flight> flights = new ArrayList<>();
        String sql = "SELECT * FROM v_FlightWithRoute";
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Flight f = new Flight();
                f.setFlightId(rs.getInt("flight_id"));
                f.setFlightNumber(rs.getString("flight_number"));
                f.setDepartureTime(rs.getTimestamp("departure_time"));
                f.setArrivalTime(rs.getTimestamp("arrival_time"));
                f.setStatus(rs.getString("status"));
                // AircraftTypeID nếu cần: f.setAircraftType(new AircraftType(rs.getInt("AircraftTypeID")));
                Route r = new Route();
                r.setOriginIata(rs.getString("origin_iata"));
                r.setDestIata(rs.getString("dest_iata"));
                // Có thể thêm origin_name, dest_name, origin_city, dest_city nếu cần
                f.setRoute(r);
                Airline a = new Airline();
                a.setName(rs.getString("airline_name"));
                f.setAirline(a);
                flights.add(f);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy danh sách chuyến bay", e);
        }
        return flights;
    }

    public List<Booking> getBookingsByFlightId(int flightId, String statusFilter) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT b.booking_id, b.user_id, b.flight_id, b.booking_time, b.status, b.seat_id, b.total_price, b.booking_code, b.staff_note, "
                + "p.full_name AS userFullName, p.passport_number AS passport, f.flight_number, "
                + "ao.iata_code AS originIata, ad.iata_code AS destIata, "
                + "s.seat_number, s.class_id, s.is_booked, sc.Name AS seat_class_name, "
                + "c.status AS checkInStatus "
                + "FROM Booking b "
                + "LEFT JOIN Passenger p ON b.booking_id = p.booking_id "
                + "LEFT JOIN Flight f ON b.flight_id = f.flight_id "
                + "LEFT JOIN Route r ON f.route_id = r.route_id "
                + "LEFT JOIN Airport ao ON r.origin_airport_id = ao.airport_id "
                + "LEFT JOIN Airport ad ON r.destination_airport_id = ad.airport_id "
                + "LEFT JOIN Seat s ON b.seat_id = s.seat_id "
                + "LEFT JOIN TicketClass tc ON s.class_id = tc.class_id "
                + "LEFT JOIN SeatClass sc ON tc.SeatClassID = sc.SeatClassID "
                + "LEFT JOIN CheckIn c ON b.booking_id = c.booking_id "
                + "WHERE b.flight_id = ?";

        List<Object> params = new ArrayList<>();
        params.add(flightId);

        if (statusFilter != null && !statusFilter.isEmpty()) {
            if (statusFilter.equals("CHECKED-IN") || statusFilter.equals("BOARDED")) {
                sql += " AND c.status = ?";
            } else {
                sql += " AND b.status = ?";
            }
            params.add(statusFilter);
        }

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Booking b = new Booking();
                    b.setBookingId(rs.getInt("booking_id"));
                    b.setUserId(rs.getInt("user_id"));
                    b.setFlightId(rs.getInt("flight_id"));
                    b.setBookingDate(rs.getTimestamp("booking_time"));
                    b.setStatus(rs.getString("status"));
                    b.setTotalPrice(rs.getDouble("total_price"));
                    b.setBookingCode(rs.getString("booking_code"));
                    b.setStaffNote(rs.getString("staff_note"));
                    b.setCheckInStatus(rs.getString("checkInStatus") != null ? rs.getString("checkInStatus") : "NOT CHECKED-IN");

                    Seat seat = null;
                    int seatId = rs.getInt("seat_id");
                    if (!rs.wasNull()) {
                        seat = new Seat(rs.getInt("seat_id"), rs.getInt("flight_id"), rs.getInt("class_id"), rs.getString("seat_number"), rs.getBoolean("is_booked"));
                        seat.setSeatId(seatId);
                        seat.setSeatNumber(rs.getString("seat_number"));
                        seat.setClassId(rs.getInt("class_id"));
                        seat.setIsBooked(rs.getBoolean("is_booked"));
                        SeatClass seatClass = new SeatClass();
                        seatClass.setName(rs.getString("seat_class_name"));
                        seat.setSeatClass(seatClass);
                    }
                    b.setSeat(seat);

                    Passenger passen = new Passenger();
                    passen.setFullName(rs.getString("userFullName"));
                    passen.setPassportNumber(rs.getString("passport"));
                    b.setPassenger(passen);

                    Flight f = new Flight();
                    f.setFlightNumber(rs.getString("flight_number"));
                    Route r = new Route();
                    r.setOriginIata(rs.getString("originIata"));
                    r.setDestIata(rs.getString("destIata"));
                    f.setRoute(r);
                    b.setFlight(f);
                    bookings.add(b);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy bookings theo flightId: " + flightId, e);
        }
        LOGGER.info("Số booking lấy được cho flightId " + flightId + ": " + bookings.size());
        return bookings;
    }

    public List<Booking> searchBookings(String searchTerm) {
        List<Booking> bookings = new ArrayList<>();
        String searchPattern = "%" + searchTerm + "%";
        String query = "SELECT b.booking_id, b.user_id, b.flight_id, b.booking_time, b.status, b.seat_id, b.total_price, b.booking_code, b.staff_note, b.last_updated_by, b.last_updated_at, "
                + "f.flight_number, f.origin_iata, f.dest_iata, f.origin_name, f.dest_name, f.route_id, "
                + "c.status AS checkin_status, "
                + "p.full_name, p.passport_number, "
                + "s.seat_number, s.class_id, s.is_booked, sc.Name AS seat_class_name "
                + "FROM Booking b "
                + "JOIN v_FlightWithRoute f ON b.flight_id = f.flight_id "
                + "LEFT JOIN Passenger p ON b.booking_id = p.booking_id "
                + "LEFT JOIN CheckIn c ON b.booking_id = c.booking_id "
                + "LEFT JOIN Seat s ON b.seat_id = s.seat_id "
                + "LEFT JOIN TicketClass tc ON s.class_id = tc.class_id "
                + "LEFT JOIN SeatClass sc ON tc.SeatClassID = sc.SeatClassID "
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
                booking.setTotalPrice(rs.getBigDecimal("total_price").doubleValue());
                booking.setBookingCode(rs.getString("booking_code"));
                booking.setStaffNote(rs.getString("staff_note"));
                booking.setLastUpdatedBy(rs.getInt("last_updated_by"));
                booking.setLastUpdatedAt(rs.getTimestamp("last_updated_at"));
                booking.setCheckInStatus(rs.getString("checkin_status") != null ? rs.getString("checkin_status") : "NOT CHECKED-IN");

                // Gán đối tượng Seat
                Seat seat = new Seat(rs.getInt("seat_id"), rs.getInt("flight_id"), rs.getInt("class_id"), rs.getString("seat_number"), rs.getBoolean("is_booked"));
                seat.setSeatId(rs.getInt("seat_id"));
                seat.setSeatNumber(rs.getString("seat_number"));
                seat.setClassId(rs.getInt("class_id"));
                seat.setIsBooked(rs.getBoolean("is_booked"));

                SeatClass seatClass = new SeatClass();
                seatClass.setName(rs.getString("seat_class_name"));
                seat.setSeatClass(seatClass);

                booking.setSeat(seat);

                Passenger passen = new Passenger();
                passen.setFullName(rs.getString("full_name"));
                passen.setPassportNumber(rs.getString("passport_number"));
                booking.setPassenger(passen);

                // Tạo đối tượng Flight
                Flight flight = new Flight();
                flight.setFlightNumber(rs.getString("flight_number"));

                // Tạo đối tượng Route
                Route route = new Route();
                route.setRouteId(rs.getInt("route_id"));
                route.setOriginIata(rs.getString("origin_iata"));
                route.setDestIata(rs.getString("dest_iata"));
                route.setOriginName(rs.getString("origin_name"));
                route.setDestName(rs.getString("dest_name"));
                flight.setRoute(route);

                booking.setFlight(flight);
                bookings.add(booking);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi tìm kiếm booking: " + e.getMessage(), e);
        }
        return bookings;
    }

    // Lấy chi tiết booking
    public Booking getBookingDetails(int bookingId) {
        String query = "SELECT b.booking_id, b.user_id, b.flight_id, b.booking_time, b.status, b.seat_id, b.total_price, b.booking_code, b.staff_note, b.last_updated_by, b.last_updated_at, "
                + "f.flight_number, f.departure_time, f.arrival_time, f.status AS flight_status, f.route_id, "
                + "f.origin_iata, f.origin_name, f.dest_iata, f.dest_name, "
                + "s.seat_number, s.class_id, s.is_booked, sc.Name AS seat_class_name "
                + "FROM Booking b "
                + "JOIN v_FlightWithRoute f ON b.flight_id = f.flight_id "
                + "LEFT JOIN Seat s ON b.seat_id = s.seat_id "
                + "LEFT JOIN TicketClass tc ON s.class_id = tc.class_id "
                + "LEFT JOIN SeatClass sc ON tc.SeatClassID = sc.SeatClassID "
                + "WHERE b.booking_id = ?";
        try (ResultSet rs = dbContext.execSelectQuery(query, new Object[]{bookingId})) {
            if (rs.next()) {
                Booking booking = new Booking();
                booking.setBookingId(rs.getInt("booking_id"));
                booking.setUserId(rs.getInt("user_id"));
                booking.setFlightId(rs.getInt("flight_id"));
                booking.setBookingDate(rs.getTimestamp("booking_time"));
                booking.setStatus(rs.getString("status"));
                booking.setTotalPrice(rs.getBigDecimal("total_price").doubleValue());
                booking.setBookingCode(rs.getString("booking_code"));
                booking.setStaffNote(rs.getString("staff_note"));
                booking.setLastUpdatedBy(rs.getInt("last_updated_by"));
                booking.setLastUpdatedAt(rs.getTimestamp("last_updated_at"));

                // Gán đối tượng Seat
                Seat seat = new Seat(rs.getInt("seat_id"), rs.getInt("flight_id"), rs.getInt("class_id"), rs.getString("seat_number"), rs.getBoolean("is_booked"));
                seat.setSeatId(rs.getInt("seat_id"));
                seat.setSeatNumber(rs.getString("seat_number"));
                seat.setClassId(rs.getInt("class_id"));
                seat.setIsBooked(rs.getBoolean("is_booked"));

                SeatClass seatClass = new SeatClass();
                seatClass.setName(rs.getString("seat_class_name"));
                seat.setSeatClass(seatClass);

                booking.setSeat(seat);

                booking.setUserFullName(getPassengerFullNames(bookingId));

                // Tạo đối tượng Flight
                Flight flight = new Flight();
                flight.setFlightNumber(rs.getString("flight_number"));
                flight.setDepartureTime(rs.getTimestamp("departure_time"));
                flight.setArrivalTime(rs.getTimestamp("arrival_time"));
                flight.setStatus(rs.getString("flight_status"));

                // Tạo đối tượng Route
                Route route = new Route();
                route.setRouteId(rs.getInt("route_id"));
                route.setOriginIata(rs.getString("origin_iata"));
                route.setDestIata(rs.getString("dest_iata"));
                route.setOriginName(rs.getString("origin_name"));
                route.setDestName(rs.getString("dest_name"));
                flight.setRoute(route);

                booking.setFlight(flight);
                return booking;
            } else {
                LOGGER.info("Không tìm thấy booking với bookingId: " + bookingId);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy chi tiết booking với bookingId: " + bookingId, e);
        }
        return null;
    }

    // Lấy thông tin hành khách
    public Passenger getPassengerDetails(int bookingId) {
        String query = "SELECT p.passenger_id, p.full_name, p.passport_number, p.dob, p.gender, p.phone_number, p.email, p.country, p.address "
                + "FROM Passenger p "
                + "WHERE p.booking_id = ?";
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

    // Lấy chi tiết chuyến bay
    public Flight getFlightDetails(int bookingId) {
        String query = "SELECT f.flight_id, f.flight_number, f.departure_time, f.arrival_time, f.status, f.route_id, "
                + "ao.iata_code AS origin_iata, ao.name AS origin_name, "
                + "ad.iata_code AS dest_iata, ad.name AS dest_name, "
                + "al.name AS airline_name, at.AircraftTypeID, at.AircraftTypeName "
                + "FROM Booking b "
                + "JOIN Flight f ON b.flight_id = f.flight_id "
                + "JOIN Route r ON f.route_id = r.route_id "
                + "JOIN Airport ao ON r.origin_airport_id = ao.airport_id "
                + "JOIN Airport ad ON r.destination_airport_id = ad.airport_id "
                + "JOIN Airline al ON f.airline_id = al.airline_id "
                + "LEFT JOIN AircraftType at ON f.AircraftTypeID = at.AircraftTypeID "
                + "WHERE b.booking_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, bookingId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Flight flight = new Flight();
                    flight.setFlightId(rs.getInt("flight_id"));
                    flight.setFlightNumber(rs.getString("flight_number"));
                    flight.setDepartureTime(rs.getTimestamp("departure_time"));
                    flight.setArrivalTime(rs.getTimestamp("arrival_time"));
                    flight.setStatus(rs.getString("status"));

                    Route route = new Route();
                    route.setRouteId(rs.getInt("route_id"));
                    route.setOriginIata(rs.getString("origin_iata"));
                    route.setOriginName(rs.getString("origin_name"));
                    route.setDestIata(rs.getString("dest_iata"));
                    route.setDestName(rs.getString("dest_name"));
                    flight.setRoute(route);

                    Airline airline = new Airline();
                    airline.setName(rs.getString("airline_name"));
                    flight.setAirline(airline);

                    AircraftType aircraftType = new AircraftType();
                    aircraftType.setAircraftTypeId(rs.getInt("AircraftTypeID"));
                    aircraftType.setAircraftTypeName(rs.getString("AircraftTypeName"));
                    flight.setAircraftType(aircraftType);

                    LOGGER.info("Flight details for bookingId " + bookingId + ": flight_number=" + flight.getFlightNumber()
                            + ", origin_iata=" + route.getOriginIata() + ", aircraft_type_name=" + (aircraftType.getAircraftTypeName() != null ? aircraftType.getAircraftTypeName() : "null"));
                    return flight;
                } else {
                    LOGGER.warning("Không tìm thấy chuyến bay cho bookingId: " + bookingId);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy chi tiết chuyến bay cho bookingId: " + bookingId, e);
        }
        return null;
    }

    // Cập nhật booking
    public boolean updateBooking(Booking booking, int staffId) throws SQLException {
        String query = "UPDATE Booking SET status = ?, seat_id = ?, total_price = ?, staff_note = ?, last_updated_by = ?, last_updated_at = ? WHERE booking_id = ?";
        String updateSeatSql = "UPDATE Seat SET is_booked = 1 WHERE seat_id = ?";
        String resetOldSeatSql = "UPDATE Seat SET is_booked = 0 WHERE seat_id = (SELECT seat_id FROM Booking WHERE booking_id = ?)";

        try (
                PreparedStatement stmt = conn.prepareStatement(query); PreparedStatement psSeat = conn.prepareStatement(updateSeatSql); PreparedStatement psResetSeat = conn.prepareStatement(resetOldSeatSql)) {
            // Đặt lại trạng thái is_booked của ghế cũ
            psResetSeat.setInt(1, booking.getBookingId());
            psResetSeat.executeUpdate();

            // Cập nhật booking
            stmt.setString(1, booking.getStatus());
            stmt.setInt(2, booking.getSeat().getSeatId());
            stmt.setBigDecimal(3, new java.math.BigDecimal(booking.getTotalPrice()));
            stmt.setString(4, booking.getStaffNote());
            stmt.setInt(5, staffId);
            stmt.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
            stmt.setInt(7, booking.getBookingId());
            int affectedRows = stmt.executeUpdate();

            // Cập nhật is_booked cho ghế mới
            if (affectedRows > 0) {
                psSeat.setInt(1, booking.getSeat().getSeatId());
                psSeat.executeUpdate();
            }

            LOGGER.info("Cập nhật booking ID " + booking.getBookingId() + ": " + (affectedRows > 0 ? "Thành công" : "Thất bại"));
            return affectedRows > 0;
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

    public List<CheckIn> getCheckInDetails(int bookingId) throws SQLException {
        List<CheckIn> checkIns = new ArrayList<>();
        String query = "SELECT checkin_id, passenger_id, booking_id, flight_id, checkin_time, status, user_id "
                + "FROM CheckIn WHERE booking_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                CheckIn checkIn = new CheckIn();
                checkIn.setCheckinId(rs.getInt("checkin_id"));
                checkIn.setPassengerId(rs.getInt("passenger_id"));
                checkIn.setBookingId(rs.getInt("booking_id"));
                checkIn.setFlightId(rs.getInt("flight_id"));
                Timestamp checkinTime = rs.getTimestamp("checkin_time");
                checkIn.setCheckinTime(checkinTime != null ? checkinTime : null);
                checkIn.setStatus(rs.getString("status"));
                checkIn.setUserId(rs.getInt("user_id"));
                checkIns.add(checkIn);
            }
            LOGGER.info("Lấy CheckIn cho bookingId: " + bookingId + ", số lượng: " + checkIns.size());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy CheckIn cho bookingId: " + bookingId, e);
            throw e;
        }
        return checkIns;
    }

    public boolean addCheckIn(CheckIn checkIn) throws SQLException {
        // Kiểm tra xem đã có bản ghi check-in cho booking và passenger chưa
        String checkQuery = "SELECT checkin_id FROM CheckIn WHERE booking_id = ? AND passenger_id = ?";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
            checkStmt.setInt(1, checkIn.getBookingId());
            checkStmt.setInt(2, checkIn.getPassengerId());
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                // Cập nhật bản ghi check-in hiện có
                String updateQuery = "UPDATE CheckIn SET checkin_time = ?, status = ?, user_id = ? WHERE checkin_id = ?";
                try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                    updateStmt.setTimestamp(1, (checkIn.getCheckinTime()));
                    updateStmt.setString(2, checkIn.getStatus());
                    updateStmt.setInt(3, checkIn.getUserId());
                    updateStmt.setInt(4, rs.getInt("checkin_id"));
                    return updateStmt.executeUpdate() > 0;
                }
            } else {
                // Thêm bản ghi check-in mới
                String insertQuery = "INSERT INTO CheckIn (passenger_id, booking_id, flight_id, checkin_time, status, user_id) "
                        + "VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                    insertStmt.setInt(1, checkIn.getPassengerId());
                    insertStmt.setInt(2, checkIn.getBookingId());
                    insertStmt.setInt(3, checkIn.getFlightId());
                    insertStmt.setTimestamp(4, checkIn.getCheckinTime());
                    insertStmt.setString(5, checkIn.getStatus());
                    insertStmt.setInt(6, checkIn.getUserId());
                    return insertStmt.executeUpdate() > 0;
                }
            }
        }
    }

    public boolean addBookingHistory(BookingHistory history) {
        String query = "INSERT INTO BookingHistory (booking_id, user_id, action, description, action_time) VALUES (?, ?, ?, ?, ?)";
        Object[] params = new Object[]{
            history.getBookingId(),
            history.getUserId(),
            history.getAction(),
            history.getDescription(),
            history.getActionTime()
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
        String query = "SELECT bh.history_id, bh.booking_id, bh.user_id, bh.action, bh.description, bh.action_time, "
                + "a.role AS role, a.fullname AS user_name "
                + "FROM BookingHistory bh "
                + "LEFT JOIN Account a ON bh.user_id = a.user_id "
                + "WHERE bh.booking_id = ?";
        Object[] params = new Object[]{bookingId};

        try (ResultSet rs = dbContext.execSelectQuery(query, params)) {
            while (rs.next()) {
                BookingHistory bh = new BookingHistory();
                bh.setHistoryId(rs.getInt("history_id"));
                bh.setBookingId(rs.getInt("booking_id"));
                bh.setUserId(rs.getInt("user_id"));
                bh.setAction(rs.getString("action"));
                bh.setDescription(rs.getString("description"));
                bh.setActionTime(rs.getTimestamp("action_time"));
                bh.setRole(rs.getString("role"));
                bh.setUserName(rs.getString("user_name"));
                history.add(bh);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy lịch sử booking", e);
        }
        return history;
    }

    public String getBookingCodeById(int bookingId) {
        String sql = "SELECT booking_code FROM Booking WHERE booking_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("booking_code");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy mã booking", e);
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
                b.setFlightId(rs.getInt("flight_id"));
                list.add(b);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy danh sách booking theo userId", e);
        }
        return list;
    }

    public void cancelBooking(int bookingId) {
        String sql = "UPDATE Booking SET status = 'Cancelled' WHERE booking_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi hủy booking", e);
        }
    }

    public Timestamp getDepartureTimeByFlightId(int flightId) {
        String sql = "SELECT departure_time FROM Flight WHERE flight_id = ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, flightId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getTimestamp("departure_time");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getPassengerFullNames(int bookingId) {
        String query = "SELECT TOP 1 full_name FROM Passenger WHERE booking_id = ?";
        try (ResultSet rs = dbContext.execSelectQuery(query, new Object[]{bookingId})) {
            if (rs.next()) {
                return rs.getString("full_name");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy full_name của hành khách cho booking_id " + bookingId, e);
        }
        return "N/A";
    }

    public List<Seat> getAvailableSeats(int flightId) {
        List<Seat> seats = new ArrayList<>();
        String sql = "SELECT s.seat_id, s.seat_number, s.class_id, s.is_booked, sc.Name AS seat_class_name "
                + "FROM Seat s "
                + "JOIN TicketClass tc ON s.class_id = tc.class_id "
                + "JOIN SeatClass sc ON tc.SeatClassID = sc.SeatClassID "
                + "WHERE s.flight_id = ? AND s.is_booked = 0";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, flightId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Seat seat = new Seat();
                    seat.setSeatId(rs.getInt("seat_id"));
                    seat.setSeatNumber(rs.getString("seat_number"));
                    seat.setClassId(rs.getInt("class_id"));
                    seat.setIsBooked(rs.getBoolean("is_booked"));
                    SeatClass seatClass = new SeatClass();
                    seatClass.setName(rs.getString("seat_class_name"));
                    seat.setSeatClass(seatClass);
                    seats.add(seat);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy danh sách ghế khả dụng cho flightId: " + flightId, e);
        }
        return seats;
    }

    public void updateSeatBookingStatus(int seatId, boolean isBooked) throws SQLException {
        String sql = "UPDATE Seat SET is_booked = ? WHERE seat_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, isBooked);
            ps.setInt(2, seatId);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.info("Cập nhật trạng thái is_booked cho seatId " + seatId + " thành " + isBooked);
            } else {
                LOGGER.warning("Không tìm thấy ghế với seatId: " + seatId);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi cập nhật is_booked cho seatId: " + seatId, e);
            throw e;
        }
    }

    // Lấy chi tiết chuyến bay
    public Flight getFlightDetails2(int flightId) {
        String query = "SELECT f.flight_id, f.flight_number, f.departure_time, f.arrival_time, f.status, f.route_id, "
                + "ao.city AS origin_city, ao.name AS origin_name, "
                + "ad.city AS dest_city, ad.name AS dest_name, "
                + "al.name AS airline_name "
                + "FROM Flight f "
                + "JOIN Route r ON f.route_id = r.route_id "
                + "JOIN Airport ao ON r.origin_airport_id = ao.airport_id "
                + "JOIN Airport ad ON r.destination_airport_id = ad.airport_id "
                + "JOIN Airline al ON f.airline_id = al.airline_id "
                + "WHERE flight_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, flightId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Flight flight = new Flight();
                    flight.setFlightId(rs.getInt("flight_id"));
                    flight.setFlightNumber(rs.getString("flight_number"));
                    flight.setDepartureTime(rs.getTimestamp("departure_time"));
                    flight.setArrivalTime(rs.getTimestamp("arrival_time"));
                    flight.setStatus(rs.getString("status"));

                    Route route = new Route();
                    route.setRouteId(rs.getInt("route_id"));
                    route.setOriginIata(rs.getString("origin_city"));
                    route.setOriginName(rs.getString("origin_name"));
                    route.setDestIata(rs.getString("dest_city"));
                    route.setDestName(rs.getString("dest_name"));
                    flight.setRoute(route);

                    Airline airline = new Airline();
                    airline.setName(rs.getString("airline_name"));
                    flight.setAirline(airline);

                    LOGGER.info("Flight details for flightId " + flightId + ": flight_number=" + flight.getFlightNumber()
                            + ", origin_iata=" + route.getOriginIata() + ", origin_name=" + route.getOriginName()
                            + ", origin_iata=" + route.getOriginIata() + ", dest_iata=" + route.getDestIata()
                            + ", dest_name=" + route.getDestName() + ", dest_iata=" + route.getDestIata()
                            + ", airline_name=" + airline.getName());
                    return flight;
                } else {
                    LOGGER.warning("Không tìm thấy chuyến bay cho bookingId: " + flightId);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy chi tiết chuyến bay cho bookingId: " + flightId, e);
        }
        return null;
    }
}
