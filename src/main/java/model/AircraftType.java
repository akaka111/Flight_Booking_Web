package model;

    public class AircraftType {
    private int aircraftTypeId;
    private String aircraftTypeCode;
    private String aircraftTypeName;
    private String status;
    private Airline airlineId;

    // Constructor
    public AircraftType() {
    }

    // Getters and Setters
    public int getAircraftTypeId() {
        return aircraftTypeId;
    }

    public void setAircraftTypeId(int aircraftTypeId) {
        this.aircraftTypeId = aircraftTypeId;
    }

    public String getAircraftTypeCode() {
        return aircraftTypeCode;
    }

    public void setAircraftTypeCode(String aircraftTypeCode) {
        this.aircraftTypeCode = aircraftTypeCode;
    }

    public String getAircraftTypeName() {
        return aircraftTypeName;
    }

    public void setAircraftTypeName(String aircraftTypeName) {
        this.aircraftTypeName = aircraftTypeName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Airline getAirlineId() {
        return airlineId;
    }

    public void setAirlineId(Airline airlineId) {
        this.airlineId = airlineId;
    }

   

   
}