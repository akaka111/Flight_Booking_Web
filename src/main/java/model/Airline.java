/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 * Represents an Airline entity with attributes matching the Airline table in the database.
 * @author LienXuanThinh - CE182117
 */
public class Airline {
    private int airlineId;
    private String name;
    private String code;
    private String description;
    private String services;

    // Default constructor
    public Airline() {
    }

    // Parameterized constructor
    public Airline(int airlineId, String name, String code, String description, String services) {
        this.airlineId = airlineId;
        this.name = name;
        this.code = code;
        this.description = description;
        this.services = services;
    }

    // Getters and Setters
    public int getAirlineId() {
        return airlineId;
    }

    public void setAirlineId(int airlineId) {
        this.airlineId = airlineId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    // toString for debugging
    @Override
    public String toString() {
        return "Airline{" +
                "airlineId=" + airlineId +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", services='" + services + '\'' +
                '}';
    }
}