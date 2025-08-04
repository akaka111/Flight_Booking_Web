package DAO.Admin;

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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AccountDAO {

    private static final Logger logger = LogManager.getLogger(AccountDAO.class);
    private final DBContext dbContext = new DBContext();

    // Xác thực user bằng username và password
    public Account authenticateUser(String username, String password) {
        String sql = "SELECT * FROM Account WHERE username = ? AND password = ?";
        try (Connection conn = dbContext.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, hashSHA256(password));
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Account account = mapResultSetToAccount(rs);
                logger.info("Authenticated user - userId: {}, username: {}, role: {}", account.getUserId(), account.getUsername(), account.getRole());
                return account;
            } else {
                logger.warn("Authentication failed for username: {}", username);
            }

        } catch (SQLException e) {
            logger.error("SQL error in authenticateUser: ", e);
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
                Account account = mapResultSetToAccount(rs);
                logger.info("Found user by email - userId: {}, email: {}, role: {}", account.getUserId(), email, account.getRole());
                return account;
            } else {
                logger.warn("No user found for email: {}", email);
            }

        } catch (SQLException e) {
            logger.error("SQL error in getUserByEmail: ", e);
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
            logger.error("SQL error in getUserRole: ", e);
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
        int userId = rs.getInt("user_id");
        acc.setUserId(rs.getInt("user_id"));
        acc.setUsername(rs.getString("username"));
        acc.setPassword(rs.getString("password"));
        acc.setEmail(rs.getString("email"));
        acc.setPhone(rs.getString("phone"));
        acc.setDob(rs.getDate("dob"));
        acc.setRole(rs.getString("role"));
        acc.setStatus(rs.getBoolean("status"));
        String fullname = rs.getString("fullname");
        acc.setFullname(fullname != null ? fullname.trim().replaceAll("\\s+", "_") : "Unknown_Staff");
        logger.info("Mapped account - userId: {}, username: {}, fullname: {}, role: {}", userId, acc.getUsername(), acc.getFullname(), acc.getRole());

        if (userId <= 0) {
            logger.warn("Invalid userId detected: {}", userId);
        }
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
            stmt.setString(2, hashSHA256(user.getPassword()));
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPhone());
            stmt.setDate(5, user.getDob());
            stmt.setString(6, "CUSTOMER");
            stmt.setBoolean(7, user.isStatus());
            stmt.setString(8, user.getFullname());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            logger.error("SQL error in registerUser: ", e);
        }
        return false;
    }

    // Kiểm tra user có tồn tại không (trùng username hoặc email)
    public boolean isUserExist(String username, String email) {
        String sql = "SELECT user_id FROM Account WHERE LOWER(username) = LOWER(?) OR LOWER(email) = LOWER(?)";
        try (Connection conn = dbContext.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username.toLowerCase());
            stmt.setString(2, email.toLowerCase());
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            logger.error("SQL error in isUserExist: ", e);
        }
        return false;
    }

    public List<Account> getAllAccounts() {
        List<Account> list = new ArrayList<>();
        String sql = "SELECT * FROM Account";
        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSetToAccount(rs));
            }

        } catch (SQLException e) {
            logger.error("SQL error in getAllAccounts: ", e);
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
            logger.error("SQL error in getAccountById: ", e);
        }
        return null;
    }

    public boolean addAccountByAdmin(Account user) {
        String sql = "INSERT INTO Account (username, password, email, phone, role, status, fullname, dob) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, hashSHA256(user.getPassword()));
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPhone());
            ps.setString(5, user.getRole());
            ps.setBoolean(6, user.isStatus());
            ps.setString(7, user.getFullname());
            ps.setDate(8, user.getDob());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("SQL error in addAccountByAdmin: ", e);
        }
        return false;
    }

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
            logger.error("SQL error in updateAccountByAdmin: ", e);
        }
        return false;
    }

    public boolean deleteAccount(int userId) {
        String sql = "DELETE FROM Account WHERE user_id = ?";
        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("SQL error in deleteAccount: ", e);
        }
        return false;
    }

    public boolean checkOldPassword(String username, String oldPassword) {
        String sql = "SELECT * FROM Account WHERE username = ? AND password = ? AND role = 'ADMIN'";
        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, hashSHA256(oldPassword));
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            logger.error("SQL error in checkOldPassword: ", e);
            return false;
        }
    }

    public boolean updatePassword(String username, String newPassword) {
        String sql = "UPDATE Account SET password = ? WHERE username = ? AND role = 'ADMIN'";
        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hashSHA256(newPassword));
            ps.setString(2, username);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            logger.error("SQL error in updatePassword: ", e);
            return false;
        }
    }
}