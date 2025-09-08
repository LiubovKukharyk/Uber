package com.solvd.uber.models;

import java.time.LocalDateTime;

public class Rating {
    private long id;
    private Ride ride = new Ride();
    private Person rater = new Person();
    private Person ratee = new Person();
    private int score;
    private LocalDateTime createdAt;

    public Rating() {}

    public Rating(long id, Ride ride, Person rater, Person ratee, int score, LocalDateTime createdAt) {
        this.id = id;
        this.ride = ride;
        this.rater = rater;
        this.ratee = ratee;
        this.score = score;
        this.createdAt = createdAt;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public Ride getRide() { return ride; }
    public void setRide(Ride ride) { this.ride = ride; }

    public Person getRater() { return rater; }
    public void setRater(Person rater) { this.rater = rater; }

    public Person getRatee() { return ratee; }
    public void setRatee(Person ratee) { this.ratee = ratee; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
