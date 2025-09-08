package com.solvd.uber.dao;

import com.solvd.uber.models.Ride;
import java.util.List;

public interface IRideDAO<T extends Ride> extends IBaseDAO<T> {
    List<T> getByPassengerId(long passengerId);
    List<T> getByDriverId(long driverPersonId);
    void acceptRide(long rideId, long driverPersonId);
    void completeRide(long rideId);
}
