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
    public List<Message> getSupportMails(int recipientId) throws SQLException {
        List<Message> list = new ArrayList<>();
        String sql = "SELECT * FROM Message "
                + "WHERE message_type = 'SUPPORT' AND recipient_id = ? "
                + "ORDER BY sent_time ASC";
        try (Connection conn = dbconnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, recipientId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Message mail = new Message();
                mail.setId(rs.getInt("id"));
                mail.setSenderId(rs.getInt("sender_id"));
                mail.setRecipientId(rs.getInt("recipient_id"));
                mail.setSubject(rs.getString("subject"));
                mail.setContent(rs.getString("content"));
                mail.setSentTime(rs.getTimestamp("sent_time"));
                mail.setIsRead(rs.getBoolean("is_read"));
                list.add(mail);
            }
        }
        return list;
    }

    public boolean insertSupportMail(Message mail, int senderId, int recipientId) {
        String sql = "INSERT INTO Message (sender_id, recipient_id, subject, content, message_type) "
                + "VALUES (?, ?, ?, ?, 'SUPPORT')";
        try (Connection conn = dbconnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, senderId);
            ps.setInt(2, recipientId);
            ps.setString(3, mail.getSubject());
            ps.setString(4, mail.getContent());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
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

    public List<Message> LiveChatGuest(String guestLabel, int staffId) throws SQLException {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM Message "
                + "WHERE message_type = 'CHAT' AND guest_label = ? "
                + "ORDER BY sent_time ASC";
        try (Connection conn = dbconnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, guestLabel);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Message msg = new Message();
                msg.setId(rs.getInt("id"));
                msg.setSenderId(rs.getInt("sender_id"));
                msg.setRecipientId(rs.getInt("recipient_id"));
                msg.setSentTime(rs.getTimestamp("sent_time"));
                msg.setIsRead(rs.getBoolean("is_read"));
                msg.setGuest_label(rs.getString("guest_label"));
                msg.setContent(rs.getString("content"));
                // phân loại dựa vào id
                if (msg.getSenderId() == staffId) {
                    msg.setSenderType("staff");
                } else {
                    msg.setSenderType("guest");
                }
                messages.add(msg);
            }
        }
        return messages;
    }

    public List<Message> LiveChatUser(int userId, int staffId) throws SQLException {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT m.id, m.content, m.sent_time, m.is_read, "
                + "m.sender_id, s.username AS sender_name, "
                + "m.recipient_id, r.username AS recipient_name, "
                + "m.guest_label "
                + "FROM Message m "
                + "LEFT JOIN Account s ON m.sender_id = s.user_id "
                + "LEFT JOIN Account r ON m.recipient_id = r.user_id "
                + "WHERE m.message_type = 'CHAT' "
                + "AND ( (m.sender_id = ? AND m.recipient_id = ?) "
                + "   OR (m.sender_id = ? AND m.recipient_id = ?) ) "
                + "ORDER BY m.sent_time ASC";
        try (Connection conn = dbconnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, staffId);
            ps.setInt(3, staffId);
            ps.setInt(4, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Message msg = new Message();
                msg.setId(rs.getInt("id"));
                msg.setSenderId(rs.getInt("sender_id"));
                msg.setSenderName(rs.getString("sender_name"));
                msg.setRecipientId(rs.getInt("recipient_id"));
                msg.setRecipientName(rs.getString("recipient_name"));
                msg.setContent(rs.getString("content"));
                msg.setSentTime(rs.getTimestamp("sent_time"));
                msg.setIsRead(rs.getBoolean("is_read"));
                msg.setGuest_label(rs.getString("guest_label"));
                if (msg.getSenderId() == staffId) {
                    msg.setSenderType("staff");
                } else {
                    msg.setSenderType("user");
                }
                messages.add(msg);
            }
        }
        return messages;
    }

    public boolean insertLiveChat(Message msg) {
        String sqlUser = "INSERT INTO Message (sender_id, recipient_id, content, message_type, sent_time, is_read) "
                + "VALUES (?, ?, ?, 'CHAT', GETDATE(), 0)";

        String sqlGuest = "INSERT INTO Message (sender_id, guest_label, content, message_type, sent_time, is_read) "
                + "VALUES (?, ?, ?, 'CHAT', GETDATE(), 0)";
        try (Connection conn = dbconnect.getConnection()) {
            PreparedStatement ps;
            if (msg.getGuest_label() != null && !msg.getGuest_label().isEmpty()) {
                // insert cho guest
                ps = conn.prepareStatement(sqlGuest);
                ps.setInt(1, msg.getSenderId());
                ps.setString(2, msg.getGuest_label());
                ps.setString(3, msg.getContent());
            } else {
                // insert cho user
                ps = conn.prepareStatement(sqlUser);
                ps.setInt(1, msg.getSenderId());
                ps.setInt(2, msg.getRecipientId());
                ps.setString(3, msg.getContent());
            }

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
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
        String sql = "SELECT COUNT(*) FROM Message WHERE recipient_id = ? AND is_read = 0";
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
        String sql = "UPDATE Message SET is_read = 1 WHERE recipient_id = ?";
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
