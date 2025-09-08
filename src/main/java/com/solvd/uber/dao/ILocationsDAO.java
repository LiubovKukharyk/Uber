package com.solvd.uber.dao;

import com.solvd.uber.models.Location;
import java.util.List;

public interface ILocationsDAO<T extends Location> extends IBaseDAO<T> {
    List<T> getByRideId(long rideId);
    List<T> getByPersonId(long personId);
}
