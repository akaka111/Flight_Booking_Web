/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author $ LienXuanThinh - CE182117
 */
public class Seat {

    private int seatId;
    private int flightId;
    private int classId;
    private String seatNumber;

    public Seat() {
    }

    
    public Seat(int seatId, int flightId, int classId, String seatNumber, boolean booked) {
        this.seatId = seatId;
        this.flightId = flightId;
        this.classId = classId;
        this.seatNumber = seatNumber;
        this.booked = booked;
    }

    // ✅ sửa ở đây:
    private boolean booked;

    public int getSeatId() {
        return seatId;
    }

    public void setSeatId(int seatId) {
        this.seatId = seatId;
    }

    public int getFlightId() {
        return flightId;
    }

    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    // ✅ đúng chuẩn JavaBean
    public boolean isBooked() {
        return booked;
    }

    public void setBooked(boolean booked) {
        this.booked = booked;
    }
}
