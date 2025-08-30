/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 *
 * @author $ LienXuanThinh - CE182117
 */
public class Booking {

    private int bookingId;
    private int userId;
    private int flightId;
    private Timestamp bookingDate; // Sử dụng Timestamp để khớp với datetime
    private String status;
    private String seatClass; // Thêm thuộc tính này
    private double totalPrice;  // Thêm thuộc tính này
    private String staffNote;
    private Integer lastUpdatedBy;
    private Timestamp lastUpdatedAt;
    private String checkinStatus;
    private String bookingCode;
    private String voucherCode;
    private int seatId;
    private String userFullName; // Thêm thuộc tính cho tên người dùng
    private String flightNumber; // Thêm thuộc tính cho số hiệu chuyến bay
    private Seat seat;

    public Booking() {
    }

    public Booking(int bookingId, int userId, int flightId, Timestamp bookingDate, String status, String seatClass, double totalPrice, String staffNote, Integer lastUpdatedBy, Timestamp lastUpdatedAt, String checkinStatus, String bookingCode, String voucherCode, int seatId, String userFullName, String flightNumber, Seat seat) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.flightId = flightId;
        this.bookingDate = bookingDate;
        this.status = status;
        this.seatClass = seatClass;
        this.totalPrice = totalPrice;
        this.staffNote = staffNote;
        this.lastUpdatedBy = lastUpdatedBy;
        this.lastUpdatedAt = lastUpdatedAt;
        this.checkinStatus = checkinStatus;
        this.bookingCode = bookingCode;
        this.voucherCode = voucherCode;
        this.seatId = seatId;
        this.userFullName = userFullName;
        this.flightNumber = flightNumber;
        this.seat = seat;
    }

   

    // Getters and Setters
    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getFlightId() {
        return flightId;
    }

    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    public Timestamp getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Timestamp bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSeatClass() {
        return seatClass;
    }

    public void setSeatClass(String seatClass) {
        this.seatClass = seatClass;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStaffNote() {
        return staffNote;
    }

    public void setStaffNote(String staffNote) {
        this.staffNote = staffNote;
    }

    public Integer getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(Integer lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Timestamp getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(Timestamp lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public String getCheckinStatus() {
        return checkinStatus;
    }

    public void setCheckinStatus(String checkinStatus) {
        this.checkinStatus = checkinStatus;
    }

    public String getBookingCode() {
        return bookingCode;
    }

    public void setBookingCode(String bookingCode) {
        this.bookingCode = bookingCode;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public int getSeatId() {
        return seatId;
    }

    public void setSeatId(int seatId) {
        this.seatId = seatId;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public Seat getSeat() {
        return seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }
    
    

    @Override
    public String toString() {
        return "Booking{"
                + "bookingCode='" + bookingCode + '\''
                + ", flightId=" + flightId
                + '}';
    }

}
