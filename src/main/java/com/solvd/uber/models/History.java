package com.solvd.uber.models;

import com.solvd.uber.enums.RideStatus;
import java.time.LocalDateTime;

public class History {
    private long id;
    private Ride ride = new Ride();
    private Account account = new Account();
    private RideStatus status;
    private LocalDateTime changedAt;

    public History() {}

    public History(long id, Ride ride, Account account, RideStatus status, LocalDateTime changedAt) {
        this.id = id;
        this.ride = ride;
        this.account = account;
        this.status = status;
        this.changedAt = changedAt;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public Ride getRide() { return ride; }
    public void setRide(Ride ride) { this.ride = ride; }

    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }

    public RideStatus getStatus() { return status; }
    public void setStatus(RideStatus status) { this.status = status; }

    public LocalDateTime getChangedAt() { return changedAt; }
    public void setChangedAt(LocalDateTime changedAt) { this.changedAt = changedAt; }
}
