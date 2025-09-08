package com.solvd.uber.services;

import com.solvd.uber.models.Ride;
import com.solvd.uber.models.Person;
import com.solvd.uber.enums.RideStatus;
import com.solvd.uber.services.interfaces.IRideService;

import java.time.LocalDateTime;

public class RideService implements IRideService {

    @Override
    public Ride requestRide(Ride ride) {
        if (ride == null) return null;

        ride.setStatus(RideStatus.REQUESTED);
        ride.setStartTime(LocalDateTime.now());

        return ride;
    }

    @Override
    public void acceptRide(long rideId, long driverId) {
        Ride ride = new Ride();
        ride.setId(rideId);

        Person driver = new Person();
        driver.setId(driverId);
        ride.setDriver(driver);

        ride.setStatus(RideStatus.ACCEPTED);
        ride.setStartTime(LocalDateTime.now());
    }

    @Override
    public void completeRide(long rideId) {
        Ride ride = new Ride();
        ride.setId(rideId);
        ride.setStatus(RideStatus.COMPLETED);
        ride.setEndTime(LocalDateTime.now());
    }
}
