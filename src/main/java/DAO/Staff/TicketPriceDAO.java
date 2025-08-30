/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO.staff;

/**
 *
 * @author Khoa
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Airline;
import model.Flight;
import model.Route;
import model.SeatClass;
import model.TicketClass;
import utils.DBContext;

public class TicketPriceDAO {
    private static final Logger LOGGER = Logger.getLogger(TicketPriceDAO.class.getName());
    private DBContext dbContext;

    public TicketPriceDAO() {
        this.dbContext = new DBContext();
    }

    // Get all flights with their ticket price status
    public List<Object[]> getFlightsWithPriceStatus() throws SQLException {
        List<Object[]> flights = new ArrayList<>();
        String query = "SELECT f.flight_id, f.flight_number, a.name AS airline_name, " +
                      "ao.iata_code AS origin_iata, ad.iata_code AS dest_iata, " +
                      "f.departure_time, f.arrival_time, f.status, " +
                      "(SELECT COUNT(*) FROM TicketClass tc WHERE tc.flight_id = f.flight_id) AS price_count " +
                      "FROM Flight f " +
                      "JOIN Airline a ON a.airline_id = f.airline_id " +
                      "LEFT JOIN Route r ON r.route_id = f.route_id " +
                      "LEFT JOIN Airport ao ON ao.airport_id = r.origin_airport_id " +
                      "LEFT JOIN Airport ad ON ad.airport_id = r.destination_airport_id";
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = dbContext.getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                Object[] flight = new Object[8];
                flight[0] = rs.getInt("flight_id");
                flight[1] = rs.getString("flight_number");
                flight[2] = rs.getString("airline_name");
                flight[3] = rs.getString("origin_iata");
                flight[4] = rs.getString("dest_iata");
                flight[5] = rs.getTimestamp("departure_time");
                flight[6] = rs.getTimestamp("arrival_time");
                flight[7] = rs.getInt("price_count") > 0 ? "Đã cập nhật" : "Chưa cập nhật";
                flights.add(flight);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching flights with price status", e);
            throw e;
        } finally {
            DBContext.closeSelectResources(rs, ps, conn);
        }
        return flights;
    }

    // Get seat classes for a flight based on AircraftTypeID
    public List<SeatClass> getSeatClassesForFlight(int flightId) throws SQLException {
        List<SeatClass> seatClasses = new ArrayList<>();
        String query = "SELECT sc.SeatClassID, sc.Name, sc.Description, sc.Status " +
                      "FROM SeatClass sc " +
                      "JOIN AircraftSeatConfig acfg ON acfg.SeatClassID = sc.SeatClassID " +
                      "JOIN Flight f ON f.AircraftTypeID = acfg.AircraftTypeID " +
                      "WHERE f.flight_id = ?";
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = dbContext.getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, flightId);
            rs = ps.executeQuery();
            while (rs.next()) {
                SeatClass seatClass = new SeatClass(
                    rs.getInt("SeatClassID"),
                    rs.getString("Name"),
                    rs.getString("Description"),
                    rs.getString("Status")
                );
                seatClasses.add(seatClass);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching seat classes for flight ID: " + flightId, e);
            throw e;
        } finally {
            DBContext.closeSelectResources(rs, ps, conn);
        }
        return seatClasses;
    }

    // Get existing ticket prices for a flight
    public List<TicketClass> getTicketPricesForFlight(int flightId) throws SQLException {
        List<TicketClass> ticketClasses = new ArrayList<>();
        String query = "SELECT tc.class_id, tc.flight_id, sc.Name AS class_name, tc.price " +
                      "FROM TicketClass tc " +
                      "JOIN SeatClass sc ON sc.SeatClassID = tc.SeatClassID " +
                      "WHERE tc.flight_id = ?";
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = dbContext.getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, flightId);
            rs = ps.executeQuery();
            while (rs.next()) {
                TicketClass ticketClass = new TicketClass(
                    rs.getInt("class_id"),
                    rs.getInt("flight_id"),
                    rs.getString("class_name"),
                    rs.getDouble("price")
                );
                ticketClasses.add(ticketClass);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching ticket prices for flight ID: " + flightId, e);
            throw e;
        } finally {
            DBContext.closeSelectResources(rs, ps, conn);
        }
        return ticketClasses;
    }

    // Insert or update ticket price
    public void saveTicketPrice(int flightId, int seatClassId, double price) throws SQLException {
        String checkQuery = "SELECT COUNT(*) FROM TicketClass WHERE flight_id = ? AND SeatClassID = ?";
        String insertQuery = "INSERT INTO TicketClass (flight_id, SeatClassID, price) VALUES (?, ?, ?)";
        String updateQuery = "UPDATE TicketClass SET price = ? WHERE flight_id = ? AND SeatClassID = ?";
        
        Connection conn = null;
        PreparedStatement checkPs = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = dbContext.getConnection();
            // Check if price exists
            checkPs = conn.prepareStatement(checkQuery);
            checkPs.setInt(1, flightId);
            checkPs.setInt(2, seatClassId);
            rs = checkPs.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            
            if (count > 0) {
                // Update existing price
                ps = conn.prepareStatement(updateQuery);
                ps.setDouble(1, price);
                ps.setInt(2, flightId);
                ps.setInt(3, seatClassId);
            } else {
                // Insert new price
                ps = conn.prepareStatement(insertQuery);
                ps.setInt(1, flightId);
                ps.setInt(2, seatClassId);
                ps.setDouble(3, price);
            }
            ps.executeUpdate();
            LOGGER.info("Saved ticket price for flight ID: " + flightId + ", seat class ID: " + seatClassId);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error saving ticket price for flight ID: " + flightId, e);
            throw e;
        } finally {
            DBContext.closeSelectResources(rs, checkPs, null);
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, "Error closing PreparedStatement", e);
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, "Error closing Connection", e);
                }
            }
        }
    }
    
    // Lấy chi tiết chuyến bay
public Flight getFlightDetails2(int flightId) {
    String query = "SELECT f.flight_id, f.flight_number, f.departure_time, f.arrival_time, f.status, f.route_id, "
            + "ao.city AS origin_city, ao.name AS origin_name, "
            + "ad.city AS dest_city, ad.name AS dest_name, "
            + "al.name AS airline_name "
            + "FROM Flight f "
            + "JOIN Route r ON f.route_id = r.route_id "
            + "JOIN Airport ao ON r.origin_airport_id = ao.airport_id "
            + "JOIN Airport ad ON r.destination_airport_id = ad.airport_id "
            + "JOIN Airline al ON f.airline_id = al.airline_id "
            + "WHERE f.flight_id = ?";

    try (Connection conn = dbContext.getConnection();
         PreparedStatement ps = conn.prepareStatement(query)) {

        ps.setInt(1, flightId);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                Flight flight = new Flight();
                flight.setFlightId(rs.getInt("flight_id"));
                flight.setFlightNumber(rs.getString("flight_number"));
                flight.setDepartureTime(rs.getTimestamp("departure_time"));
                flight.setArrivalTime(rs.getTimestamp("arrival_time"));
                flight.setStatus(rs.getString("status"));

                Route route = new Route();
                route.setRouteId(rs.getInt("route_id"));
                route.setOriginIata(rs.getString("origin_city"));
                route.setOriginName(rs.getString("origin_name"));
                route.setDestIata(rs.getString("dest_city"));
                route.setDestName(rs.getString("dest_name"));
                flight.setRoute(route);

                Airline airline = new Airline();
                airline.setName(rs.getString("airline_name"));
                flight.setAirline(airline);

                LOGGER.info("Flight details for flightId " + flightId
                        + ": flight_number=" + flight.getFlightNumber()
                        + ", origin_city=" + route.getOriginIata()
                        + ", origin_name=" + route.getOriginName()
                        + ", dest_city=" + route.getDestIata()
                        + ", dest_name=" + route.getDestName()
                        + ", airline_name=" + airline.getName());
                return flight;
            } else {
                LOGGER.warning("Không tìm thấy chuyến bay với flightId: " + flightId);
            }
        }
    } catch (SQLException e) {
        LOGGER.log(Level.SEVERE, "Lỗi khi lấy chi tiết chuyến bay với flightId: " + flightId, e);
    }
    return null;
}

}