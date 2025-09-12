package com.solvd.uber.models;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "rides")
public class RideListWrapper {

    private List<Ride> rides;

    public RideListWrapper() {}

    public RideListWrapper(List<Ride> rides) {
        this.rides = rides;
    }

    @XmlElement(name = "ride")
    public List<Ride> getRides() { return rides; }
    public void setRides(List<Ride> rides) { this.rides = rides; }
}
