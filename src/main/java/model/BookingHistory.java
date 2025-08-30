/*
 * Click nbfs://.netbeans.org/templates/license.txt to change this license
 * Click nfs://NetBeans/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 *
 * @author Khoa
 */
public class BookingHistory {
    private int historyId;
    private int bookingId;
    private String action;
    private String description;
    private Timestamp actionTime;
    private String role; // Thêm vai trò
    private String UserName; // Thêm tên nhân viên
    private int userId;

    

    // Constructor
    public BookingHistory() {}

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    
    // Getter và Setter
    public int getHistoryId() {
        return historyId;
    }

    public void setHistoryId(int historyId) {
        this.historyId = historyId;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getActionTime() {
        return actionTime;
    }

    public void setActionTime(Timestamp actionTime) {
        this.actionTime = actionTime;
    }

    public String getRole() { // Thêm getter cho vai trò
        return role;
    }

    public void setRole(String role) { // Thêm setter cho vai trò
        this.role = role;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    
}