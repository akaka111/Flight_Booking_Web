package model;

import java.math.BigDecimal;

public class TicketClass {

    private int classId;
    private int flightId;
    private BigDecimal price;
    private int seatClassId;
    private String className;

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public int getFlightId() {
        return flightId;
    }

    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getSeatClassId() {
        return seatClassId;
    }

    public void setSeatClassId(int seatClassId) {
        this.seatClassId = seatClassId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
