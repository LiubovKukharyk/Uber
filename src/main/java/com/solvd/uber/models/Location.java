package com.solvd.uber.models;

import com.solvd.uber.enums.LocationType;

public class Location {
    private long id;
    private Ride ride = new Ride();
    private LocationType locationType;
    private double latitude;
    private double longitude;

    public Location() {}

    public Location(long id, Ride ride, LocationType locationType, double latitude, double longitude) {
        this.id = id;
        this.ride = ride;
        this.locationType = locationType;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public Ride getRide() { return ride; }
    public void setRide(Ride ride) { this.ride = ride; }

    public LocationType getLocationType() { return locationType; }
    public void setLocationType(LocationType locationType) { this.locationType = locationType; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
}
