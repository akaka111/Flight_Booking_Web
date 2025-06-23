/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import java.sql.*;
import java.util.*;
import model.Flight;
import utils.DBContext;

/**
 *
 * @author $ LienXuanThinh - CE182117
 */
public class FlightDAO extends DBContext {

    public List<Flight> getAllFlights() {
        List<Flight> flights = new ArrayList<>();
        String sql = "SELECT * FROM Flight;";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Flight f = new Flight();
                f.setFlightId(rs.getInt("flight_id"));
                f.setFlightNumber(rs.getString("flight_number"));
                f.setRouteFrom(rs.getString("route_from"));
                f.setRouteTo(rs.getString("route_to"));
                f.setDepartureTime(rs.getTimestamp("departure_time"));
                f.setArrivalTime(rs.getTimestamp("arrival_time"));
                f.setPrice(rs.getDouble("price"));
                f.setAircraft(rs.getString("aircraft"));
                f.setStatus(rs.getString("status"));
                flights.add(f);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return flights;
    }

    public Flight getFlightById(int flightId) {
        Flight flight = null;
        String sql = "SELECT * FROM Flight WHERE flight_id = ?";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, flightId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    flight = new Flight();
                    flight.setFlightId(rs.getInt("flight_id"));
                    flight.setAirlineId(rs.getInt("airline_id"));
                    flight.setFlightNumber(rs.getString("flight_number"));
                    flight.setRouteFrom(rs.getString("route_from"));
                    flight.setRouteTo(rs.getString("route_to"));
                    flight.setDepartureTime(rs.getTimestamp("departure_time"));
                    flight.setArrivalTime(rs.getTimestamp("arrival_time"));

                    // === SỬA Ở ĐÂY - LẤY TẤT CẢ CÁC MỨC GIÁ ===
                    flight.setPrice(rs.getDouble("price")); // Giá gốc (ECO)
                    flight.setPriceDeluxe(rs.getDouble("price_deluxe"));
                    flight.setPriceSkyboss(rs.getDouble("price_skyboss"));
                    flight.setPriceBusiness(rs.getDouble("price_business"));
                    // ===========================================

                    flight.setAircraft(rs.getString("aircraft"));
                    flight.setStatus(rs.getString("status"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flight;
    }
}
