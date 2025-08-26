package model;

import java.io.Serializable;
import java.sql.Timestamp;

/** Ánh xạ bảng dbo.Flight */
public class Flight implements Serializable {
    private int flightId;
    private Integer airlineId;       // có thể null
    private String flightNumber;     // map từ cột flight_num
    private Timestamp departureTime;
    private Timestamp arrivalTime;
    private String status;
    private Integer routeId;         // có thể null
    private Integer aircraftType;    // có thể null

    public int getFlightId() { return flightId; }
    public void setFlightId(int flightId) { this.flightId = flightId; }

    public Integer getAirlineId() { return airlineId; }
    public void setAirlineId(Integer airlineId) { this.airlineId = airlineId; }

    public String getFlightNumber() { return flightNumber; }
    public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }

    public Timestamp getDepartureTime() { return departureTime; }
    public void setDepartureTime(Timestamp departureTime) { this.departureTime = departureTime; }

    public Timestamp getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(Timestamp arrivalTime) { this.arrivalTime = arrivalTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getRouteId() { return routeId; }
    public void setRouteId(Integer routeId) { this.routeId = routeId; }

    public Integer getAircraftType() { return aircraftType; }
    public void setAircraftType(Integer aircraftType) { this.aircraftType = aircraftType; }
}
