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
    private boolean isBooked;
    private SeatClass seatClass;

    public Seat() {
    }

    public Seat(int seatId, int flightId, int classId, String seatNumber, boolean isBooked) {
        this.seatId = seatId;
        this.flightId = flightId;
        this.classId = classId;
        this.seatNumber = seatNumber;
        this.isBooked = isBooked;
    }
    
    public Seat(int seatId, int flightId, int classId, String seatNumber, boolean isBooked, SeatClass seatClass) {
        this.seatId = seatId;
        this.flightId = flightId;
        this.classId = classId;
        this.seatNumber = seatNumber;
        this.isBooked = isBooked;
        this.seatClass = seatClass;
    }

    
    

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

    public boolean isIsBooked() {
        return isBooked;
    }

    public void setIsBooked(boolean isBooked) {
        this.isBooked = isBooked;
    }

    public SeatClass getSeatClass() {
        return seatClass;
    }

    public void setSeatClass(SeatClass seatClass) {
        this.seatClass = seatClass;
    }
    
    
}
