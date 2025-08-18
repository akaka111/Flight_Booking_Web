package model;

public class Route {
    private int routeId;
    private String originIata;
    private String originName;
    private String destIata;
    private String destName;
    private Integer distanceKm;
    private Integer durationMinutes;
    private boolean active;

    public int getRouteId() { return routeId; }
    public void setRouteId(int routeId) { this.routeId = routeId; }

    public String getOriginIata() { return originIata; }
    public void setOriginIata(String originIata) { this.originIata = originIata; }

    public String getOriginName() { return originName; }
    public void setOriginName(String originName) { this.originName = originName; }

    public String getDestIata() { return destIata; }
    public void setDestIata(String destIata) { this.destIata = destIata; }

    public String getDestName() { return destName; }
    public void setDestName(String destName) { this.destName = destName; }

    public Integer getDistanceKm() { return distanceKm; }
    public void setDistanceKm(Integer distanceKm) { this.distanceKm = distanceKm; }

    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
