/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.Message;
import utils.DBContext;

/**
 *
 * @author ADMIN
 */
public class SupportContact {
 
    DBContext dbconnect = new DBContext();
    public void insertMessage(Message msg) {
        String sql = "INSERT INTO Message (sender_email, recipient_email, subject, content) VALUES (?, ?, ?, ?)";
        try (Connection conn = dbconnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, msg.getSenderEmail());
            ps.setString(2, msg.getRecipientEmail());
            ps.setString(3, msg.getSubject());
            ps.setString(4, msg.getContent());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Message> getMessagesByRecipient(String recipientEmail) {
        List<Message> list = new ArrayList<>();
        String sql = "SELECT * FROM Message WHERE recipient_email = ? ORDER BY sent_time DESC";
        try (Connection conn = dbconnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, recipientEmail);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Message msg = new Message(
                        rs.getInt("id"),
                        rs.getString("sender_email"),
                        rs.getString("subject"),
                        rs.getString("content"),
                        rs.getTimestamp("sent_time"),
                        rs.getBoolean("is_read"),
                        rs.getString("recipient_email")
                );
                list.add(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
