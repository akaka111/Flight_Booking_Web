package DAO.Admin;

import model.Route;
import utils.DBContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RouteDAO extends DBContext {
    // Lấy danh sách tất cả các tuyến đường
    public List<Route> getAllRoutes() {
        List<Route> routes = new ArrayList<>();
        String sql = "SELECT r.route_id, ao.iata_code AS origin_iata, ao.name AS origin_name, ao.city AS origin_city, " +
                     "ad.iata_code AS dest_iata, ad.name AS dest_name, ad.city AS dest_city, " +
                     "r.distance_km, r.duration_minutes, r.is_active " +
                     "FROM Route r " +
                     "JOIN Airport ao ON ao.airport_id = r.origin_airport_id " +
                     "JOIN Airport ad ON ad.airport_id = r.destination_airport_id " +
                     "WHERE r.is_active = 1";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
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
                routes.add(route);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return routes;
    }

    // Lấy thông tin tuyến đường theo ID
    public Route getRouteById(int routeId) {
        Route route = null;
        String sql = "SELECT r.route_id, ao.iata_code AS origin_iata, ao.name AS origin_name, ao.city AS origin_city, " +
                     "ad.iata_code AS dest_iata, ad.name AS dest_name, ad.city AS dest_city, " +
                     "r.distance_km, r.duration_minutes, r.is_active " +
                     "FROM Route r " +
                     "JOIN Airport ao ON ao.airport_id = r.origin_airport_id " +
                     "JOIN Airport ad ON ad.airport_id = r.destination_airport_id " +
                     "WHERE r.route_id = ? AND r.is_active = 1";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, routeId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    route = new Route();
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
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return route;
    }
}