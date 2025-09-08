package com.solvd.uber.models;

import java.time.LocalDateTime;

public class Feedback {
    private long id;
    private Rating rating = new Rating();
    private String comment;
    private LocalDateTime createdAt;

    public Feedback() {}

    public Feedback(long id, Rating rating, String comment, LocalDateTime createdAt) {
        this.id = id;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public Rating getRating() { return rating; }
    public void setRating(Rating rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
