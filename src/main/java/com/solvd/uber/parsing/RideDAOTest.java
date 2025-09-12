package com.solvd.uber.parsing;

import com.solvd.uber.dao.mysqlimpl.RideDAO;
import com.solvd.uber.models.Person;
import com.solvd.uber.models.Promo;
import com.solvd.uber.models.Ride;
import com.solvd.uber.models.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.util.List;

public class RideDAOTest {
    private static final Logger LOGGER = LogManager.getLogger("RidesLogger");

    public static void main(String[] args) {
        RideDAO rideDAO = new RideDAO();

        try (Connection conn = rideDAO.getConnection()) {
            if (conn != null && !conn.isClosed()) {
                LOGGER.info("Connection to database is successful.");
            } else {
                LOGGER.error("Failed to connect to database.");
                return;
            }
        } catch (Exception e) {
            LOGGER.error("Exception while connecting to database:", e);
            return;
        }

        try {
            List<Ride> rides = rideDAO.getAll();
            if (rides.isEmpty()) {
                LOGGER.warn("No rides found in database.");
            } else {
                LOGGER.info("Rides found: {}", rides.size());
                for (Ride ride : rides) {
                    LOGGER.info("-------------------------------------------------");
                    LOGGER.info("Ride ID: {}", ride.getId());

                    Person passenger = ride.getPassenger();
                    if (passenger != null) {
                        LOGGER.info("Passenger: {} {}", safe(passenger.getFirstName()), safe(passenger.getLastName()));
                    } else {
                        LOGGER.info("Passenger: NULL");
                    }

                    Person driver = ride.getDriver();
                    if (driver != null) {
                        LOGGER.info("Driver: {} {}", safe(driver.getFirstName()), safe(driver.getLastName()));
                    } else {
                        LOGGER.info("Driver: NULL");
                    }

                    Service service = ride.getService();
                    if (service != null) {
                        LOGGER.info("Service ID: {}, Option: {}", service.getId(), safe(service.getOptionName()));
                    } else {
                        LOGGER.info("Service: NULL");
                    }

                    Promo promo = ride.getPromo();
                    if (promo != null) {
                        LOGGER.info("Promo ID: {}, Code: {}, Voucher: {}, Discount %: {}, Discount amount: {}",
                                promo.getId(),
                                safe(promo.getCode()),
                                promo.isVoucher(),
                                promo.getDiscountPercent() != null ? promo.getDiscountPercent() : "NULL",
                                promo.getDiscountAmount() != null ? promo.getDiscountAmount() : "NULL"
                        );
                    } else {
                        LOGGER.info("Promo: NULL");
                    }

                    LOGGER.info("Start time: {}", ride.getStartTime() != null ? ride.getStartTime() : "NULL");
                    LOGGER.info("End time: {}", ride.getEndTime() != null ? ride.getEndTime() : "NULL");
                    LOGGER.info("Status: {}", ride.getStatus() != null ? ride.getStatus() : "NULL");
                }
            }
        } catch (Exception e) {
            LOGGER.error("Exception while fetching rides:", e);
        }
    }

    private static String safe(String s) {
        return s != null ? s : "NULL";
    }
}
