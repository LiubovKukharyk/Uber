package com.solvd.uber.models;

public class SavedPlace {
    private long id;
    private Account account = new Account();
    private String label;
    private double latitude;
    private double longitude;

    public SavedPlace() {}

    public SavedPlace(long id, Account account, String label, double latitude, double longitude) {
        this.id = id;
        this.account = account;
        this.label = label;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
}
