package com.solvd.uber.models;

public class Wallet {
    private long id;
    private Person person = new Person();
    private double balance;

    public Wallet() {}

    public Wallet(long id, Person person, double balance) {
        this.id = id;
        this.person = person;
        this.balance = balance;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public Person getPerson() { return person; }
    public void setPerson(Person person) { this.person = person; }

    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
}
