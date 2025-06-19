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
        String sql = "SELECT f.*, a.airline_name FROM Flight f "
                + "JOIN Airline a ON f.airline_id = a.airline_id";

        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {

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
}
