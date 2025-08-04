/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO.staff;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
    public static String hashSHA256(String input) {
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
        acc.setFullname(rs.getString("fullname"));

        return acc;
    }

    // Demo hash password
    public static void main(String[] args) {
        String password = "123";
        System.out.println("Hash SHA-256 của password: " + hashSHA256(password));
    }

    public boolean registerUser(Account user) {
        String sql = "INSERT INTO Account (username, password, email, phone, dob, role, status, fullname) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dbContext.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, hashSHA256(user.getPassword())); // Hash mật khẩu
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPhone());
            stmt.setDate(5, user.getDob()); // đảm bảo user.getDob() không null
            stmt.setString(6, "CUSTOMER");  // hoặc user.getRole()
            stmt.setBoolean(7, user.isStatus()); // boolean đúng kiểu
            stmt.setString(8, user.getFullname());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Kiểm tra user có tồn tại không (trùng username hoặc email)
    public boolean isUserExist(String username, String email) {
        // Sửa lại đúng bảng Account
        String sql = "SELECT user_id FROM Account WHERE LOWER(username) = LOWER(?) OR LOWER(email) = LOWER(?)";
        try (Connection conn = dbContext.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username.toLowerCase());
            stmt.setString(2, email.toLowerCase());
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Account> getAllAccounts() {
        List<Account> list = new ArrayList<>();
        String sql = "SELECT * FROM Account";

        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                // Tái sử dụng phương thức mapResultSetToAccount để tránh lỗi và lặp code
                list.add(mapResultSetToAccount(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Account getAccountById(int userId) {
        String sql = "SELECT * FROM Account WHERE user_id = ?";
        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToAccount(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean addAccountByAdmin(Account user) {
        String sql = "INSERT INTO Account (username, password, email, phone, role, status, fullname, dob) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, hashSHA256(user.getPassword())); // Hash mật khẩu
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPhone());
            ps.setString(5, user.getRole());
            ps.setBoolean(6, user.isStatus());
            ps.setString(7, user.getFullname());
            ps.setDate(8, user.getDob());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Cập nhật thông tin tài khoản (không cập nhật mật khẩu)
     *
     * @param user đối tượng Account chứa thông tin cần cập nhật
     * @return true nếu thành công, false nếu thất bại
     */
    public boolean updateAccountByAdmin(Account user) {
        String sql = "UPDATE Account SET fullname = ?, email = ?, phone = ?, role = ?, status = ?, dob = ? WHERE user_id = ?";
        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getFullname());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhone());
            ps.setString(4, user.getRole());
            ps.setBoolean(5, user.isStatus());
            ps.setDate(6, user.getDob());
            ps.setInt(7, user.getUserId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Xóa một tài khoản khỏi database
     *
     * @param userId ID của người dùng cần xóa
     * @return true nếu thành công, false nếu thất bại
     */
    public boolean deleteAccount(int userId) {
        String sql = "DELETE FROM Account WHERE user_id = ?";
        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * đổi mật khẩu admin
     *
     * @param username
     * @param oldPassword
     * @return
     */
    public boolean checkOldPassword(String username, String oldPassword) {
        String sql = "SELECT * FROM Account WHERE username = ? AND password = ? AND role = 'staff'";
        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, oldPassword);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // Trả về true nếu tồn tại username + oldPassword
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePassword(String username, String NewPwd) {
        String sql = "UPDATE Account SET password = ? WHERE username = ? AND role = 'staff'";
        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, NewPwd);
            ps.setString(2, username);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<Account> getAllCustomers() {
        int count = 0;
        List<Account> list = new ArrayList<>();
        String sql = "SELECT * FROM Account WHERE role = 'CUSTOMER'";
        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (conn == null) {
                System.out.println("Lỗi: Không thể kết nối DB!");
            } else {
                System.out.println("ok ket noi");
            }
            while (rs.next()) {
                count++;
                Account acc = new Account();
                acc.setUserId(rs.getInt("user_id"));
                acc.setUsername(rs.getString("username"));
                acc.setEmail(rs.getString("email"));
                acc.setPhone(rs.getString("phone"));
                acc.setDob(rs.getDate("dob"));
                acc.setRole(rs.getString("role"));
                acc.setStatus(rs.getBoolean("status"));
                acc.setFullname(rs.getString("fullname"));
                list.add(acc);
            }
            System.out.println("Tổng số khách hàng tìm được: " + count);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void updateCustomerPassword(int userId, String newPassword) {
        String sql = "UPDATE Account SET password = ? WHERE user_Id = ? and role = 'customer'";
        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newPassword);
            ps.setInt(2, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
