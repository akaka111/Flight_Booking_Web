/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO.Admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.TicketClass;
import utils.DBContext;

/**
 *
 * @author $ LienXuanThinh - CE182117
 */
public class TicketClassDAO {

    public List<TicketClass> getTicketClassesByFlightId(int flightId) {
        List<TicketClass> tickets = new ArrayList<>();
        String sql = """
        SELECT t.price, c.class_name
        FROM TicketClass t
        JOIN TicketClass c ON t.class_id = c.class_id
        WHERE t.flight_id = ?; 
    """;
        DBContext db = new DBContext();
        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, flightId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                TicketClass ticket = new TicketClass();
                ticket.setClassName(rs.getString("class_name"));
                ticket.setPrice(rs.getDouble("price"));
                tickets.add(ticket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tickets;
    }

    public Double getEcoPriceByFlightId(int flightId) {
        String sql = "SELECT * FROM TicketClass WHERE flight_id = ? AND class_name = 'Economy'";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, flightId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("price");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getClassIdByFlightIdAndName(int flightId, String className) {
        String sql = "SELECT class_id FROM TicketClass WHERE flight_id = ? AND class_name = ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, flightId);
            ps.setString(2, className);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("class_id");
                    System.out.println("Found class_id = " + id + " for flightId = " + flightId + " and className = " + className);
                    return id;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

}
