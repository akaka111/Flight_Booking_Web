package DAO.Admin;

import model.Airport;
import utils.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AirportDAO extends DBContext {

    public List<Airport> getAllAirports() {
        List<Airport> list = new ArrayList<>();
        String sql = "SELECT airport_id, iata_code, icao_code, name, city, country, timezone, is_active " +
                     "FROM Airport ORDER BY airport_id";
        try (Connection cn = getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Airport a = map(rs);
                list.add(a);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Airport getById(int id) {
        String sql = "SELECT airport_id, iata_code, icao_code, name, city, country, timezone, is_active " +
                     "FROM Airport WHERE airport_id=?";
        try (Connection cn = getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean existsIata(String iata) {
        String sql = "SELECT 1 FROM Airport WHERE iata_code = ?";
        try (Connection cn = getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, iata);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    public void insert(Airport a) {
        String sql = "INSERT INTO Airport (iata_code, icao_code, name, city, country, timezone, is_active) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection cn = getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, a.getIataCode());
            ps.setString(2, a.getIcaoCode());
            ps.setString(3, a.getName());
            ps.setString(4, a.getCity());
            ps.setString(5, a.getCountry());
            ps.setString(6, a.getTimezone());
            ps.setBoolean(7, a.isActive());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(Airport a) {
        String sql = "UPDATE Airport SET iata_code=?, icao_code=?, name=?, city=?, country=?, timezone=?, is_active=? " +
                     "WHERE airport_id=?";
        try (Connection cn = getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, a.getIataCode());
            ps.setString(2, a.getIcaoCode());
            ps.setString(3, a.getName());
            ps.setString(4, a.getCity());
            ps.setString(5, a.getCountry());
            ps.setString(6, a.getTimezone());
            ps.setBoolean(7, a.isActive());
            ps.setInt(8, a.getAirportId());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM Airport WHERE airport_id=?";
        try (Connection cn = getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            // Có thể dính FK từ Route -> Airport, nên sẽ lỗi nếu đang được dùng
            e.printStackTrace();
            throw new RuntimeException("Không thể xóa do đang được sử dụng trong Route/Flight.");
        }
    }

    private Airport map(ResultSet rs) throws SQLException {
        Airport a = new Airport();
        a.setAirportId(rs.getInt("airport_id"));
        a.setIataCode(rs.getString("iata_code"));
        a.setIcaoCode(rs.getString("icao_code"));
        a.setName(rs.getString("name"));
        a.setCity(rs.getString("city"));
        a.setCountry(rs.getString("country"));
        a.setTimezone(rs.getString("timezone"));
        a.setActive(rs.getBoolean("is_active"));
        return a;
    }
}
