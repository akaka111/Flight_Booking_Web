/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author $ LienXuanThinh - CE182117
 */
public class TicketClass {

    private int classId;
    private int flightId;
    private String className;
    private double price;

    public TicketClass() {
    }

    public TicketClass(int classId, int flightId, String className, double price) {
        this.classId = classId;
        this.flightId = flightId;
        this.className = className;
        this.price = price;
    }

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

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
