/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO.Admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Passenger;
import utils.DBContext;

/**
 *
 * @author $ LienXuanThinh - CE182117
 */
public class PassengerDAO {

    private Connection conn;

    // Thêm constructor không tham số để tự động lấy kết nối
    public PassengerDAO() {
        try {
            // Giả sử bạn có một lớp DBContext để lấy kết nối
            this.conn = new DBContext().getConnection();
        } catch (Exception ex) {
            Logger.getLogger(PassengerDAO.class.getName()).log(Level.SEVERE, "Không thể kết nối đến cơ sở dữ liệu.", ex);
        }
    }

    // Giữ lại constructor này nếu bạn muốn sử dụng cho các mục đích khác (ví dụ: testing)
    public PassengerDAO(Connection conn) {
        this.conn = conn;
    }

    public void addPassenger(Passenger passenger) throws SQLException {
        String sql = "INSERT INTO dbo.Passenger (booking_id, full_name, passport_number, phone_number, dob, gender, email, country, address) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, passenger.getBookingId());
        ps.setString(2, passenger.getFullName());
        ps.setString(3, passenger.getPassportNumber());
        ps.setString(4, passenger.getPhoneNumber());
        ps.setDate(5, passenger.getDob() != null ? new java.sql.Date(passenger.getDob().getTime()) : null);
        ps.setString(6, passenger.getGender());
        ps.setString(7, passenger.getEmail());
        ps.setString(8, passenger.getCountry());
        ps.setString(9, passenger.getAddress());
        ps.executeUpdate();
    }
}
