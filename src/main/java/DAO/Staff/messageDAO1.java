package DAO.staff;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Account;
import model.Message;
import utils.DBContext;

/**
 *
 * @author lgbaoce180780
 */
public class messageDAO1 {

    DBContext dbconnect = new DBContext();

    // ==================== SUPPORT MAIL ====================
    public List<Message> getAllSupportMessages() throws SQLException {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT m.id, m.subject, m.content, m.sent_time, m.is_read, "
                + "a.username AS senderName, m.guest_label "
                + "FROM Message m "
                + "LEFT JOIN Account a ON m.sender_id = a.user_id "
                + "WHERE m.message_type = 'SUPPORT' "
                + "ORDER BY m.sent_time DESC";
        try (Connection conn = dbconnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Message msg = new Message();
                msg.setId(rs.getInt("id"));
                msg.setSenderId(rs.getInt("sender_id"));
                msg.setRecipientId(rs.getInt("recipient_id"));
                msg.setSenderName(rs.getString("senderName"));
                msg.setGuest_label(rs.getString("guest_label"));
                msg.setSubject(rs.getString("subject"));
                msg.setContent(rs.getString("content"));
                msg.setSentTime(rs.getTimestamp("sent_time"));
                msg.setIsRead(rs.getBoolean("is_read"));
                msg.setMessageType(rs.getString("message_type"));
                messages.add(msg);
            }
        }
        return messages;
    }

    // Lấy chi tiết 1 tin nhắn theo id
    public Message getSupportMessageById(int id) throws SQLException {
        String sql = "SELECT m.*, a.username AS senderName "
                + "FROM Message m "
                + "LEFT JOIN Account a ON m.sender_id = a.user_id "
                + "WHERE m.id = ? AND m.message_type = 'SUPPORT'";
        try (Connection conn = dbconnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            ps.setInt(1, id);
            while (rs.next()) {
                Message msg = new Message();
                msg.setId(rs.getInt("id"));
                msg.setSenderId(rs.getInt("sender_id"));
                msg.setRecipientId(rs.getInt("recipient_id"));
                msg.setSenderName(rs.getString("senderName"));
                msg.setGuest_label(rs.getString("guest_label"));
                msg.setSubject(rs.getString("subject"));
                msg.setContent(rs.getString("content"));
                msg.setSentTime(rs.getTimestamp("sent_time"));
                msg.setIsRead(rs.getBoolean("is_read"));
                msg.setMessageType(rs.getString("message_type"));
                return msg;
            }
        }
        return null;
    }

    public void insertMessage(Message msg) {
        String sql = "INSERT INTO Message (sender_email, recipient_email, subject, content) VALUES (?, ?, ?, ?)";
        try (Connection conn = dbconnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            ps.setString(1, msg.getSenderEmail());
            ps.setString(2, msg.getRecipientEmail());
            ps.setString(3, msg.getSubject());
            ps.setString(4, msg.getContent());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ==================== LIVE CHAT ====================
    public List<String> getActiveGuests() throws SQLException {
        List<String> guests = new ArrayList<>();
        String sql = "SELECT DISTINCT guest_label FROM Message WHERE message_type = 'CHAT' AND guest_label is not null";
        try (Connection conn = dbconnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                guests.add(rs.getString("guest_label"));
            }
            System.out.println("Active guests: " + guests);
        }
        return guests;
    }

    public List<Account> getAllUsernames() throws SQLException {
        List<Account> usernames = new ArrayList<>();
        String sql = "SELECT DISTINCT a.user_id, a.username "
                + "FROM Message m "
                + "JOIN Account a ON (m.sender_id = a.user_id OR m.recipient_id = a.user_id) "
                + "WHERE m.message_type = 'CHAT' AND a.role = 'CUSTOMER'";
        try (Connection conn = dbconnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Account acc = new Account();
                acc.setUserId(rs.getInt("user_id"));
                acc.setUsername(rs.getString("username"));
                usernames.add(acc);
            }
        }
        return usernames;
    }

    public String getUsernameById(int userId) throws SQLException {
        String sql = "SELECT username FROM Account WHERE user_id = ? AND role = 'CUSTOMER'";
        try (Connection conn = dbconnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("username");
                }
            }
        }
        return null;
    }

    // Lấy tin nhắn chat mới hơn afterId
    public List<Message> getMessagesAfter(String guestLabel, Integer userId, long afterId, int staffId) throws SQLException {
        List<Message> messages = new ArrayList<>();
        String sql;
        if (guestLabel != null) {
            sql = "SELECT * FROM Message WHERE message_type='CHAT' AND guest_label=? AND id > ? ORDER BY id ASC";
        } else if (userId != null) {
            sql = "SELECT * FROM Message WHERE message_type='CHAT' AND ( (sender_id = ? AND (recipient_id = ? OR recipient_id IS NULL)) "
                    + " OR (sender_id=? AND recipient_id=?)) AND id > ? ORDER BY id ASC";
        } else {
            return messages;
        }

        try (Connection conn = dbconnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            if (guestLabel != null) {
                ps.setString(1, guestLabel);
                ps.setLong(2, afterId);
            } else {
                ps.setInt(1, userId);
                ps.setInt(2, staffId);
                ps.setInt(3, staffId);
                ps.setInt(4, userId);
                ps.setLong(5, afterId);
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

                if (msg.getSenderId() != null && msg.getSenderId() == staffId) {
                    msg.setSenderType("staff");
                    msg.setSenderName("Staff");
                } else if (guestLabel != null) {
                    msg.setSenderType("guest");
                } else {
                    msg.setSenderType("user");
                    if (msg.getSenderId() != null) {
                        msg.setSenderName(getUsernameById(msg.getSenderId()));
                    }
                }
                System.out.println("Fetching messages after ID: " + afterId + " for guest: " + guestLabel + " or userId: " + userId);

                messages.add(msg);
            }
        }
        return messages;
    }

    public Integer insertLiveChat(Message msg) {
        String sqlUser = "INSERT INTO Message (sender_id, recipient_id, content, message_type, sent_time, is_read) "
                + "VALUES (?, ?, ?, 'CHAT', GETDATE(), 0)";

        String sqlGuest = "INSERT INTO Message (sender_id, guest_label, content, message_type, sent_time, is_read) "
                + "VALUES (?, ?, ?, 'CHAT', GETDATE(), 0)";
        try (Connection conn = dbconnect.getConnection()) {
            PreparedStatement ps;
            if (msg.getGuest_label() != null && !msg.getGuest_label().isEmpty()) {
                ps = conn.prepareStatement(sqlGuest, new String[]{"id"});
                ps.setInt(1, msg.getSenderId());
                ps.setString(2, msg.getGuest_label());
                ps.setString(3, msg.getContent());
            } else {
                ps = conn.prepareStatement(sqlUser, new String[]{"id"});
                ps.setInt(1, msg.getSenderId());
                ps.setInt(2, msg.getRecipientId());
                ps.setString(3, msg.getContent());
            }

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int newId = rs.getInt(1);
                        msg.setId(newId);
                        return newId;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean assignConversationToStaff(int staffId, Integer userId, String guestLabel) throws SQLException {
        String sql = "UPDATE Message SET recipient_id = ? "
                + "WHERE recipient_id IS NULL AND "
                + "((? IS NOT NULL AND sender_id = ?) OR (? IS NOT NULL AND guest_label = ?))";
        try (Connection conn = dbconnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, staffId);
            if (userId != null) {
                ps.setInt(2, userId);
                ps.setInt(3, userId);
            } else {
                ps.setNull(2, java.sql.Types.INTEGER);
                ps.setNull(3, java.sql.Types.INTEGER);
            }
            if (guestLabel != null) {
                ps.setString(4, guestLabel);
                ps.setString(5, guestLabel);
            } else {
                ps.setNull(4, java.sql.Types.VARCHAR);
                ps.setNull(5, java.sql.Types.VARCHAR);
            }
            return ps.executeUpdate() > 0;
        }
    }

    // Helper: lấy user_id từ email
    public Integer getUserIdByEmail(String email) throws SQLException {
        String sql = "SELECT user_id FROM Account WHERE email = ?";
        try (Connection conn = dbconnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("user_id");
            }
        }
        return null;
    }

// ==================== COMMON ====================
    public int countUnreadUserMessages(int recipientId) {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM Message WHERE recipient_id IS NULL AND is_read = 0 AND message_type='CHAT'  AND sender_id = ? ";
        try (Connection conn = dbconnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, recipientId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    // Lấy số tin nhắn chưa đọc của guest chưa được staff xem
    public int countUnreadGuestMessages(String guestLabel) {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM Message WHERE guest_label = ? AND is_read = 0";
        try (Connection conn = dbconnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, guestLabel);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public boolean markUserMessagesRead(int recipientId) {
        String sql = "UPDATE Message SET is_read = 1 WHERE recipient_id = ? WHERE message_type='CHAT' AND"
                + " ((sender_id = ? AND recipient_id IS NULL) OR recipient_id = ?)";
        try (Connection conn = dbconnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, recipientId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean markGuestMessagesRead(String guestLabel) {
        String sql = "UPDATE Message SET is_read = 1 WHERE guest_label = ?";
        try (Connection conn = dbconnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, guestLabel);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
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
