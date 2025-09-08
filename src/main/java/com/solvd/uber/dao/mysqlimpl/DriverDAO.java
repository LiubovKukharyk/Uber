package com.solvd.uber.dao.mysqlimpl;

import com.solvd.uber.dao.IDriverDAO;
import com.solvd.uber.models.DriverProfile;
import com.solvd.uber.models.Vehicle;
import com.solvd.uber.models.Person;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DriverDAO extends MySQL implements IDriverDAO<DriverProfile> {
    private static final Logger LOGGER = LogManager.getLogger(DriverDAO.class);

    private static final String SELECT_PROFILE_BY_PERSON = "SELECT * FROM DriverProfile WHERE PersonId = ?";
    private static final String SELECT_VEHICLES_BY_DRIVER = "SELECT * FROM Vehicle WHERE PersonId = ?";
    private static final String INSERT_VEHICLE = "INSERT INTO Vehicle (PersonId, brand, model_name, year, plate_number) VALUES (?, ?, ?, ?, ?)";
    private static final String DELETE_VEHICLE = "DELETE FROM Vehicle WHERE Id = ?";
    private static final String UPDATE_VEHICLE = "UPDATE Vehicle SET brand=?, model_name=?, year=?, plate_number=? WHERE Id=?";

    @Override
    public DriverProfile getProfileByPersonId(long personId) {
        try (Connection c = getConnection();
             PreparedStatement stmt = c.prepareStatement(SELECT_PROFILE_BY_PERSON)) {
            stmt.setLong(1, personId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    DriverProfile dp = new DriverProfile();
                    dp.setId(rs.getLong("Id"));
                    Person p = new Person();
                    p.setId(rs.getLong("PersonId"));
                    dp.setPerson(p);
                    Timestamp ts = rs.getTimestamp("hired_at");
                    if (ts != null) dp.setHiredAt(ts.toLocalDateTime());
                    return dp;
                }
            }
        } catch (SQLException e) {
            LOGGER.error("getProfileByPersonId", e);
        }
        return null;
    }

    @Override
    public List<Vehicle> getVehiclesByDriverId(long driverPersonId) {
        List<Vehicle> res = new ArrayList<>();
        try (Connection c = getConnection();
             PreparedStatement stmt = c.prepareStatement(SELECT_VEHICLES_BY_DRIVER)) {
            stmt.setLong(1, driverPersonId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Vehicle v = new Vehicle();
                    v.setId(rs.getLong("Id"));
                    Person owner = new Person();
                    owner.setId(rs.getLong("PersonId"));
                    v.setOwner(owner);
                    v.setBrand(rs.getString("brand"));
                    v.setModelName(rs.getString("model_name"));
                    v.setYear(rs.getInt("year"));
                    v.setPlateNumber(rs.getString("plate_number"));
                    res.add(v);
                }
            }
        } catch (SQLException e) {
            LOGGER.error("getVehiclesByDriverId", e);
        }
        return res;
    }

    public void addVehicleToDriver(long driverPersonId, Vehicle vehicle) {
        try (Connection c = getConnection();
             PreparedStatement stmt = c.prepareStatement(INSERT_VEHICLE, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, driverPersonId);
            stmt.setString(2, vehicle.getBrand());
            stmt.setString(3, vehicle.getModelName());
            stmt.setInt(4, vehicle.getYear());
            stmt.setString(5, vehicle.getPlateNumber());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    vehicle.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            LOGGER.error("addVehicleToDriver", e);
        }
    }


    @Override
    public void removeVehicle(long vehicleId) {
        try (Connection c = getConnection();
             PreparedStatement stmt = c.prepareStatement(DELETE_VEHICLE)) {
            stmt.setLong(1, vehicleId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("removeVehicle", e);
        }
    }

    @Override
    public void updateVehicle(Vehicle vehicle) {
        try (Connection c = getConnection();
             PreparedStatement stmt = c.prepareStatement(UPDATE_VEHICLE)) {
            stmt.setString(1, vehicle.getBrand());
            stmt.setString(2, vehicle.getModelName());
            stmt.setInt(3, vehicle.getYear());
            stmt.setString(4, vehicle.getPlateNumber());
            stmt.setLong(5, vehicle.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("updateVehicle", e);
        }
    }

    @Override
    public void insert(DriverProfile entity) { /* optional: implement if you use insert profile */ }
    @Override
    public DriverProfile getById(long id) { return null; }
    @Override
    public List<DriverProfile> getAll() { return new ArrayList<>(); }
    @Override
    public void update(DriverProfile entity) { }
    @Override
    public void delete(long id) { }
}
