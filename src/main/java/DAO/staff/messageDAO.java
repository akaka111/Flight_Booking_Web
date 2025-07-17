/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO.staff;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.Message;
import utils.dbconnect;

/**
 *
 * @author lgbaoce180780
 */
public class messageDAO {

    public List<Message> getAllMessages() {
        List<Message> list = new ArrayList<>();
        String sql = "SELECT * FROM Message ORDER BY sent_time DESC";
        try (Connection conn = dbconnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Message msg = new Message();
                msg.setId(rs.getInt("id"));
                msg.setSenderEmail(rs.getString("sender_email"));
                msg.setSubject(rs.getString("subject"));
                msg.setContent(rs.getString("content"));
                msg.setSentTime(rs.getTimestamp("sent_time"));
                msg.setIsRead(rs.getBoolean("is_read"));
                list.add(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Message getMessageById(int id) {
        String sql = "SELECT * FROM Message WHERE id = ?";
        try (Connection conn = dbconnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Message(
                            rs.getInt("id"),
                            rs.getString("sender_email"),
                            rs.getString("subject"),
                            rs.getString("content"),
                            rs.getTimestamp("sent_time"),
                            rs.getBoolean("is_read")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
