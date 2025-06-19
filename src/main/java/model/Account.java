/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Date;

/**
 *
 * @author $ LienXuanThinh - CE182117
 */
public class Account {

    private int userId;
    private String username;
    private String password;
    private String email;
    private String phone;
    private Date dob;
    private String role;
    private boolean status;

    // Constructors
    public Account() {
    }

    public Account(int userId, String username, String password, String email, String phone, Date dob, String role, boolean status) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.dob = dob;
        this.role = role;
        this.status = status;
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    // toString() for debugging
    @Override
    public String toString() {
        return "Account{"
                + "userId=" + userId
                + ", username='" + username + '\''
                + ", email='" + email + '\''
                + ", phone='" + phone + '\''
                + ", dob=" + dob
                + ", role='" + role + '\''
                + ", status=" + status
                + '}';
    }
}
