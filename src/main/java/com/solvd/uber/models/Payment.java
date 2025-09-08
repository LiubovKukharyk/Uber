package com.solvd.uber.models;

import java.time.LocalDateTime;

public class Payment {
    private long id;
    private Wallet wallet = new Wallet();
    private Ride ride = new Ride();
    private String method;
    private double amount;
    private String status;
    private LocalDateTime createdAt;

    public Payment() {}

    public Payment(long id, Wallet wallet, Ride ride, String method, double amount, String status, LocalDateTime createdAt) {
        this.id = id;
        this.wallet = wallet;
        this.ride = ride;
        this.method = method;
        this.amount = amount;
        this.status = status;
        this.createdAt = createdAt;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public Wallet getWallet() { return wallet; }
    public void setWallet(Wallet wallet) { this.wallet = wallet; }

    public Ride getRide() { return ride; }
    public void setRide(Ride ride) { this.ride = ride; }

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
