package com.solvd.uber.services.interfaces;

import com.solvd.uber.models.Person;
import com.solvd.uber.models.Ride;

@SuppressWarnings("unused")
public interface IRideService {
    Ride requestRide(Ride ride);
    void acceptRide(long rideId, long driverPersonId);
    void completeRide(long rideId);
}
