/*
     * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
     * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import model.Account;
import model.Message;
import utils.DBContext;

/**
 *
 * @author lgbaoce180780
 */
public class SupportContact {

    DBContext dbconnect = new DBContext();

    // ==================== SUPPORT MAIL ====================
    public List<Message> getSupportMails(String recipientEmail) throws SQLException {
        List<Message> list = new ArrayList<>();
        String sql = "SELECT * FROM Message WHERE message_type = 'SUPPORT' AND recipient_email = ? ORDER BY sent_time ASC";
        try (Connection conn = dbconnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, recipientEmail);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Message mail = new Message();
                mail.setId(rs.getInt("id"));
                mail.setSenderEmail(rs.getString("sender_email"));
                mail.setRecipientEmail(rs.getString("recipient_email"));
                mail.setSubject(rs.getString("subject"));
                mail.setContent(rs.getString("content"));
                mail.setSentTime(rs.getTimestamp("sent_time"));
                mail.setIsRead(rs.getBoolean("is_read"));
                list.add(mail);
            }
        }
        return list;
    }

    public boolean insertSupportMail(Message mail) {
        String sql = "INSERT INTO Message (sender_email, recipient_email, subject, content, message_type) "
                + "VALUES (?, ?, ?, ?, 'SUPPORT')";
        try (Connection conn = dbconnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, mail.getSenderEmail());
            ps.setString(2, mail.getRecipientEmail());
            ps.setString(3, mail.getSubject());
            ps.setString(4, mail.getContent());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<String> getAllUserEmails() throws SQLException {
        List<String> emails = new ArrayList<>();
        String sql = "SELECT DISTINCT sender_email FROM Message WHERE message_type = 'CHAT' AND sender_email IS NOT NULL";
        try (Connection conn = dbconnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                emails.add(rs.getString("sender_email"));
            }
        }
        return emails;
    }

    public Integer findAssignedStaff(Integer userId, String guestLabel) {
        Integer staffId = null;
        String sql = "SELECT TOP 1 recipient_id "
                + "FROM Message "
                + "WHERE (sender_id = ? OR guest_label = ?) "
                + "AND recipient_id IS NOT NULL "
                + "AND message_type = 'CHAT' "
                + "ORDER BY sent_time DESC";
        try (Connection conn = dbconnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            if (userId != null) {
                ps.setInt(1, userId);
            } else {
                ps.setNull(1, java.sql.Types.INTEGER);
            }
            ps.setString(2, guestLabel);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    staffId = rs.getInt("recipient_id");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return staffId;
    }

    // ==================== LIVE CHAT ====================
    // Lấy tất cả tin nhắn chat
    public List<Message> getMessages(String guestLabel, Integer userId, long afterId) throws SQLException {
        List<Message> messages = new ArrayList<>();
        String sql;
        if (guestLabel != null) {
            sql = "SELECT * FROM Message WHERE message_type='CHAT' AND guest_label=? AND id > ? ORDER BY id ASC";
        } else if (userId != null) {
            sql = "SELECT * FROM Message WHERE message_type='CHAT' AND (sender_id=? OR recipient_id=?) AND id > ? ORDER BY id ASC";
        } else {
            return messages;
        }
        try (Connection conn = dbconnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            if (guestLabel != null) {
                ps.setString(1, guestLabel);
                ps.setLong(2, afterId);
            } else {
                ps.setInt(1, userId);
                ps.setInt(2, userId);
                ps.setLong(3, afterId);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Message msg = new Message();
                msg.setId(rs.getInt("id"));
                msg.setSenderId((Integer) rs.getObject("sender_id"));
                msg.setRecipientId((Integer) rs.getObject("recipient_id"));
                msg.setGuest_label(rs.getString("guest_label"));
                msg.setContent(rs.getString("content"));
                msg.setSentTime(rs.getTimestamp("sent_time"));
                msg.setIsRead(rs.getBoolean("is_read"));

                if (msg.getSenderId() != null) {
                    String role = getRoleById(msg.getSenderId());
                    if ("user".equals(role)) {
                        msg.setSenderType("user");
                        msg.setSenderName(getUsernameById(msg.getSenderId()));
                    } else if ("staff".equals(role)) {
                        msg.setSenderType("staff");
                        msg.setSenderName("Staff");
                    } else {
                        // fallback user nếu role không rõ
                        msg.setSenderType("user");
                        msg.setSenderName(getUsernameById(msg.getSenderId()));
                    }
                } else if (msg.getGuest_label() != null) {
                    msg.setSenderType("guest");
                    msg.setSenderName("Guest-" + msg.getGuest_label());
                }
                messages.add(msg);
            }
        }
        return messages;
    }

    // Insert message và trả về id mới
    public int insertMessage(Message msg) throws SQLException {
        String sql = "INSERT INTO Message(sender_id, guest_label, content, message_type, sent_time, is_read) "
                + "VALUES (?, ?, ?, 'CHAT', GETDATE(), 0)";
        try (Connection conn = dbconnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, new String[]{"id"})) {
            if (msg.getSenderId() != null) {
                ps.setInt(1, msg.getSenderId());
            } else {
                ps.setNull(1, java.sql.Types.INTEGER);
            }
            ps.setString(2, msg.getGuest_label());
            ps.setString(3, msg.getContent());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1); // id vừa insert
                }
            }
        }
        return -1;
    }

    public String getUsernameById(int userId) throws SQLException {
        String username = null;
        String sql = "SELECT username FROM Account WHERE user_id = ?";
        try (Connection conn = dbconnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    username = rs.getString("username");
                }
            }
        }
        return username != null ? username : "User"; // default nếu không tìm thấy
    }

    public String getRoleById(Integer userId) {
        if (userId == null) {
            return "guest";
        }
        String role = null;
        String sql = "SELECT role FROM Account WHERE user_id = ?";
        try (Connection conn = dbconnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    role = rs.getString("role");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (role == null) {
            return "user"; // fallback
        }
        return role.trim().toLowerCase(); // "user" hoặc "staff"
    }

    // Lấy danh sách tất cả guest đang chat
    public List<String> getActiveGuests() throws SQLException {
        List<String> guests = new ArrayList<>();
        String sql = "SELECT DISTINCT guest_label FROM Message WHERE message_type='CHAT' AND guest_label IS NOT NULL";
        try (Connection conn = dbconnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                guests.add(rs.getString("guest_label"));
            }
        }
        return guests;
    }

    public void deleteGuestMessages(String guestLabel) throws SQLException {
        String sql = "DELETE FROM Message WHERE guest_label = ?";
        try (Connection conn = dbconnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, guestLabel);
            ps.executeUpdate();
        }
    }

// ==================== COMMON ====================
    public void markAsRead(int id) {
        String sql = "UPDATE Message SET is_read = 1 WHERE id = ?";
        try (Connection conn = dbconnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int countUnreadMessages(String recipientEmail) {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM Message WHERE recipient_email = ? AND is_read = 0";
        try (Connection conn = dbconnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, recipientEmail);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public String getEmailByUsername(String username) throws SQLException {
        String email = null;
        try (Connection conn = dbconnect.getConnection()) {
            String sql = "SELECT email FROM Account WHERE username = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        email = rs.getString("email");
                    }
                }
            }
        }
        return email;
    }
}
