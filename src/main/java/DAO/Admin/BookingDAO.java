/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO.Admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import model.Booking;

/**
 *
 * @author $ LienXuanThinh - CE182117
 */
public class BookingDAO {

    private Connection conn;

    // Constructor để nhận kết nối từ servlet
    public BookingDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Phương thức này thêm một bản ghi Booking mới vào cơ sở dữ liệu và trả về
     * booking_id (khóa chính tự động tăng) của bản ghi vừa được tạo.
     *
     * @param booking Đối tượng Booking chứa thông tin cần lưu.
     * @return ID của booking vừa được tạo, hoặc -1 nếu có lỗi.
     */
    public int createBooking(Booking booking) {
        int generatedId = -1;
        String sql = "INSERT INTO Booking (user_id, flight_id, booking_time, seat_class, total_price, status) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, booking.getUserId());
            ps.setInt(2, booking.getFlightId());
            ps.setTimestamp(3, booking.getBookingDate());
            ps.setString(4, booking.getSeatClass());
            ps.setDouble(5, booking.getTotalPrice());
            ps.setString(6, booking.getStatus());

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedId = rs.getInt(1);
                        System.out.println("==> Booking ID được tạo: " + generatedId);
                    }
                }
            } else {
                System.err.println("==> Không có dòng nào được thêm vào bảng Booking.");
            }

        } catch (SQLException e) {
            System.err.println("==> SQLException khi tạo booking: " + e.getMessage());
            e.printStackTrace();  // CỰC KỲ QUAN TRỌNG
        }

        return generatedId;
    }

    // Trong file BookingDAO.java
// Phương thức để lấy một booking từ DB bằng ID
    public Booking getBookingById(int bookingId) {
        Booking booking = null;
        String sql = "SELECT * FROM dbo.Booking WHERE booking_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    booking = new Booking();
                    booking.setBookingId(rs.getInt("booking_id"));
                    booking.setUserId(rs.getInt("user_id"));
                    // ... lấy các cột khác ...
                    booking.setSeatClass(rs.getString("seat_class"));
                    booking.setTotalPrice(rs.getDouble("total_price"));
                    booking.setStatus(rs.getString("CONFIRMED"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return booking;
    }

// Phương thức để cập nhật trạng thái của một booking
    public boolean updateBookingStatus(int bookingId, String newStatus) {
        String sql = "UPDATE dbo.Booking SET status = ? WHERE booking_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, bookingId);

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0; // Trả về true nếu có ít nhất 1 dòng được cập nhật
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
