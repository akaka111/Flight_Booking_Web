/*
 * Click nbfs://SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO.Admin;

import model.Airline;
import utils.DBContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object for Airline entity, providing CRUD operations.
 *
 * @author Khoa
 */
public class AirlineDAO {

    private static final Logger LOGGER = Logger.getLogger(AirlineDAO.class.getName());
    private DBContext dbContext;

    public AirlineDAO() {
        dbContext = new DBContext();
    }

    // Helper method to map ResultSet to Airline
    private Airline mapResultSetToAirline(ResultSet rs) throws SQLException {
        return new Airline(
                rs.getInt("airline_id"),
                rs.getString("name"),
                rs.getString("code"),
                rs.getString("description"),
                rs.getString("services")
        );
    }

    // Read all airlines
    public List<Airline> getAllAirlines() throws SQLException {
        List<Airline> airlines = new ArrayList<>();
        String sql = "SELECT * FROM Airline ORDER BY airline_id";
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                airlines.add(mapResultSetToAirline(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving airlines", e);
            throw new SQLException("Failed to retrieve airlines: " + e.getMessage(), e);
        }
        return airlines;
    }

    // Read airline by ID
    public Airline getById(int id) throws SQLException {
        String sql = "SELECT * FROM Airline WHERE airline_id = ?";
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAirline(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving airline by ID", e);
            throw new SQLException("Failed to retrieve airline by ID: " + id + ", " + e.getMessage(), e);
        }
        return null;
    }

    // Insert a new airline
    public boolean insertAirline(String name, String code, String description, String services) throws SQLException {
        if (code == null || code.trim().isEmpty()) return false;

        String checkSql = "SELECT COUNT(*) FROM Airline WHERE code = ?";
        try (Connection conn = dbContext.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setString(1, code);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) return false;
            }
        }

        String sql = "INSERT INTO Airline (name, code, description, services) VALUES (?, ?, ?, ?)";
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, code);
            ps.setString(3, description);
            ps.setString(4, services);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting airline", e);
            throw new SQLException("Failed to insert airline: " + e.getMessage(), e);
        }
    }

    // Update an existing airline
    public boolean updateAirline(Airline airline) throws SQLException {
        if (airline.getCode() == null || airline.getCode().trim().isEmpty()) return false;

        String checkSql = "SELECT COUNT(*) FROM Airline WHERE code = ? AND airline_id != ?";
        try (Connection conn = dbContext.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setString(1, airline.getCode());
            checkStmt.setInt(2, airline.getAirlineId());
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) return false;
            }
        }

        String sql = "UPDATE Airline SET name = ?, code = ?, description = ?, services = ? WHERE airline_id = ?";
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, airline.getName());
            ps.setString(2, airline.getCode());
            ps.setString(3, airline.getDescription());
            ps.setString(4, airline.getServices());
            ps.setInt(5, airline.getAirlineId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating airline", e);
            throw new SQLException("Failed to update airline: " + e.getMessage(), e);
        }
    }

    // Delete an airline and all its related flights
    public boolean deleteAirline(int airlineId) throws SQLException {
        String deleteFlightsSql = "DELETE FROM Flight WHERE airline_id = ?";
        String deleteAirlineSql = "DELETE FROM Airline WHERE airline_id = ?";

        try (Connection conn = dbContext.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement psFlights = conn.prepareStatement(deleteFlightsSql)) {
                psFlights.setInt(1, airlineId);
                psFlights.executeUpdate();
            }

            try (PreparedStatement psAirline = conn.prepareStatement(deleteAirlineSql)) {
                psAirline.setInt(1, airlineId);
                int rows = psAirline.executeUpdate();
                conn.commit();
                return rows > 0;
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting airline and related flights", e);
            throw new SQLException("Failed to delete airline and its flights: " + e.getMessage(), e);
        }
    }
} 