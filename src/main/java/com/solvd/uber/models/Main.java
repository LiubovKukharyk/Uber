package com.solvd.uber.models;

import com.solvd.uber.dao.mysqlimpl.RideDAO;
import com.solvd.uber.parsing.RideExporter;
import com.solvd.uber.parsing.RideJsonExporter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import jakarta.xml.bind.JAXBException;
import java.util.List;

public class Main {
    private static final Logger LOGGER = LogManager.getLogger("RidesLogger");

    public static void main(String[] args) throws JAXBException {
        RideDAO rideDAO = new RideDAO();
        List<Ride> rides = rideDAO.getAll();

        LOGGER.info("Rides count: {}", rides.size());
        rides.forEach(r -> {
            LOGGER.info("Ride id: {}, Passenger: {} {}, Driver: {} {}, Service: {}, Promo code: {}, Discount %: {}, Discount amount: {}",
                    r.getId(),
                    r.getPassenger().getFirstName(), r.getPassenger().getLastName(),
                    r.getDriver().getFirstName(), r.getDriver().getLastName(),
                    r.getService().getOptionName(),
                    r.getPromo() != null ? r.getPromo().getCode() : "NULL",
                    r.getPromo() != null && r.getPromo().getDiscountPercent() != null ? r.getPromo().getDiscountPercent() : "NULL",
                    r.getPromo() != null && r.getPromo().getDiscountAmount() != null ? r.getPromo().getDiscountAmount() : "NULL"
            );
        });

        RideExporter.exportRides(rides);
        RideJsonExporter.toJson(rides, "rides.json");
    }
}
