package DAO.Admin;

import model.AircraftType;
import model.Airline;
import utils.DBContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AircraftTypeDAO extends DBContext {
    // Lấy danh sách tất cả các loại máy bay (giữ nguyên)
    public List<AircraftType> getAllAircraftTypes() {
        List<AircraftType> aircraftTypes = new ArrayList<>();
        String sql = "SELECT at.AircraftTypeID, at.AircraftTypeCode, at.AircraftTypeName, at.Status, at.AirlineID, "
                + "a.name AS airline_name, a.code AS airline_code "
                + "FROM AircraftType at "
                + "JOIN Airline a ON at.AirlineID = a.airline_id "
                + "WHERE at.Status = 'Active'";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                AircraftType aircraftType = new AircraftType();
                aircraftType.setAircraftTypeId(rs.getInt("AircraftTypeID"));
                aircraftType.setAircraftTypeCode(rs.getString("AircraftTypeCode"));
                aircraftType.setAircraftTypeName(rs.getString("AircraftTypeName"));
                aircraftType.setStatus(rs.getString("Status"));
                Airline airline = new Airline();
                airline.setAirlineId(rs.getInt("AirlineID"));
                airline.setName(rs.getString("airline_name"));
                airline.setCode(rs.getString("airline_code"));
                aircraftType.setAirlineId(airline);
                aircraftTypes.add(aircraftType);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return aircraftTypes;
    }

    // Lấy thông tin loại máy bay theo ID (giữ nguyên)
    public AircraftType getAircraftTypeById(int aircraftTypeId) {
        AircraftType aircraftType = null;
        String sql = "SELECT at.AircraftTypeID, at.AircraftTypeCode, at.AircraftTypeName, at.Status, at.AirlineID, "
                + "a.name AS airline_name, a.code AS airline_code "
                + "FROM AircraftType at "
                + "JOIN Airline a ON at.AirlineID = a.airline_id "
                + "WHERE at.AircraftTypeID = ? AND at.Status = 'Active'";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, aircraftTypeId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    aircraftType = new AircraftType();
                    aircraftType.setAircraftTypeId(rs.getInt("AircraftTypeID"));
                    aircraftType.setAircraftTypeCode(rs.getString("AircraftTypeCode"));
                    aircraftType.setAircraftTypeName(rs.getString("AircraftTypeName"));
                    aircraftType.setStatus(rs.getString("Status"));
                    Airline airline = new Airline();
                    airline.setAirlineId(rs.getInt("AirlineID"));
                    airline.setName(rs.getString("airline_name"));
                    airline.setCode(rs.getString("airline_code"));
                    aircraftType.setAirlineId(airline);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return aircraftType;
    }

    // Thêm mới loại máy bay và trả về ID mới
    public int insertAircraftType(AircraftType aircraftType) {
        String sql = "INSERT INTO AircraftType (AircraftTypeCode, AircraftTypeName, Status, AirlineID) "
                + "VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, aircraftType.getAircraftTypeCode());
            ps.setString(2, aircraftType.getAircraftTypeName());
            ps.setString(3, aircraftType.getStatus());
            ps.setInt(4, aircraftType.getAirlineId().getAirlineId());
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1); // Trả về ID mới
                    }
                }
            }
            return -1; // Thất bại
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    // Cập nhật thông tin loại máy bay và trả về boolean
    public boolean updateAircraftType(AircraftType aircraftType) {
        String sql = "UPDATE AircraftType SET AircraftTypeCode = ?, AircraftTypeName = ?, Status = ?, AirlineID = ? "
                + "WHERE AircraftTypeID = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, aircraftType.getAircraftTypeCode());
            ps.setString(2, aircraftType.getAircraftTypeName());
            ps.setString(3, aircraftType.getStatus());
            ps.setInt(4, aircraftType.getAirlineId().getAirlineId());
            ps.setInt(5, aircraftType.getAircraftTypeId());
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa loại máy bay và trả về boolean
    public boolean deleteAircraftType(int aircraftTypeId) {
        String sql = "DELETE FROM AircraftType WHERE AircraftTypeID = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, aircraftTypeId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Kiểm tra xem loại máy bay có đang được sử dụng không
    public boolean isAircraftTypeInUse(int aircraftTypeId) {
        String sql = "SELECT COUNT(*) FROM Flight WHERE AircraftTypeID = ? "
                + "UNION ALL "
                + "SELECT COUNT(*) FROM AircraftSeatConfig WHERE AircraftTypeID = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, aircraftTypeId);
            ps.setInt(2, aircraftTypeId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    if (rs.getInt(1) > 0) {
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}