/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Timestamp;

/**
 *
 * @author $ LienXuanThinh - CE182117
 */
public class Flight {

    private int flightId;
    private int airlineId;
    private String flightNumber;
    private String routeFrom;
    private String routeTo;
    private Timestamp departureTime;
    private Timestamp arrivalTime;
    private double price;
    private String aircraft;
    private String status;
    private double priceDeluxe;
    private double priceSkyboss;
    private double priceBusiness;

    public Flight() {
    }

    public Flight(int flightId, int airlineId, String flightNumber, String routeFrom, String routeTo, Timestamp departureTime, Timestamp arrivalTime, double price, String aircraft, String status, double priceDeluxe, double priceSkyboss, double priceBusiness) {
        this.flightId = flightId;
        this.airlineId = airlineId;
        this.flightNumber = flightNumber;
        this.routeFrom = routeFrom;
        this.routeTo = routeTo;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.price = price;
        this.aircraft = aircraft;
        this.status = status;
        this.priceDeluxe = priceDeluxe;
        this.priceSkyboss = priceSkyboss;
        this.priceBusiness = priceBusiness;
    }



    public int getFlightId() {
        return flightId;
    }

    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    public int getAirlineId() {
        return airlineId;
    }

    public void setAirlineId(int airlineId) {
        this.airlineId = airlineId;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getRouteFrom() {
        return routeFrom;
    }

    public void setRouteFrom(String routeFrom) {
        this.routeFrom = routeFrom;
    }

    public String getRouteTo() {
        return routeTo;
    }

    public void setRouteTo(String routeTo) {
        this.routeTo = routeTo;
    }

    public Timestamp getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Timestamp departureTime) {
        this.departureTime = departureTime;
    }

    public Timestamp getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Timestamp arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getAircraft() {
        return aircraft;
    }

    public void setAircraft(String aircraft) {
        this.aircraft = aircraft;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getPriceDeluxe() {
        return priceDeluxe;
    }

    public void setPriceDeluxe(double priceDeluxe) {
        this.priceDeluxe = priceDeluxe;
    }

    public double getPriceSkyboss() {
        return priceSkyboss;
    }

    public void setPriceSkyboss(double priceSkyboss) {
        this.priceSkyboss = priceSkyboss;
    }

    public double getPriceBusiness() {
        return priceBusiness;
    }

    public void setPriceBusiness(double priceBusiness) {
        this.priceBusiness = priceBusiness;
    }
}
