package DAO.Admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.sql.Date;
import model.Voucher;
import utils.DBContext;

public class VoucherDAO {

    DBContext dbContext = new DBContext();

    // Lấy tất cả voucher
    public List<Voucher> getAllVouchers() {
        List<Voucher> list = new ArrayList<>();
        String sql = "SELECT * FROM Voucher";
        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Voucher v = new Voucher(
                        rs.getInt("voucher_id"),
                        rs.getString("code"),
                        rs.getInt("discount_percent"),
                        rs.getBigDecimal("discount_max_amount"),
                        rs.getBigDecimal("min_order_value"),
                        (Integer) rs.getObject("min_people"),
                        rs.getDate("valid_from"),
                        rs.getDate("valid_to"),
                        rs.getInt("usage_limit"),
                        rs.getBoolean("is_active")
                );
                list.add(v);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void updateVoucherStatusIfExpired(Voucher v) {
        if (v.getUsage_limit() <= 0 || (v.getValid_to() != null && v.getValid_to().before(new Date(System.currentTimeMillis())))) {
            v.setIsActive(false);
            updateVoucher(v); // update vào DB
        }
    }

    // Lấy voucher theo ID
    public Voucher getVoucherById(int id) {
        String sql = "SELECT * FROM Voucher WHERE voucher_id = ?";
        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Voucher(
                        rs.getInt("voucher_id"),
                        rs.getString("code"),
                        rs.getInt("discount_percent"),
                        rs.getBigDecimal("discount_max_amount"),
                        rs.getBigDecimal("min_order_value"),
                        (Integer) rs.getObject("min_people"),
                        rs.getDate("valid_from"),
                        rs.getDate("valid_to"),
                        rs.getInt("usage_limit"),
                        rs.getBoolean("is_active")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Lấy voucher theo code
    public Voucher getVoucherByCode(String code) {
        String sql = "SELECT * FROM Voucher WHERE code = ?";
        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, code);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Voucher(
                        rs.getInt("voucher_id"),
                        rs.getString("code"),
                        rs.getInt("discount_percent"),
                        rs.getBigDecimal("discount_max_amount"),
                        rs.getBigDecimal("min_order_value"),
                        (Integer) rs.getObject("min_people"),
                        rs.getDate("valid_from"),
                        rs.getDate("valid_to"),
                        rs.getInt("usage_limit"),
                        rs.getBoolean("is_active")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Thêm voucher mới
    public void insertVoucher(Voucher v) {
        String sql = "INSERT INTO Voucher (code, discount_percent, discount_max_amount, min_order_value, min_people, valid_from, valid_to, usage_limit, is_active) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, v.getCode());
            ps.setInt(2, v.getDiscount_percent());
            ps.setBigDecimal(3, v.getDiscountMaxAmount());
            ps.setBigDecimal(4, v.getMinOrderValue());
            if (v.getMinPeople() != null) {
                ps.setInt(5, v.getMinPeople());
            } else {
                ps.setNull(5, java.sql.Types.INTEGER);
            }
            ps.setDate(6, v.getValid_from());
            ps.setDate(7, v.getValid_to());
            ps.setInt(8, v.getUsage_limit());
            ps.setBoolean(9, v.isIsActive());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Cập nhật voucher
    public void updateVoucher(Voucher v) {
        String sql = "UPDATE Voucher SET code=?, discount_percent=?, discount_max_amount=?, min_order_value=?, min_people=?, valid_from=?, valid_to=?, usage_limit=?, is_active=? WHERE voucher_id=?";
        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, v.getCode());
            ps.setInt(2, v.getDiscount_percent());
            ps.setBigDecimal(3, v.getDiscountMaxAmount());
            ps.setBigDecimal(4, v.getMinOrderValue());
            if (v.getMinPeople() != null) {
                ps.setInt(5, v.getMinPeople());
            } else {
                ps.setNull(5, java.sql.Types.INTEGER);
            }
            ps.setDate(6, v.getValid_from());
            ps.setDate(7, v.getValid_to());
            ps.setInt(8, v.getUsage_limit());
            ps.setBoolean(9, v.isIsActive());
            ps.setInt(10, v.getVoucher_id());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Xóa voucher
    public void deleteVoucher(int id) {
        String sql = "DELETE FROM Voucher WHERE voucher_id = ?";
        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Giảm usage_limit khi voucher được sử dụng
    public void decreaseUsageLimit(int voucherId) {
        String sql = "UPDATE Voucher SET usage_limit = usage_limit - 1 WHERE voucher_id = ? AND usage_limit > 0";
        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, voucherId);
            Voucher v = getVoucherById(voucherId);
            updateVoucherStatusIfExpired(v);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
