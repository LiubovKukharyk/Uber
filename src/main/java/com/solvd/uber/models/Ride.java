package com.solvd.uber.models;

import com.solvd.uber.enums.RideStatus;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

import java.time.LocalDateTime;

@XmlRootElement(name = "ride")
public class Ride {
    private long id;
    private Person passenger = new Person();
    private Person driver = new Person();
    private Service service = new Service();
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private RideStatus status;  
    private Promo promo = new Promo();

    public Ride() {}

    public Ride(long id, Person passenger, Person driver, Service service,
                LocalDateTime startTime, LocalDateTime endTime, RideStatus status, Promo promo) {
        this.id = id;
        this.passenger = passenger;
        this.driver = driver;
        this.service = service;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.promo = promo;
    }

    @XmlElement
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    @XmlElement
    public Person getPassenger() { return passenger; }
    public void setPassenger(Person passenger) { this.passenger = passenger; }

    @XmlElement
    public Person getDriver() { return driver; }
    public void setDriver(Person driver) { this.driver = driver; }

    @XmlTransient
    public long getDriverId() { return this.driver != null ? this.driver.getId() : 0; }

    @XmlElement
    public Service getService() { return service; }
    public void setService(Service service) { this.service = service; }

    @XmlTransient
    public long getServiceId() { return this.service != null ? this.service.getId() : 0; }

    @XmlElement
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    @XmlElement
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    @XmlElement
    public RideStatus getStatus() { return status; }
    public void setStatus(RideStatus status) { this.status = status; }

    @XmlElement
    public Promo getPromo() { return promo; }
    public void setPromo(Promo promo) { this.promo = promo; }
}
