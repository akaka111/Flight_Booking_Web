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
import model.Voucher;
import utils.DBContext;

/**
 *
 * @author ADMIN
 */
public class voucherDAO {

    DBContext dbContext = new DBContext();

    public List<Voucher> getAllVouchers() {
        List<Voucher> list = new ArrayList<>();
        String sql = "SELECT * FROM Voucher";
        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Voucher(
                        rs.getInt("voucher_id"),
                        rs.getString("code"),
                        rs.getInt("discount_percent"),
                        rs.getString("valid_from"),
                        rs.getString("valid_to"),
                        rs.getInt("usage_limit"),
                        rs.getInt("is_active")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void insertVoucher(Voucher v) {
        String sql = "INSERT INTO Voucher (code, discount_percent, valid_from, valid_to, usage_limit) VALUES (?, ?, ?, ?, ? )";
        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, v.getCode());
            ps.setInt(2, v.getDiscount_percent());
            ps.setString(3, v.getValid_from());
            ps.setString(4, v.getValid_to());
            ps.setInt(5, v.getUsage_limit());
//            ps.setInt(6, v.getIs_active());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateVoucher(Voucher v) {
        String sql = "UPDATE Voucher SET code=?, discount_percent=?, valid_from=?, valid_to=?, usage_limit=?, is_active=? WHERE voucher_id=?";
        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, v.getCode());
            ps.setInt(2, v.getDiscount_percent());
            ps.setString(3, v.getValid_from());
            ps.setString(4, v.getValid_to());
            ps.setInt(5, v.getUsage_limit());
            ps.setInt(6, v.getIs_active());
            ps.setInt(7, v.getVoucher_id());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteVoucher(int id) {
        String sql = "DELETE FROM Voucher WHERE voucher_id = ?";
        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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
                        rs.getString("valid_from"),
                        rs.getString("valid_to"),
                        rs.getInt("usage_limit"),
                        rs.getInt("is_active")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
