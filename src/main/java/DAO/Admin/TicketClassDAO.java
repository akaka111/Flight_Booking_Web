package DAO.Admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.TicketClass;
import utils.DBContext;

public class TicketClassDAO {

    // Lấy tất cả hạng ghế theo flightId
    public List<TicketClass> getTicketClassesByFlightId(int flightId) {
        List<TicketClass> tickets = new ArrayList<>();
        String sql = """
            SELECT t.price, s.Name AS class_name
            FROM TicketClass t
            JOIN SeatClass s ON t.SeatClassID = s.SeatClassID
            WHERE t.flight_id = ?;
        """;
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, flightId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                TicketClass ticket = new TicketClass();
                ticket.setClassName(rs.getString("class_name")); // map từ SeatClass.Name
                ticket.setPrice(rs.getDouble("price"));
                tickets.add(ticket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tickets;
    }

    // Lấy giá hạng Economy theo flightId
    public Double getEcoPriceByFlightId(int flightId) {
        String sql = """
            SELECT t.price
            FROM TicketClass t
            JOIN SeatClass s ON t.SeatClassID = s.SeatClassID
            WHERE t.flight_id = ? AND s.Name = 'Economy';
        """;
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

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

    // Lấy class_id theo flightId + tên hạng (Economy/Business/First)
    public int getClassIdByFlightIdAndName(int flightId, String className) {
        String sql = """
            SELECT t.class_id
            FROM TicketClass t
            JOIN SeatClass s ON t.SeatClassID = s.SeatClassID
            WHERE t.flight_id = ? AND s.Name = ?;
        """;
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, flightId);
            ps.setString(2, className);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("class_id");
                    System.out.println("Found class_id = " + id +
                        " for flightId = " + flightId +
                        " and className = " + className);
                    return id;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
