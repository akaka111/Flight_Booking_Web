package model;

public class SeatClass {

    private int seatClassID;
    private String name;
    private String description;
    private String status;

    public SeatClass(int seatClassID, String name, String description, String status) {
        this.seatClassID = seatClassID;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    
    public SeatClass() {
    }

    // Getters and Setters
    public int getSeatClassID() {
        return seatClassID;
    }

    public void setSeatClassID(int seatClassID) {
        this.seatClassID = seatClassID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
