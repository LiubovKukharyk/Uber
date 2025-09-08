package com.solvd.uber.dao;

import com.solvd.uber.models.Rating;
import java.util.List;

public interface IRatingDAO<T extends Rating> extends IBaseDAO<T> {
    List<T> getByDriverId(long driverPersonId);
    List<T> getByPassengerId(long passengerPersonId);
}
