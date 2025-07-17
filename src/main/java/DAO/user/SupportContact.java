/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import utils.dbconnect;

/**
 *
 * @author ADMIN
 */
public class SupportContact {

    public void insertMessage(String email, String subject, String content) {
        String sql = "INSERT INTO Message (sender_email, subject, content) VALUES (?, ?, ?)";
        try (Connection conn = dbconnect.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, subject);
            stmt.setString(3, content);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
