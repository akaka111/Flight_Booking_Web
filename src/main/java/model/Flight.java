    package model;

    import java.sql.Timestamp;

    public class Flight {
        private int flightId;
        private String flightNumber;
        private Timestamp departureTime;
        private Timestamp arrivalTime;
        private String status;
        private Airline airline; // Thay vì int airlineId
        private Route route; // Thay vì int routeId
        private AircraftType aircraftType; // Thay vì int aircraftTypeId

        // Constructor
        public Flight() { 
        }

        // Getters and Setters
        public int getFlightId() {
            return flightId;
        }

        public void setFlightId(int flightId) {
            this.flightId = flightId;
        }

        public String getFlightNumber() {
            return flightNumber;
        }

        public void setFlightNumber(String flightNumber) {
            this.flightNumber = flightNumber;
        }

        public Timestamp getDepartureTime() {
            return departureTime;
        }

        public void setDepartureTime(Timestamp departureTime) {
            this.departureTime = departureTime;
        }

        public Timestamp getArrivalTime() {
            return arrivalTime;
        }

        public void setArrivalTime(Timestamp arrivalTime) {
            this.arrivalTime = arrivalTime;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Airline getAirline() {
            return airline;
        }

        public void setAirline(Airline airline) {
            this.airline = airline;
        }

        public Route getRoute() {
            return route;
        }

        public void setRoute(Route route) {
            this.route = route;
        }

        public AircraftType getAircraftType() {
            return aircraftType;
        }

        public void setAircraftType(AircraftType aircraftType) {
            this.aircraftType = aircraftType;
        }
    }