package DAO.Admin;

import model.Flight;
import utils.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * FlightDAO đầy đủ cho cả:
 *  - Quản lý chuyến bay (CRUD)
 *  - Quản lý giá vé (liệt kê chuyến, lấy chuyến theo id)
 *
 * Bảng dbo.Flight:
 *  flight_id (PK), airline_id (INT, NULL), flight_num (NVARCHAR),
 *  departure_time (DATETIME/TIMESTAMP), arrival_time (DATETIME/TIMESTAMP),
 *  status (NVARCHAR), route_id (INT, NULL), AircraftType (INT, NULL)
 */
public class FlightDAO {

    // --------- Helpers ----------
    private Flight map(ResultSet rs) throws SQLException {
        Flight f = new Flight();
        f.setFlightId(rs.getInt("flight_id"));
        f.setAirlineId((Integer) rs.getObject("airline_id"));
        f.setFlightNumber(rs.getString("flight_num"));
        f.setDepartureTime(rs.getTimestamp("departure_time"));
        f.setArrivalTime(rs.getTimestamp("arrival_time"));
        f.setStatus(rs.getString("status"));
        f.setRouteId((Integer) rs.getObject("route_id"));
        try {
            f.setAircraftType((Integer) rs.getObject("AircraftType"));
        } catch (SQLException ignore) {
            // cột có thể không tồn tại tuỳ DB -> bỏ qua
        }
        return f;
    }

    private void setNullableInt(PreparedStatement ps, int idx, Integer value) throws SQLException {
        if (value == null) ps.setNull(idx, Types.INTEGER);
        else ps.setInt(idx, value);
    }

    // --------- Basic reads (dùng cho Quản Lý Giá Vé) ----------
    /** Lấy tất cả chuyến bay (order theo flight_id). */
    public List<Flight> getAllFlights() {
        String sql = "SELECT flight_id, airline_id, flight_num, departure_time, " +
                     "arrival_time, status, route_id, AircraftType " +
                     "FROM dbo.Flight ORDER BY flight_id";
        List<Flight> list = new ArrayList<>();
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /** Lấy 1 chuyến theo id. */
    public Flight getFlightById(int flightId) {
        String sql = "SELECT flight_id, airline_id, flight_num, departure_time, " +
                     "arrival_time, status, route_id, AircraftType " +
                     "FROM dbo.Flight WHERE flight_id = ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, flightId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // --------- CRUD cho quản lý chuyến bay ----------
    /** Thêm chuyến bay mới. */
    public int insertFlight(Flight f) {
        String sql = "INSERT INTO dbo.Flight " +
                     "(airline_id, flight_num, departure_time, arrival_time, status, route_id, AircraftType) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            setNullableInt(ps, 1, f.getAirlineId());
            ps.setString(2, f.getFlightNumber());
            ps.setTimestamp(3, f.getDepartureTime());
            ps.setTimestamp(4, f.getArrivalTime());
            ps.setString(5, f.getStatus());
            setNullableInt(ps, 6, f.getRouteId());
            setNullableInt(ps, 7, f.getAircraftType());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) return keys.getInt(1);
                }
            }
            return affected; // nếu DB không trả khóa tự tăng
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /** Cập nhật chuyến bay. */
    public int updateFlight(Flight f) {
        String sql = "UPDATE dbo.Flight SET " +
                     "airline_id = ?, flight_num = ?, departure_time = ?, arrival_time = ?, " +
                     "status = ?, route_id = ?, AircraftType = ? " +
                     "WHERE flight_id = ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            setNullableInt(ps, 1, f.getAirlineId());
            ps.setString(2, f.getFlightNumber());
            ps.setTimestamp(3, f.getDepartureTime());
            ps.setTimestamp(4, f.getArrivalTime());
            ps.setString(5, f.getStatus());
            setNullableInt(ps, 6, f.getRouteId());
            setNullableInt(ps, 7, f.getAircraftType());
            ps.setInt(8, f.getFlightId());

            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /** Xoá chuyến bay theo id. */
    public int deleteFlight(int flightId) {
        String sql = "DELETE FROM dbo.Flight WHERE flight_id = ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, flightId);
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // --------- Tìm kiếm/tiện ích ----------
    /**
     * Tìm kiếm linh hoạt:
     *  - flightNumLike: tìm theo LIKE trên flight_num (có thể null/empty để bỏ qua)
     *  - routeId: filter route_id (null để bỏ qua)
     *  - departDate: filter theo ngày khởi hành (so sánh CAST(departure_time AS DATE))
     */
    public List<Flight> searchFlights(String flightNumLike, Integer routeId, Date departDate) {
        StringBuilder sb = new StringBuilder(
            "SELECT flight_id, airline_id, flight_num, departure_time, " +
            "arrival_time, status, route_id, AircraftType " +
            "FROM dbo.Flight WHERE 1=1"
        );
        List<Object> params = new ArrayList<>();

        if (flightNumLike != null && !flightNumLike.isBlank()) {
            sb.append(" AND flight_num LIKE ?");
            params.add("%" + flightNumLike.trim() + "%");
        }
        if (routeId != null) {
            sb.append(" AND route_id = ?");
            params.add(routeId);
        }
        if (departDate != null) {
            sb.append(" AND CAST(departure_time AS DATE) = ?");
            params.add(departDate);
        }
        sb.append(" ORDER BY departure_time, flight_id");

        List<Flight> list = new ArrayList<>();
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sb.toString())) {
            // bind params
            int idx = 1;
            for (Object p : params) {
                if (p instanceof Integer) ps.setInt(idx++, (Integer) p);
                else if (p instanceof String) ps.setString(idx++, (String) p);
                else if (p instanceof Date) ps.setDate(idx++, (Date) p);
                else ps.setObject(idx++, p);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /** Danh sách chuyến bay khởi hành trong ngày hôm nay (theo server SQL). */
    public List<Flight> getFlightsToday() {
        String sql = "SELECT flight_id, airline_id, flight_num, departure_time, " +
                     "arrival_time, status, route_id, AircraftType " +
                     "FROM dbo.Flight " +
                     "WHERE CAST(departure_time AS DATE) = CAST(GETDATE() AS DATE) " +
                     "ORDER BY departure_time";
        List<Flight> list = new ArrayList<>();
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
