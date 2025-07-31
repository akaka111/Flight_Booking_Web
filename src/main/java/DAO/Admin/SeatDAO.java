/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO.Admin;

import model.Seat;
import utils.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author $ LienXuanThinh - CE182117
 */
public class SeatDAO extends DBContext {

    public List<Seat> getSeatsByFlightId(int flightId) {
        List<Seat> list = new ArrayList<>();
        DBContext db = new DBContext();
        String sql = "SELECT * FROM Seat WHERE flight_id = ?";
        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            ps.setInt(1, flightId);
            while (rs.next()) {
                Seat seat = new Seat(
                        rs.getInt("seat_id"),
                        rs.getInt("flight_id"),
                        rs.getInt("class_id"),
                        rs.getString("seat_number"),
                        rs.getBoolean("is_booked")
                );
                list.add(seat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void updateSeatBooking(int seatId, boolean isBooked) {
        DBContext db = new DBContext();
        String sql = "UPDATE Seat SET is_booked = ? WHERE seat_id = ?";
        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            ps.setBoolean(1, isBooked);
            ps.setInt(2, seatId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
