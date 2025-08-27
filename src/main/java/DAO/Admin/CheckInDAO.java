/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO.Admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import model.CheckIn;
import utils.DBContext;

/**
 *
 * @author $ LienXuanThinh - CE182117
 */
public class CheckInDAO extends DBContext {

    public void insertCheckin(CheckIn checkIn) {
        String sql = "INSERT INTO CheckIn (passenger_id, booking_id, flight_id, checkin_time, status, user_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, checkIn.getPassengerId());
            ps.setInt(2, checkIn.getBookingId());
            ps.setInt(3, checkIn.getFlightId());
            ps.setTimestamp(4, (checkIn.getCheckinTime()));
            ps.setString(5, checkIn.getStatus());
            
                ps.setInt(6, checkIn.getUserId());
            
                ps.setNull(6, Types.INTEGER);
            
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
