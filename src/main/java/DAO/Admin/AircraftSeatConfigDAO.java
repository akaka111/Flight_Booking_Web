package DAO.Admin;

import model.AircraftSeatConfig;
import utils.DBContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AircraftSeatConfigDAO extends DBContext {
    public void insertAircraftSeatConfig(AircraftSeatConfig config) {
        String sql = "INSERT INTO AircraftSeatConfig (AircraftTypeID, SeatClassID, SeatCount) VALUES (?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, config.getAircraftTypeID());
            ps.setInt(2, config.getSeatClassID());
            ps.setInt(3, config.getSeatCount());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<AircraftSeatConfig> getSeatConfigsByAircraftTypeID(int aircraftTypeID) {
        List<AircraftSeatConfig> configs = new ArrayList<>();
        String sql = "SELECT ConfigID, AircraftTypeID, SeatClassID, SeatCount FROM AircraftSeatConfig WHERE AircraftTypeID = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, aircraftTypeID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    AircraftSeatConfig config = new AircraftSeatConfig();
                    config.setConfigID(rs.getInt("ConfigID"));
                    config.setAircraftTypeID(rs.getInt("AircraftTypeID"));
                    config.setSeatClassID(rs.getInt("SeatClassID"));
                    config.setSeatCount(rs.getInt("SeatCount"));
                    configs.add(config);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return configs;
    }

    public void deleteByAircraftTypeID(int aircraftTypeID) {
        String sql = "DELETE FROM AircraftSeatConfig WHERE AircraftTypeID = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, aircraftTypeID);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}