package model;

public class AircraftSeatConfig {
    private int configID;
    private int aircraftTypeID;
    private int seatClassID;
    private int seatCount;

    // Constructor
    public AircraftSeatConfig() {
    }

    // Getters and Setters
    public int getConfigID() {
        return configID;
    }

    public void setConfigID(int configID) {
        this.configID = configID;
    }

    public int getAircraftTypeID() {
        return aircraftTypeID;
    }

    public void setAircraftTypeID(int aircraftTypeID) {
        this.aircraftTypeID = aircraftTypeID;
    }

    public int getSeatClassID() {
        return seatClassID;
    }

    public void setSeatClassID(int seatClassID) {
        this.seatClassID = seatClassID;
    }

    public int getSeatCount() {
        return seatCount;
    }

    public void setSeatCount(int seatCount) {
        this.seatCount = seatCount;
    }
}