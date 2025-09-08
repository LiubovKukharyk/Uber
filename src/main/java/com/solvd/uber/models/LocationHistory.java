package com.solvd.uber.models;

import java.time.LocalDateTime;

public class LocationHistory {
    private long id;
    private Person person = new Person();
    private double latitude;
    private double longitude;
    private LocalDateTime recordedAt;

    public LocationHistory() {}

    public LocationHistory(long id, Person person, double latitude, double longitude, LocalDateTime recordedAt) {
        this.id = id;
        this.person = person;
        this.latitude = latitude;
        this.longitude = longitude;
        this.recordedAt = recordedAt;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public Person getPerson() { return person; }
    public void setPerson(Person person) { this.person = person; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public LocalDateTime getRecordedAt() { return recordedAt; }
    public void setRecordedAt(LocalDateTime recordedAt) { this.recordedAt = recordedAt; }
}
