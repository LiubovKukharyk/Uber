package com.solvd.uber.models;

import java.time.LocalDateTime;

public class Promo {
    private long id;
    private Person person = new Person();
    private String code;
    private boolean isVoucher;
    private Double discountPercent;
    private Double discountAmount;
    private LocalDateTime validUntil;
    private LocalDateTime createdAt;

    public Promo() {}

    public Promo(long id, Person person, String code, boolean isVoucher, Double discountPercent, Double discountAmount, LocalDateTime validUntil, LocalDateTime createdAt) {
        this.id = id;
        this.person = person;
        this.code = code;
        this.isVoucher = isVoucher;
        this.discountPercent = discountPercent;
        this.discountAmount = discountAmount;
        this.validUntil = validUntil;
        this.createdAt = createdAt;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public Person getPerson() { return person; }
    public void setPerson(Person person) { this.person = person; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public boolean isVoucher() { return isVoucher; }
    public void setVoucher(boolean voucher) { isVoucher = voucher; }

    public Double getDiscountPercent() { return discountPercent; }
    public void setDiscountPercent(Double discountPercent) { this.discountPercent = discountPercent; }

    public Double getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(Double discountAmount) { this.discountAmount = discountAmount; }

    public LocalDateTime getValidUntil() { return validUntil; }
    public void setValidUntil(LocalDateTime validUntil) { this.validUntil = validUntil; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
