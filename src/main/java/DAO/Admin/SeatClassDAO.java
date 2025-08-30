package DAO.Admin;

import model.SeatClass;
import utils.DBContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**    
 * 
 * DAO class to handle CRUD operations for SeatClass.
 */
public class SeatClassDAO extends DBContext {

    // Lấy tất cả lớp ghế
    public List<SeatClass> getAllSeatClasses() {
        List<SeatClass> seatClasses = new ArrayList<>();
        String sql = "SELECT * FROM SeatClass";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                SeatClass sc = new SeatClass();
                sc.setSeatClassID(rs.getInt("SeatClassID"));
                sc.setName(rs.getString("Name"));
                sc.setDescription(rs.getString("Description"));
                sc.setStatus(rs.getString("Status"));
                seatClasses.add(sc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seatClasses;
    }

    // Lấy lớp ghế theo ID
    public SeatClass getSeatClassById(int seatClassId) {
        SeatClass seatClass = null;
        String sql = "SELECT * FROM SeatClass WHERE SeatClassID = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, seatClassId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    seatClass = new SeatClass();
                    seatClass.setSeatClassID(rs.getInt("SeatClassID"));
                    seatClass.setName(rs.getString("Name"));
                    seatClass.setDescription(rs.getString("Description"));
                    seatClass.setStatus(rs.getString("Status"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seatClass;
    }

    public boolean insertSeatClass(SeatClass sc) {
        String sql = "INSERT INTO SeatClass (Name, Description, Status) VALUES (?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sc.getName());
            ps.setString(2, sc.getDescription());
            ps.setString(3, sc.getStatus());
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0; // Return true if at least one row was inserted
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false on error
        }
    }

    // Update an existing seat class
    public boolean updateSeatClass(SeatClass sc) {
        String sql = "UPDATE SeatClass SET Name = ?, Description = ?, Status = ? WHERE SeatClassID = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sc.getName());
            ps.setString(2, sc.getDescription());
            ps.setString(3, sc.getStatus());
            ps.setInt(4, sc.getSeatClassID());
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0; // Return true if at least one row was updated
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false on error
        }
    }

    // Delete a seat class
    public boolean deleteSeatClass(int seatClassId) {
        String sql = "DELETE FROM SeatClass WHERE SeatClassID = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, seatClassId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0; // Return true if at least one row was deleted
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false on error
        }
    }

    // Xóa lớp ghế
    public void deleteSeatClasss(int seatClassId) {
        String sql = "DELETE FROM SeatClass WHERE SeatClassID = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, seatClassId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
