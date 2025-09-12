package com.solvd.uber.models;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

@XmlRootElement(name = "promo")
public class Promo {
    private long id;
    private String code;
    private Double discountPercent;
    private Double discountAmount;
    private boolean voucher;

    @JsonProperty("validUntil")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date validUntil;

    @JsonProperty("createdAt")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date createdAt;

    public Promo() {}

    @XmlElement
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    @XmlElement
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    @XmlElement
    public Double getDiscountPercent() { return discountPercent; }
    public void setDiscountPercent(Double discountPercent) { this.discountPercent = discountPercent; }

    @XmlElement
    public Double getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(Double discountAmount) { this.discountAmount = discountAmount; }

    @XmlElement
    public boolean isVoucher() { return voucher; }
    public void setVoucher(boolean voucher) { this.voucher = voucher; }

    @XmlElement
    public Date getValidUntil() { return validUntil; }
    public void setValidUntil(Date validUntil) { this.validUntil = validUntil; }

    @XmlElement
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}
