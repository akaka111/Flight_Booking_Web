/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO.Admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.BookingVoucher;
import utils.DBContext;

/**
 *
 * @author $ LienXuanThinh - CE182117
 */
public class BookingVoucherDAO {

    public BookingVoucherDAO() {
        try {
            this.conn = new DBContext().getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Connection conn;

    // Constructor để nhận kết nối từ servlet
    public BookingVoucherDAO(Connection conn) {
        this.conn = conn;
    }

    public void insertBookingVoucher(BookingVoucher bv) {
        String sql = "INSERT INTO BookingVoucher (booking_id, voucher_id) VALUES (?, ?)";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bv.getBookingId());
            ps.setInt(2, bv.getVoucherId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isVoucherApplied(int bookingId, int voucherId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM BookingVoucher WHERE booking_id = ? AND voucher_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            ps.setInt(2, voucherId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

}
