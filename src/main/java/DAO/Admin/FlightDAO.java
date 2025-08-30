package DAO.Admin;

import model.Flight;
import model.Airline;
import model.Route;
import model.AircraftType;
import utils.DBContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FlightDAO extends DBContext {
    private static final Logger LOGGER = Logger.getLogger(FlightDAO.class.getName());

    public List<Flight> getAllFlights() {
        List<Flight> flights = new ArrayList<>();
        String sql = "SELECT f.flight_id, f.flight_number, f.departure_time, f.arrival_time, f.status, "
                + "f.airline_id, a.name AS airline_name, a.code AS airline_code, "
                + "f.route_id, ao.iata_code AS origin_iata, ao.name AS origin_name, ao.city AS origin_city, "
                + "ad.iata_code AS dest_iata, ad.name AS dest_name, ad.city AS dest_city, "
                + "r.distance_km, r.duration_minutes, r.is_active, "
                + "f.AircraftTypeID, at.AircraftTypeName, at.AircraftTypeCode "
                + "FROM Flight f "
                + "JOIN Airline a ON a.airline_id = f.airline_id "
                + "LEFT JOIN Route r ON r.route_id = f.route_id "
                + "LEFT JOIN Airport ao ON ao.airport_id = r.origin_airport_id "
                + "LEFT JOIN Airport ad ON ad.airport_id = r.destination_airport_id "
                + "LEFT JOIN AircraftType at ON at.AircraftTypeID = f.AircraftTypeID";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Flight flight = new Flight();
                flight.setFlightId(rs.getInt("flight_id"));
                flight.setFlightNumber(rs.getString("flight_number"));
                flight.setDepartureTime(rs.getTimestamp("departure_time"));
                flight.setArrivalTime(rs.getTimestamp("arrival_time"));
                flight.setStatus(rs.getString("status"));
                Airline airline = new Airline();
                airline.setAirlineId(rs.getInt("airline_id"));
                airline.setName(rs.getString("airline_name"));
                airline.setCode(rs.getString("airline_code"));
                flight.setAirline(airline);
                Route route = new Route();
                route.setRouteId(rs.getInt("route_id"));
                route.setOriginIata(rs.getString("origin_iata"));
                route.setOriginName(rs.getString("origin_name"));
                route.setOriginCity(rs.getString("origin_city"));
                route.setDestIata(rs.getString("dest_iata"));
                route.setDestName(rs.getString("dest_name"));
                route.setDestCity(rs.getString("dest_city"));
                route.setDistanceKm(rs.getInt("distance_km"));
                route.setDurationMinutes(rs.getInt("duration_minutes"));
                route.setActive(rs.getBoolean("is_active"));
                flight.setRoute(route);
                AircraftType aircraftType = new AircraftType();
                aircraftType.setAircraftTypeId(rs.getInt("AircraftTypeID"));
                aircraftType.setAircraftTypeName(rs.getString("AircraftTypeName"));
                aircraftType.setAircraftTypeCode(rs.getString("AircraftTypeCode"));
                flight.setAircraftType(aircraftType);
                flights.add(flight);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving all flights", e);
        }
        return flights;
    }

    public Flight getFlightById(int flightId) {
        Flight flight = null;
        String sql = "SELECT f.flight_id, f.flight_number, f.departure_time, f.arrival_time, f.status, "
                + "f.airline_id, a.name AS airline_name, a.code AS airline_code, "
                + "f.route_id, ao.iata_code AS origin_iata, ao.name AS origin_name, ao.city AS origin_city, "
                + "ad.iata_code AS dest_iata, ad.name AS dest_name, ad.city AS dest_city, "
                + "r.distance_km, r.duration_minutes, r.is_active, "
                + "f.AircraftTypeID, at.AircraftTypeName, at.AircraftTypeCode "
                + "FROM Flight f "
                + "JOIN Airline a ON a.airline_id = f.airline_id "
                + "LEFT JOIN Route r ON r.route_id = f.route_id "
                + "LEFT JOIN Airport ao ON ao.airport_id = r.origin_airport_id "
                + "LEFT JOIN Airport ad ON ad.airport_id = r.destination_airport_id "
                + "LEFT JOIN AircraftType at ON at.AircraftTypeID = f.AircraftTypeID "
                + "WHERE f.flight_id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, flightId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    flight = new Flight();
                    flight.setFlightId(rs.getInt("flight_id"));
                    flight.setFlightNumber(rs.getString("flight_number"));
                    flight.setDepartureTime(rs.getTimestamp("departure_time"));
                    flight.setArrivalTime(rs.getTimestamp("arrival_time"));
                    flight.setStatus(rs.getString("status"));
                    Airline airline = new Airline();
                    airline.setAirlineId(rs.getInt("airline_id"));
                    airline.setName(rs.getString("airline_name"));
                    airline.setCode(rs.getString("airline_code"));
                    flight.setAirline(airline);
                    Route route = new Route();
                    route.setRouteId(rs.getInt("route_id"));
                    route.setOriginIata(rs.getString("origin_iata"));
                    route.setOriginName(rs.getString("origin_name"));
                    route.setOriginCity(rs.getString("origin_city"));
                    route.setDestIata(rs.getString("dest_iata"));
                    route.setDestName(rs.getString("dest_name"));
                    route.setDestCity(rs.getString("dest_city"));
                    route.setDistanceKm(rs.getInt("distance_km"));
                    route.setDurationMinutes(rs.getInt("duration_minutes"));
                    route.setActive(rs.getBoolean("is_active"));
                    flight.setRoute(route);
                    AircraftType aircraftType = new AircraftType();
                    aircraftType.setAircraftTypeId(rs.getInt("AircraftTypeID"));
                    aircraftType.setAircraftTypeName(rs.getString("AircraftTypeName"));
                    aircraftType.setAircraftTypeCode(rs.getString("AircraftTypeCode"));
                    flight.setAircraftType(aircraftType);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving flight by ID: " + flightId, e);
        }
        return flight;
    }

    public void insertFlight(Flight f) {
        String sql = "INSERT INTO Flight (airline_id, flight_number, departure_time, arrival_time, status, route_id, AircraftTypeID) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, f.getAirline().getAirlineId());
            ps.setString(2, f.getFlightNumber());
            ps.setTimestamp(3, f.getDepartureTime());
            ps.setTimestamp(4, f.getArrivalTime());
            ps.setString(5, f.getStatus());
            ps.setInt(6, f.getRoute().getRouteId());
            ps.setInt(7, f.getAircraftType().getAircraftTypeId());
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting flight", e);
            throw new RuntimeException("Failed to insert flight: " + e.getMessage());
        }
    }

    public void updateFlight(Flight f) {
        String sql = "UPDATE Flight SET airline_id = ?, flight_number = ?, departure_time = ?, arrival_time = ?, "
                + "status = ?, route_id = ?, AircraftTypeID = ? WHERE flight_id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, f.getAirline().getAirlineId());
            ps.setString(2, f.getFlightNumber());
            ps.setTimestamp(3, f.getDepartureTime());
            ps.setTimestamp(4, f.getArrivalTime());
            ps.setString(5, f.getStatus());
            ps.setInt(6, f.getRoute().getRouteId());
            ps.setInt(7, f.getAircraftType().getAircraftTypeId());
            ps.setInt(8, f.getFlightId());
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating flight: " + f.getFlightId(), e);
            throw new RuntimeException("Failed to update flight: " + e.getMessage());
        }
    }

    public void deleteFlight(int id) {
        String sql = "DELETE FROM Flight WHERE flight_id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting flight: " + id, e);
            throw new RuntimeException("Failed to delete flight: " + e.getMessage());
        }
    }

    public List<Flight> searchFlights(String origin, String destination, java.sql.Date departureDate) {
        List<Flight> flights = new ArrayList<>();
        String sql = "SELECT f.flight_id, f.flight_number, f.departure_time, f.arrival_time, f.status, "
                + "f.airline_id, a.name AS airline_name, a.code AS airline_code, "
                + "f.route_id, ao.iata_code AS origin_iata, ao.name AS origin_name, ao.city AS origin_city, "
                + "ad.iata_code AS dest_iata, ad.name AS dest_name, ad.city AS dest_city, "
                + "r.distance_km, r.duration_minutes, r.is_active, "
                + "f.AircraftTypeID, at.AircraftTypeName, at.AircraftTypeCode "
                + "FROM Flight f "
                + "JOIN Airline a ON a.airline_id = f.airline_id "
                + "LEFT JOIN Route r ON r.route_id = f.route_id "
                + "LEFT JOIN Airport ao ON ao.airport_id = r.origin_airport_id "
                + "LEFT JOIN Airport ad ON ad.airport_id = r.destination_airport_id "
                + "LEFT JOIN AircraftType at ON at.AircraftTypeID = f.AircraftTypeID "
                + "WHERE ao.city LIKE ? AND ad.city LIKE ? AND CAST(f.departure_time AS DATE) = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + origin + "%");
            ps.setString(2, "%" + destination + "%");
            ps.setDate(3, departureDate);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Flight flight = new Flight();
                    flight.setFlightId(rs.getInt("flight_id"));
                    flight.setFlightNumber(rs.getString("flight_number"));
                    flight.setDepartureTime(rs.getTimestamp("departure_time"));
                    flight.setArrivalTime(rs.getTimestamp("arrival_time"));
                    flight.setStatus(rs.getString("status"));
                    Airline airline = new Airline();
                    airline.setAirlineId(rs.getInt("airline_id"));
                    airline.setName(rs.getString("airline_name"));
                    airline.setCode(rs.getString("airline_code"));
                    flight.setAirline(airline);
                    Route route = new Route();
                    route.setRouteId(rs.getInt("route_id"));
                    route.setOriginIata(rs.getString("origin_iata"));
                    route.setOriginName(rs.getString("origin_name"));
                    route.setOriginCity(rs.getString("origin_city"));
                    route.setDestIata(rs.getString("dest_iata"));
                    route.setDestName(rs.getString("dest_name"));
                    route.setDestCity(rs.getString("dest_city"));
                    route.setDistanceKm(rs.getInt("distance_km"));
                    route.setDurationMinutes(rs.getInt("duration_minutes"));
                    route.setActive(rs.getBoolean("is_active"));
                    flight.setRoute(route);
                    AircraftType aircraftType = new AircraftType();
                    aircraftType.setAircraftTypeId(rs.getInt("AircraftTypeID"));
                    aircraftType.setAircraftTypeName(rs.getString("AircraftTypeName"));
                    aircraftType.setAircraftTypeCode(rs.getString("AircraftTypeCode"));
                    flight.setAircraftType(aircraftType);
                    flights.add(flight);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error searching flights", e);
        }
        return flights;
    }

    public List<Flight> getFlightsToday() {
        List<Flight> flights = new ArrayList<>();
        String sql = "SELECT f.flight_id, f.flight_number, f.departure_time, f.arrival_time, f.status, "
                + "f.airline_id, a.name AS airline_name, a.code AS airline_code, "
                + "f.route_id, ao.iata_code AS origin_iata, ao.name AS origin_name, ao.city AS origin_city, "
                + "ad.iata_code AS dest_iata, ad.name AS dest_name, ad.city AS dest_city, "
                + "r.distance_km, r.duration_minutes, r.is_active, "
                + "f.AircraftTypeID, at.AircraftTypeName, at.AircraftTypeCode "
                + "FROM Flight f "
                + "JOIN Airline a ON a.airline_id = f.airline_id "
                + "LEFT JOIN Route r ON r.route_id = f.route_id "
                + "LEFT JOIN Airport ao ON ao.airport_id = r.origin_airport_id "
                + "LEFT JOIN Airport ad ON ad.airport_id = r.destination_airport_id "
                + "LEFT JOIN AircraftType at ON at.AircraftTypeID = f.AircraftTypeID "
                + "WHERE CONVERT(DATE, f.departure_time) = CONVERT(DATE, GETDATE())";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Flight flight = new Flight();
                flight.setFlightId(rs.getInt("flight_id"));
                flight.setFlightNumber(rs.getString("flight_number"));
                flight.setDepartureTime(rs.getTimestamp("departure_time"));
                flight.setArrivalTime(rs.getTimestamp("arrival_time"));
                flight.setStatus(rs.getString("status"));
                Airline airline = new Airline();
                airline.setAirlineId(rs.getInt("airline_id"));
                airline.setName(rs.getString("airline_name"));
                airline.setCode(rs.getString("airline_code"));
                flight.setAirline(airline);
                Route route = new Route();
                route.setRouteId(rs.getInt("route_id"));
                route.setOriginIata(rs.getString("origin_iata"));
                route.setOriginName(rs.getString("origin_name"));
                route.setOriginCity(rs.getString("origin_city"));
                route.setDestIata(rs.getString("dest_iata"));
                route.setDestName(rs.getString("dest_name"));
                route.setDestCity(rs.getString("dest_city"));
                route.setDistanceKm(rs.getInt("distance_km"));
                route.setDurationMinutes(rs.getInt("duration_minutes"));
                route.setActive(rs.getBoolean("is_active"));
                flight.setRoute(route);
                AircraftType aircraftType = new AircraftType();
                aircraftType.setAircraftTypeId(rs.getInt("AircraftTypeID"));
                aircraftType.setAircraftTypeName(rs.getString("AircraftTypeName"));
                aircraftType.setAircraftTypeCode(rs.getString("AircraftTypeCode"));
                flight.setAircraftType(aircraftType);
                flights.add(flight);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving today's flights", e);
        }
        return flights;
    }

    public List<Date> getDistinctDepartureDates() {
        List<Date> departureDates = new ArrayList<>();
        String sql = "SELECT DISTINCT CAST(departure_time AS DATE) AS departure_date FROM Flight WHERE departure_time >= ? AND status = 'Scheduled' ORDER BY departure_date";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    departureDates.add(rs.getDate("departure_date"));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving distinct departure dates", e);
        }
        return departureDates;
    }

    public List<Flight> searchFlightsByRoute(int routeId, Date departureDate) {
        List<Flight> flights = new ArrayList<>();
        String sql = "SELECT f.flight_id, f.flight_number, f.departure_time, f.arrival_time, f.status, "
                + "f.airline_id, a.name AS airline_name, a.code AS airline_code, "
                + "f.route_id, ao.iata_code AS origin_iata, ao.name AS origin_name, ao.city AS origin_city, "
                + "ad.iata_code AS dest_iata, ad.name AS dest_name, ad.city AS dest_city, "
                + "r.distance_km, r.duration_minutes, r.is_active, "
                + "f.AircraftTypeID, at.AircraftTypeName, at.AircraftTypeCode "
                + "FROM Flight f "
                + "JOIN Airline a ON a.airline_id = f.airline_id "
                + "JOIN Route r ON r.route_id = f.route_id "
                + "JOIN Airport ao ON ao.airport_id = r.origin_airport_id "
                + "JOIN Airport ad ON ad.airport_id = r.destination_airport_id "
                + "JOIN AircraftType at ON at.AircraftTypeID = f.AircraftTypeID "
                + "WHERE f.route_id = ? AND CAST(f.departure_time AS DATE) = ? AND f.status != 'CANCELLED'";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, routeId);
            ps.setDate(2, departureDate);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Flight flight = new Flight();
                    flight.setFlightId(rs.getInt("flight_id"));
                    flight.setFlightNumber(rs.getString("flight_number"));
                    flight.setDepartureTime(rs.getTimestamp("departure_time"));
                    flight.setArrivalTime(rs.getTimestamp("arrival_time"));
                    flight.setStatus(rs.getString("status"));
                    Airline airline = new Airline();
                    airline.setAirlineId(rs.getInt("airline_id"));
                    airline.setName(rs.getString("airline_name"));
                    airline.setCode(rs.getString("airline_code"));
                    flight.setAirline(airline);
                    Route route = new Route();
                    route.setRouteId(rs.getInt("route_id"));
                    route.setOriginIata(rs.getString("origin_iata"));
                    route.setOriginName(rs.getString("origin_name"));
                    route.setOriginCity(rs.getString("origin_city"));
                    route.setDestIata(rs.getString("dest_iata"));
                    route.setDestName(rs.getString("dest_name"));
                    route.setDestCity(rs.getString("dest_city"));
                    route.setDistanceKm(rs.getInt("distance_km"));
                    route.setDurationMinutes(rs.getInt("duration_minutes"));
                    route.setActive(rs.getBoolean("is_active"));
                    flight.setRoute(route);
                    AircraftType aircraftType = new AircraftType();
                    aircraftType.setAircraftTypeId(rs.getInt("AircraftTypeID"));
                    aircraftType.setAircraftTypeName(rs.getString("AircraftTypeName"));
                    aircraftType.setAircraftTypeCode(rs.getString("AircraftTypeCode"));
                    flight.setAircraftType(aircraftType);
                    flights.add(flight);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error searching flights by route", e);
        }
        return flights;
    }
}
