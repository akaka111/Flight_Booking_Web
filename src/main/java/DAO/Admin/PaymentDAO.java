 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO.Admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import model.Payment;
import utils.DBContext;

/**
 *
 * @author $ LienXuanThinh - CE182117
 */
public class PaymentDAO {

    private Connection conn;

    // Constructor không tham số
    public PaymentDAO() {
        try {
            this.conn = new DBContext().getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Constructor có tham số (nếu cần dùng trong transaction)
    public PaymentDAO(Connection conn) {
        this.conn = conn;
    }

    DBContext db = new DBContext();

    public void insert(Payment payment) {
        String sql = "INSERT INTO Payment (booking_id, amount, payment_method, payment_time, transaction_id, status, payment_note) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            System.out.println(">>> Connected to DB: " + conn.getCatalog()); // In ra tên DB thật

            ps.setInt(1, payment.getBookingId());
            ps.setBigDecimal(2, payment.getAmount());
            ps.setString(3, payment.getPaymentMethod());
            ps.setTimestamp(4, Timestamp.valueOf(payment.getPaymentTime()));
            ps.setString(5, payment.getTransactionId());
            ps.setString(6, payment.getStatus());
            ps.setString(7, payment.getPaymentNote());

            int rows = ps.executeUpdate();
            System.out.println(">>> Inserted rows: " + rows);

            if (rows == 0) {
                System.out.println(">>> KHÔNG có dòng nào được thêm vào DB! Hãy kiểm tra dữ liệu hoặc DB.");
            }

        } catch (SQLException e) {
            System.out.println(">>> SQL Error Code: " + e.getErrorCode());
            System.out.println(">>> SQL State: " + e.getSQLState());
            e.printStackTrace();
        }
    }

    public boolean checkPaymentExistsByBookingId(int bookingId) {
        String sql = "SELECT COUNT(*) FROM Payment WHERE booking_id = ? AND status = 'Success'";
        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Nếu count > 0, nghĩa là đã tồn tại
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Trả về false nếu có lỗi hoặc không tìm thấy
        return false;
    }

    public boolean existsByTransactionId(String transactionId) {
        String query = "SELECT COUNT(*) FROM Payment WHERE transaction_id = ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, transactionId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

   
}
