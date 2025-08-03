/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO.Admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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

    public int insertPassengerAndReturnId(Passenger passenger) throws SQLException {
        int generatedId = -1;
        String sql = "INSERT INTO Passenger (booking_id, full_name, passport_number, dob, gender, phone_number, email, country, address) "
                + "OUTPUT INSERTED.passenger_id VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, passenger.getBookingId());
            ps.setString(2, passenger.getFullName());
            ps.setString(3, passenger.getPassportNumber());
            ps.setDate(4, passenger.getDob());
            ps.setString(5, passenger.getGender());
            ps.setString(6, passenger.getPhoneNumber());
            ps.setString(7, passenger.getEmail());
            ps.setString(8, passenger.getCountry());
            ps.setString(9, passenger.getAddress());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedId = rs.getInt(1);
                    }
                }
            }
        }
        return generatedId;
    }

    public Passenger getPassengerByBookingId(int bookingId) {
        Passenger passenger = null;
        String sql = "SELECT * FROM Passenger WHERE booking_id = ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                passenger = new Passenger();
                passenger.setPassengerId(rs.getInt("passenger_id"));
                passenger.setBookingId(rs.getInt("booking_id"));
                passenger.setFullName(rs.getString("full_name"));
                passenger.setDob(rs.getDate("dob"));
                passenger.setGender(rs.getString("gender"));
                passenger.setPassportNumber(rs.getString("passport_number"));
                passenger.setPhoneNumber(rs.getString("phone_number"));
                passenger.setEmail(rs.getString("email"));
                passenger.setCountry(rs.getString("country"));
                passenger.setAddress(rs.getString("address"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return passenger;
    }

    public List<Passenger> getPassengersByBookingId(int bookingId) {
        List<Passenger> list = new ArrayList<>();
        String sql = "SELECT * FROM Passenger WHERE booking_id = ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Passenger p = new Passenger();
                p.setPassengerId(rs.getInt("passenger_id"));
                p.setBookingId(rs.getInt("booking_id"));
                p.setFullName(rs.getString("fullname"));
                p.setGender(rs.getString("gender"));
                p.setDob(rs.getDate("dob"));
                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

}
