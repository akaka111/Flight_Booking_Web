/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Khoa
 */
public class SeatClass {
    private int seatClassId;
    private String name;
    private String description;
    private String status;

    public SeatClass() {
    }
    public SeatClass(int seatClassId, String name, String description, String status) {
        this.seatClassId = seatClassId;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public int getSeatClassId() {
        return seatClassId;
    }

    public void setSeatClassId(int seatClassId) {
        this.seatClassId = seatClassId;
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
