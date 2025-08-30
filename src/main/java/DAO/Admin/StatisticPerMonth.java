/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO.Admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Airline;
import model.Flight;
import utils.DBContext;

/**
 *
 * @author ADMIN
 */
public class StatisticPerMonth {

    Airline airline = new Airline();

    DBContext dbconnect = new DBContext();

    // Đếm số user đăng ký trong tháng/năm
    public int countUsersInMonthYear(int m, int y) {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM account WHERE MONTH(created_at) = ? AND YEAR(created_at) = ?";
        try (Connection conn = dbconnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, m);
            ps.setInt(2, y);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    // Thống kê số chuyến bay theo trạng thái
    public int countFlightsByStatus(String status) {
        String sql = "SELECT COUNT(*) FROM Flight WHERE status = ?";
        try (Connection conn = dbconnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    //DAO cập nhật trạng thái
//    public void updateFlightStatus(int flightId, String newStatus) {
//        String sql = "UPDATE Flight "
//                + "SET status = ?, status_update_time = GETDATE() "
//                + "WHERE flight_id = ?";
//        try (Connection conn = dbconnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setString(1, newStatus);
//            ps.setInt(2, flightId);
//            ps.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
    // Lấy danh sách chuyến bay theo trạng thái
    public List<Flight> getFlightsByStatus(String status) throws SQLException {
        List<Flight> list = new ArrayList<>();
        String sql = "SELECT f.flight_id, f.flight_number, f.departure_time, f.arrival_time, f.status, "
                + "       a.name AS airline_name, "
                + "       r.origin_airport_id, r.destination_airport_id, "
                + "       f.AircraftTypeID "
                + "FROM Flight f "
                + "JOIN Airline a ON f.airline_id = a.airline_id "
                + "JOIN Route r ON f.route_id = r.route_id "
                + "WHERE f.status = ?";
        try (Connection conn = dbconnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Flight f = new Flight();
                    f.setFlightId(rs.getInt("flight_id"));
                    f.setFlightNumber(rs.getString("flight_number"));
                    f.setDepartureTime(rs.getTimestamp("departure_time"));
                    f.setArrivalTime(rs.getTimestamp("arrival_time"));
                    f.setStatus(rs.getString("status"));
                    f.setRouteFrom(String.valueOf(rs.getInt("origin_airport_id")));  // lấy ID sân bay đi
                    f.setRouteTo(String.valueOf(rs.getInt("destination_airport_id"))); // lấy ID sân bay đến
                    f.setAircraftTypeId(rs.getInt("AircraftTypeID"));
                    Airline airline = new Airline();
                    airline.setName(rs.getString("airline_name"));
                    f.setAirline(airline);
                    list.add(f);

                }
            }
        }
        return list;
    }

    public List<Flight> getFlightsByStatusAndDate(String status, Integer month, Integer year, String airlineFilter) {
        List<Flight> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT f.flight_id, f.flight_number, a.name AS airline_name, \n"
                + "       f.route_id, f.departure_time, f.arrival_time, f.status\n"
                + "FROM Flight f\n"
                + "JOIN Airline a ON f.airline_id = a.airline_id\n"
                + "WHERE f.status = ?"
        );

        if (month != null) {
            sql.append(" AND MONTH(f.departure_time) = ?");
        }
        if (year != null) {
            sql.append(" AND YEAR(f.departure_time) = ?");
        }

        // Subquery chọn airline_id theo filter
        sql.append(" AND f.airline_id = ( "
                + "     SELECT TOP 1 f2.airline_id "
                + "     FROM Flight f2 "
                + "     WHERE f2.status = ?");

        if (month != null) {
            sql.append(" AND MONTH(f2.departure_time) = ?");
        }
        if (year != null) {
            sql.append(" AND YEAR(f2.departure_time) = ?");
        }

        // max hoặc min
        if ("min".equalsIgnoreCase(airlineFilter)) {
            sql.append("     GROUP BY f2.airline_id "
                    + "     ORDER BY COUNT(*) ASC ");
        } else {
            sql.append("     GROUP BY f2.airline_id "
                    + "     ORDER BY COUNT(*) DESC ");
        }

        sql.append(") ");

        sql.append(" ORDER BY f.departure_time DESC"); // mặc định sort theo ngày

        try (Connection conn = dbconnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int index = 1;
            ps.setString(index++, status);
            if (month != null) {
                ps.setInt(index++, month);
            }
            if (year != null) {
                ps.setInt(index++, year);
            }
            ps.setString(index++, status);
            if (month != null) {
                ps.setInt(index++, month);
            }
            if (year != null) {
                ps.setInt(index++, year);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Flight f = new Flight();
                f.setFlightId(rs.getInt("flight_id"));
                f.setFlightNumber(rs.getString("flight_number"));
                f.setRouteId(rs.getInt("route_id"));
                f.setDepartureTime(rs.getTimestamp("departure_time"));
                f.setArrivalTime(rs.getTimestamp("arrival_time"));
                f.setStatus(rs.getString("status"));
                Airline airline = new Airline();
                airline.setName(rs.getString("airline_name"));
                f.setAirline(airline);
                list.add(f);
                list.add(f);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Hàm chung
    private Flight getMostFlightByStatus(String status, Integer month, Integer year) {
        StringBuilder sql = new StringBuilder(
                "SELECT TOP 1 f.flight_number, COUNT(*) AS total "
                + "FROM Flight f WHERE f.status = ?"
        );

        if (month != null) {
            sql.append(" AND MONTH(f.departure_time) = ?");
        }
        if (year != null) {
            sql.append(" AND YEAR(f.departure_time) = ?");
        }
        sql.append(" GROUP BY f.flight_number ORDER BY total DESC");

        try (Connection conn = dbconnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int index = 1;
            ps.setString(index++, status);
            if (month != null) {
                ps.setInt(index++, month);
            }
            if (year != null) {
                ps.setInt(index++, year);
            }

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Flight f = new Flight();
                f.setFlightNumber(rs.getString("flight_number"));
                f.setCompletedCount(rs.getInt("total")); // tái sử dụng completedCount
                return f;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Flight getMostCompletedFlight(Integer month, Integer year) {
        return getMostFlightByStatus("ON TIME", month, year);
    }
// Chuyến bay hoãn nhiều nhất

    public Flight getMostDelayedFlight(Integer month, Integer year) {
        return getMostFlightByStatus("DELAYED", month, year);
    }
// Chuyến bay hủy nhiều nhất

    public Flight getMostCancelledFlight(Integer month, Integer year) {
        return getMostFlightByStatus("CANCELLED", month, year);
    }

    // Đếm chuyến bay hoàn thành (ON TIME) theo năm dựa vào status_update_time
    public int getCompletedFlightsByYear(int year) {
        String sql = "SELECT COUNT(*) FROM Flight "
                + "WHERE status = 'ON TIME' AND YEAR(status_update_time) = ?";
        try (Connection conn = dbconnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, year);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

// Đếm chuyến bay hủy (CANCELLED) theo năm
    public int getCancelledFlightsByYear(int year) {
        String sql = "SELECT COUNT(*) FROM Flight "
                + "WHERE status = 'CANCELLED' AND YEAR(status_update_time) = ?";
        try (Connection conn = dbconnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, year);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

// Đếm chuyến bay delay (DELAYED) theo năm
    public int getDelayedFlightsByYear(int year) {
        String sql = "SELECT COUNT(*) FROM Flight "
                + "WHERE status = 'DELAYED' AND YEAR(status_update_time) = ?";
        try (Connection conn = dbconnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, year);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Thống kê theo hãng bay
    public double getRevenueByAirlineMonthYear(int airlineId, int month, int year) {
        String sql = "SELECT SUM(b.total_price) "
                + "FROM Booking b "
                + "JOIN Flight f ON b.flight_id = f.flight_id "
                + "WHERE b.status='CONFIRMED' AND f.airline_id=? "
                + "AND MONTH(b.booking_time)=? AND YEAR(b.booking_time)=?";
        try (Connection conn = dbconnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, airlineId);
            ps.setInt(2, month);
            ps.setInt(3, year);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public int countFlightsByAirlineAndStatus(int airlineId, String status, int month, int year) {
        String sql = "SELECT COUNT(*) FROM Flight "
                + "WHERE airline_id=? AND status=? "
                + "AND MONTH(departure_time)=? AND YEAR(departure_time)=?";
        try (Connection conn = dbconnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, airlineId);
            ps.setString(2, status);
            ps.setInt(3, month);
            ps.setInt(4, year);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int countTicketsSoldByAirline(int airlineId, int month, int year) {
        String sql = "SELECT COUNT(*) FROM Booking b "
                + "JOIN Flight f ON b.flight_id = f.flight_id "
                + "WHERE b.status='CONFIRMED' AND f.airline_id=? "
                + "AND MONTH(b.booking_time)=? AND YEAR(b.booking_time)=?";
        try (Connection conn = dbconnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, airlineId);
            ps.setInt(2, month);
            ps.setInt(3, year);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Airline> getAllAirlines() {
        List<Airline> list = new ArrayList<>();
        String sql = "SELECT airline_id, name FROM Airline";
        try (Connection conn = dbconnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Airline a = new Airline();
                a.setAirlineId(rs.getInt("airline_id"));
                a.setName(rs.getString("name"));
                list.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Doanh thu theo tháng/năm (dùng total_price trong Booking)
    public double getRevenueMonthYear(int month, int year) {
        String sql = """
                SELECT SUM(b.total_price)
                FROM Booking b
                WHERE b.status = 'CONFIRMED'
                  AND MONTH(b.booking_time) = ?
                  AND YEAR(b.booking_time) = ?
                """;
        try (Connection conn = dbconnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, month);
            ps.setInt(2, year);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    // Danh sách khách hàng có đặt vé thành công
    public List<String> getCustomerList() {
        List<String> customers = new ArrayList<>();
        String sql = """
                SELECT DISTINCT a.full_name
                FROM Account a
                JOIN Booking b ON a.user_id = b.user_id
                WHERE b.status = 'CONFIRMED'
                """;
        try (Connection conn = dbconnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                customers.add(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    // Số vé đã bán trong tháng/năm
    public int countTicketsSold(int month, int year) {
        String sql = "SELECT COUNT(*) FROM Booking WHERE status = 'CONFIRMED' AND MONTH(booking_time) = ? AND YEAR(booking_time) = ?";
        try (Connection conn = dbconnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, month);
            ps.setInt(2, year);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

//    public List<Airline> getTopAirlinesByTicketSales() throws SQLException {
//        List<Airline> list = new ArrayList<>();
//        String sql = """
//                        SELECT a.name
//                             , COUNT(b.booking_id) AS totalTickets 
//                             FROM Booking b JOIN Flight f ON b
//                             .flight_id = f.flight_id 
//                             JOIN Airline a ON f.airline_id = a.airline_id 
//                             GROUP BY a.name ORDER BY totalTickets DESC
//                     """;
//        try (Connection conn = dbconnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
//            while (rs.next()) {
//                Airline a = new Airline();
//                a.setName(rs.getString("airlineName"));
//                a.setTicketsSold(rs.getInt("ticketsSold"));
//                list.add(a);
//            }
//        }
//        return list;
//    }
}
