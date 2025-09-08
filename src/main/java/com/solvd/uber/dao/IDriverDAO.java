package com.solvd.uber.dao;

import com.solvd.uber.models.DriverProfile;
import com.solvd.uber.models.Vehicle;
import java.util.List;

public interface IDriverDAO<T extends DriverProfile> extends IBaseDAO<T> {
    T getProfileByPersonId(long personId);
    List<Vehicle> getVehiclesByDriverId(long driverPersonId);
    void addVehicleToDriver(long driverPersonId, Vehicle vehicle);
    void removeVehicle(long vehicleId);
    void updateVehicle(Vehicle vehicle);
}
