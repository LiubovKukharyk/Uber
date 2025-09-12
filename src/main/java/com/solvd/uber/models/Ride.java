package com.solvd.uber.models;

import com.solvd.uber.enums.RideStatus;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

@XmlRootElement(name = "ride")
public class Ride {
    @JsonProperty("id")
    private long id;

    @JsonProperty("passenger")
    private Person passenger; 

    @JsonProperty("driver")
    private Person driver;

    @JsonProperty("service")
    private Service service;

    @JsonProperty("startTime")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startTime;

    @JsonProperty("endTime")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endTime;

    @JsonProperty("status")
    private RideStatus status;

    @JsonProperty("promo")
    private Promo promo;

    public Ride() {}

    @XmlElement
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    @XmlElement
    public Person getPassenger() { return passenger; }
    public void setPassenger(Person passenger) { this.passenger = passenger; }

    @XmlElement
    public Person getDriver() { return driver; }
    public void setDriver(Person driver) { this.driver = driver; }

    @XmlElement
    public Service getService() { return service; }
    public void setService(Service service) { this.service = service; }

    @XmlElement
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    @XmlElement
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    @XmlElement
    public RideStatus getStatus() { return status; }
    public void setStatus(RideStatus status) { this.status = status; }

    @XmlElement
    public Promo getPromo() { return promo; }
    public void setPromo(Promo promo) { this.promo = promo; }
}
