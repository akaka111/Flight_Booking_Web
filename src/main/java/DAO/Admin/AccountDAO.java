/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO.Admin;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Account;
import utils.DBContext;

/**
 *
 * @author $ LienXuanThinh - CE182117
 */
public class AccountDAO {

    DBContext dbContext = new DBContext();

    // Xác thực user bằng username và password
    public Account authenticateUser(String username, String password) {
        String sql = "SELECT * FROM Account WHERE username = ? AND password = ?";
        try (Connection conn = dbContext.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, hashSHA256(password));
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToAccount(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Lấy user bằng email
    public Account getUserByEmail(String email) {
        String sql = "SELECT * FROM Account WHERE email = ?";
        try (Connection conn = dbContext.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToAccount(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Lấy vai trò của user
    public String getUserRole(int userId) {
        String sql = "SELECT role FROM Account WHERE user_id = ?";
        try (Connection conn = dbContext.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("role");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "GUEST";
    }

    // Hàm hash password bằng SHA-256
    private static String hashSHA256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] messageDigest = md.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    
     // Tạo user mới từ Google
    public void createUserFromGoogle(String email, String fullName, String imageURL) {
        String sql = "INSERT INTO UserAccount (email, fullName, type, imageUML) VALUES (?, ?, 'google', ?)";
        try ( Connection conn = dbContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, fullName);
            stmt.setString(3, imageURL);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

   

    // Map ResultSet => Account object
    private Account mapResultSetToAccount(ResultSet rs) throws SQLException {
        Account acc = new Account();
        acc.setUserId(rs.getInt("user_id"));
        acc.setUsername(rs.getString("username"));
        acc.setPassword(rs.getString("password"));
        acc.setEmail(rs.getString("email"));
        acc.setPhone(rs.getString("phone"));
        acc.setDob(rs.getDate("dob"));
        acc.setRole(rs.getString("role"));
        acc.setStatus(rs.getBoolean("status"));
        return acc;
    }

    // Demo hash password
    public static void main(String[] args) {
        String password = "123";
        System.out.println("Hash SHA-256 của password: " + hashSHA256(password));
    }
}
