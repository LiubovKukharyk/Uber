package com.solvd.uber.services;

import com.solvd.uber.models.Promo;
import com.solvd.uber.models.Person;
import com.solvd.uber.services.interfaces.IPromotionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PromotionService implements IPromotionService {

    private static final Logger LOGGER = LogManager.getLogger();

    private String promoCode;
    private int rideCount;
    private Person person;
    private Promo promo;

    public PromotionService() {}

    public PromotionService(String promoCode, int rideCount, Person person, Promo promo) {
        this.promoCode = promoCode;
        this.rideCount = rideCount;
        this.person = person;
        this.promo = promo;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public int getRideCount() {
        return rideCount;
    }

    public void setRideCount(int rideCount) {
        this.rideCount = rideCount;
    }

    private boolean isValidPromoCode() {
        if (promo == null || promoCode == null) return false;
        return promoCode.equalsIgnoreCase(promo.getCode());
    }

    @Override
    public double calculateDiscount() {
        boolean firstRide = rideCount == 1;
        boolean everyThirdRide = rideCount > 0 && rideCount % 3 == 0;
        boolean validPromo = isValidPromoCode();

        if (firstRide) return 0.20;
        if (everyThirdRide) return 0.10; 
        if (validPromo) return promo.getDiscountPercent();

        return 0.0;
    }

    public void printPromotionDetails() {
        LOGGER.info("User: " + person.getFirstName() + " " + person.getLastName());
        LOGGER.info("Ride Count: " + rideCount);
        LOGGER.info("Promo Code Entered: " + promoCode);
        LOGGER.info("Discount Applied: " + (calculateDiscount() * 100) + "%");
        LOGGER.info("Promo isVoucher: " + (promo != null && promo.isVoucher()));
    }
}
