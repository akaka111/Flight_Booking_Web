package model;

public class Airport {
    private int airportId;
    private String iataCode;   // 3 chars
    private String icaoCode;   // 4 chars (nullable)
    private String name;
    private String city;
    private String country;
    private String timezone;   // nullable
    private boolean active;

    public Airport() {}

    public int getAirportId() { return airportId; }
    public void setAirportId(int airportId) { this.airportId = airportId; }

    public String getIataCode() { return iataCode; }
    public void setIataCode(String iataCode) { this.iataCode = iataCode; }

    public String getIcaoCode() { return icaoCode; }
    public void setIcaoCode(String icaoCode) { this.icaoCode = icaoCode; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getTimezone() { return timezone; }
    public void setTimezone(String timezone) { this.timezone = timezone; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
