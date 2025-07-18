/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller.admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utils.DBContext;

/**
 *
 * @author ADMIN
 */
public class StatisticPerMonth {

    DBContext dbconnect = new DBContext();
    
    public int countUsersInMonthYear(int m, int y) {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM [User] WHERE MONTH(created_at) = ? AND YEAR(created_at) = ?";
        try (Connection conn = dbconnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, m);
            ps.setInt(2, y);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public int getCompletedFlights() {
        String sql = "SELECT COUNT(*) AS CompletedFlights FROM Flight WHERE status = 'ON TIME';";
        try (Connection conn = dbconnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getCancelFlights() {
        String sql = "SELECT COUNT(*) AS CompletedFlights FROM Flight WHERE status = 'CANCELLED';";
        try (Connection conn = dbconnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getDelayFlights() {
        String sql = "SELECT COUNT(*) AS CompletedFlights FROM Flight WHERE status = 'DELAYED';";
        try (Connection conn = dbconnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public double getRevenueMonthYear(int m, int y) {
        String sql = """
        SELECT SUM(f.price)
        FROM Booking b
        JOIN Flight f ON b.flight_id = f.flight_id
        WHERE b.status = 'CONFIRMED'
          AND MONTH(b.booking_time) = ?
          AND YEAR(b.booking_time) = ?
    """;
        try (Connection conn = dbconnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, m);
            ps.setInt(2, y);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public List<String> getCustomerList() {
        List<String> customers = new ArrayList<>();
        String sql = " SELECT DISTINCT u.username  FROM [User] u JOIN Booking b ON u.user_id = b.user_id  WHERE b.status = 'CONFIRMED'";
        try (Connection conn = dbconnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                customers.add(rs.getString("username"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customers;
    }

    public int countTicketsSold(int m, int y) {
        String sql = "SELECT COUNT(*) FROM Booking WHERE status = 'CONFIRMED' AND MONTH(booking_time) = ? AND YEAR(booking_time) = ?";
        try (Connection conn = dbconnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, m);
            ps.setInt(2, y);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
