package com.solvd.uber.models;

public class Vehicle {
    private long id;
    private Person owner = new Person(); // driver
    private String brand;
    private String modelName;
    private int year;
    private String plateNumber;

    public Vehicle() {}

    public Vehicle(long id, Person owner, String brand, String modelName, int year, String plateNumber) {
        this.id = id;
        this.owner = owner;
        this.brand = brand;
        this.modelName = modelName;
        this.year = year;
        this.plateNumber = plateNumber;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public Person getOwner() { return owner; }
    public void setOwner(Person owner) { this.owner = owner; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getModelName() { return modelName; }
    public void setModelName(String modelName) { this.modelName = modelName; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public String getPlateNumber() { return plateNumber; }
    public void setPlateNumber(String plateNumber) { this.plateNumber = plateNumber; }
}
