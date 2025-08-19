package DAO.Admin;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.Route; // Model Route của bạn (có các field: routeId, originIata, originName, destIata, destName, distanceKm, durationMinutes, active)
import utils.DBContext;

/**
 * RouteDAO: CRUD cho bảng Route + helper lấy danh sách Airport (IATA)
 */
public class RouteDAO extends DBContext {

    /* ===== DTO nhỏ để đổ ra combobox sân bay ===== */
    public static class AirportOption {
        private String iata;
        private String name;
        private String city;

        public AirportOption(String iata, String name, String city) {
            this.iata = iata;
            this.name = name;
            this.city = city;
        }
        public String getIata() { return iata; }
        public String getName() { return name; }
        public String getCity() { return city; }
    }

    /* ===== Helpers ===== */

    private Integer getAirportIdByIata(Connection conn, String iata) throws SQLException {
        String sql = "SELECT airport_id FROM dbo.Airport WHERE iata_code = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, iata);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
                return null;
            }
        }
    }

    /* ===== Airports for select ===== */
    public List<AirportOption> getAirportOptions() {
        List<AirportOption> list = new ArrayList<>();
        String sql = "SELECT iata_code, name, city FROM dbo.Airport WHERE is_active = 1 ORDER BY iata_code";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new AirportOption(
                        rs.getString("iata_code"),
                        rs.getString("name"),
                        rs.getString("city")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /* ===== List all routes with joined airport info ===== */
    public List<Route> getAllRoutes() {
        List<Route> list = new ArrayList<>();
        String sql =
            "SELECT r.route_id, " +
            "       ao.iata_code AS origin_iata, ao.name AS origin_name, " +
            "       ad.iata_code AS dest_iata,  ad.name AS dest_name, " +
            "       r.distance_km, r.duration_minutes, r.is_active " +
            "FROM dbo.Route r " +
            "JOIN dbo.Airport ao ON ao.airport_id = r.origin_airport_id " +
            "JOIN dbo.Airport ad ON ad.airport_id = r.destination_airport_id " +
            "ORDER BY r.route_id";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Route r = new Route();
                r.setRouteId(rs.getInt("route_id"));
                r.setOriginIata(rs.getString("origin_iata"));
                r.setOriginName(rs.getString("origin_name"));
                r.setDestIata(rs.getString("dest_iata"));
                r.setDestName(rs.getString("dest_name"));
                r.setDistanceKm((Integer) (rs.getObject("distance_km") == null ? null : rs.getInt("distance_km")));
                r.setDurationMinutes((Integer) (rs.getObject("duration_minutes") == null ? null : rs.getInt("duration_minutes")));
                r.setActive(rs.getBoolean("is_active"));
                list.add(r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /* ===== Get route by id ===== */
    public Route getRouteById(int id) {
        String sql =
            "SELECT r.route_id, " +
            "       ao.iata_code AS origin_iata, ao.name AS origin_name, " +
            "       ad.iata_code AS dest_iata,  ad.name AS dest_name, " +
            "       r.distance_km, r.duration_minutes, r.is_active " +
            "FROM dbo.Route r " +
            "JOIN dbo.Airport ao ON ao.airport_id = r.origin_airport_id " +
            "JOIN dbo.Airport ad ON ad.airport_id = r.destination_airport_id " +
            "WHERE r.route_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Route r = new Route();
                    r.setRouteId(rs.getInt("route_id"));
                    r.setOriginIata(rs.getString("origin_iata"));
                    r.setOriginName(rs.getString("origin_name"));
                    r.setDestIata(rs.getString("dest_iata"));
                    r.setDestName(rs.getString("dest_name"));
                    r.setDistanceKm((Integer) (rs.getObject("distance_km") == null ? null : rs.getInt("distance_km")));
                    r.setDurationMinutes((Integer) (rs.getObject("duration_minutes") == null ? null : rs.getInt("duration_minutes")));
                    r.setActive(rs.getBoolean("is_active"));
                    return r;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /* ===== Insert route by IATA ===== */
    public void insertRoute(String originIata, String destIata, Integer distanceKm, Integer durationMinutes, boolean active) throws SQLException {
        if (originIata == null || destIata == null) throw new SQLException("IATA không hợp lệ");
        if (originIata.equalsIgnoreCase(destIata)) throw new SQLException("Origin và Destination phải khác nhau");

        String sql = "INSERT INTO dbo.Route(origin_airport_id, destination_airport_id, distance_km, duration_minutes, is_active) " +
                     "VALUES(?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            Integer o = getAirportIdByIata(conn, originIata);
            Integer d = getAirportIdByIata(conn, destIata);
            if (o == null || d == null) throw new SQLException("Mã IATA không tồn tại trong bảng Airport.");

            ps.setInt(1, o);
            ps.setInt(2, d);
            if (distanceKm == null) ps.setNull(3, Types.INTEGER); else ps.setInt(3, distanceKm);
            if (durationMinutes == null) ps.setNull(4, Types.INTEGER); else ps.setInt(4, durationMinutes);
            ps.setBoolean(5, active);

            ps.executeUpdate();
        }
    }

    /* ===== Update route by IATA ===== */
    public void updateRoute(int routeId, String originIata, String destIata, Integer distanceKm, Integer durationMinutes, boolean active) throws SQLException {
        if (originIata == null || destIata == null) throw new SQLException("IATA không hợp lệ");
        if (originIata.equalsIgnoreCase(destIata)) throw new SQLException("Origin và Destination phải khác nhau");

        String sql = "UPDATE dbo.Route " +
                     "SET origin_airport_id=?, destination_airport_id=?, distance_km=?, duration_minutes=?, is_active=? " +
                     "WHERE route_id=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            Integer o = getAirportIdByIata(conn, originIata);
            Integer d = getAirportIdByIata(conn, destIata);
            if (o == null || d == null) throw new SQLException("Mã IATA không tồn tại trong bảng Airport.");

            ps.setInt(1, o);
            ps.setInt(2, d);
            if (distanceKm == null) ps.setNull(3, Types.INTEGER); else ps.setInt(3, distanceKm);
            if (durationMinutes == null) ps.setNull(4, Types.INTEGER); else ps.setInt(4, durationMinutes);
            ps.setBoolean(5, active);
            ps.setInt(6, routeId);

            ps.executeUpdate();
        }
    }

    /* ===== Check exists when insert ===== */
    public boolean existsRoute(String originIata, String destIata) {
        String sql = "SELECT COUNT(*) " +
                     "FROM dbo.Route r " +
                     "JOIN dbo.Airport ao ON ao.airport_id = r.origin_airport_id " +
                     "JOIN dbo.Airport ad ON ad.airport_id = r.destination_airport_id " +
                     "WHERE ao.iata_code = ? AND ad.iata_code = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, originIata);
            ps.setString(2, destIata);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /* ===== Check exists when update (exclude itself) ===== */
    public boolean existsRouteForUpdate(int routeId, String originIata, String destIata) {
        String sql = "SELECT COUNT(*) " +
                     "FROM dbo.Route r " +
                     "JOIN dbo.Airport ao ON ao.airport_id = r.origin_airport_id " +
                     "JOIN dbo.Airport ad ON ad.airport_id = r.destination_airport_id " +
                     "WHERE ao.iata_code = ? AND ad.iata_code = ? AND r.route_id <> ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, originIata);
            ps.setString(2, destIata);
            ps.setInt(3, routeId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /* ===== Delete route ===== */
    public boolean deleteRoute(int routeId) {
        String sql = "DELETE FROM dbo.Route WHERE route_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, routeId);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            // Nếu có lỗi khóa ngoại (Route đang được dùng trong Flight)
            if (e.getMessage().contains("FK") || e.getMessage().contains("foreign key")) {
                System.err.println("Không thể xóa: Tuyến bay đang được tham chiếu bởi Flight.");
            } else {
                e.printStackTrace();
            }
            return false;
        }
    }
}
