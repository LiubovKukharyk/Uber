package com.solvd.uber.dao.mysqlimpl;

import com.solvd.uber.dao.IRideDAO;
import com.solvd.uber.enums.RideStatus;
import com.solvd.uber.models.Person;
import com.solvd.uber.models.Promo;
import com.solvd.uber.models.Ride;
import com.solvd.uber.models.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RideDAO extends MySQL implements IRideDAO<Ride> {
    private static final Logger LOGGER = LogManager.getLogger(RideDAO.class);

    private static final String INSERT_SQL =
            "INSERT INTO Ride (PassengerId, DriverId, ServiceId, start_time, end_time, status) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SELECT_BY_ID = "SELECT * FROM Ride WHERE Id = ?";
    private static final String SELECT_ALL = "SELECT * FROM Ride";
    private static final String UPDATE_SQL =
            "UPDATE Ride SET PassengerId=?, DriverId=?, ServiceId=?, start_time=?, end_time=?, status=? WHERE Id=?";
    private static final String DELETE_SQL = "DELETE FROM Ride WHERE Id=?";
    private static final String SELECT_BY_PASSENGER = "SELECT * FROM Ride WHERE PassengerId = ?";
    private static final String SELECT_BY_DRIVER = "SELECT * FROM Ride WHERE DriverId = ?";
    private static final String ACCEPT_RIDE_SQL = "UPDATE Ride SET DriverId=?, status=? WHERE Id=?";
    private static final String COMPLETE_RIDE_SQL = "UPDATE Ride SET status=?, end_time=? WHERE Id=?";

    @Override
    public Ride getById(long id) {
        try (Connection c = getConnection();
             PreparedStatement stmt = c.prepareStatement(SELECT_BY_ID)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            LOGGER.error("getById Ride", e);
        }
        return null;
    }

    @Override
    public List<Ride> getAll() {
        List<Ride> list = new ArrayList<>();
        try (Connection c = getConnection();
             PreparedStatement stmt = c.prepareStatement(SELECT_ALL);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            LOGGER.error("getAll Ride", e);
        }
        return list;
    }

    @Override
    public void insert(Ride entity) {
        try (Connection c = getConnection();
             PreparedStatement stmt = c.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, entity.getPassenger() != null ? entity.getPassenger().getId() : 0);
            stmt.setLong(2, entity.getDriver() != null ? entity.getDriver().getId() : 0);
            stmt.setLong(3, entity.getService() != null ? entity.getService().getId() : 0);
            stmt.setTimestamp(4, entity.getStartTime() != null ? Timestamp.valueOf(entity.getStartTime()) : null);
            stmt.setTimestamp(5, entity.getEndTime() != null ? Timestamp.valueOf(entity.getEndTime()) : null);
            stmt.setString(6, entity.getStatus() != null ? entity.getStatus().name() : null);
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) entity.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            LOGGER.error("insert Ride", e);
        }
    }

    @Override
    public void update(Ride entity) {
        try (Connection c = getConnection();
             PreparedStatement stmt = c.prepareStatement(UPDATE_SQL)) {
            stmt.setLong(1, entity.getPassenger() != null ? entity.getPassenger().getId() : 0);
            stmt.setLong(2, entity.getDriver() != null ? entity.getDriver().getId() : 0);
            stmt.setLong(3, entity.getService() != null ? entity.getService().getId() : 0);
            stmt.setTimestamp(4, entity.getStartTime() != null ? Timestamp.valueOf(entity.getStartTime()) : null);
            stmt.setTimestamp(5, entity.getEndTime() != null ? Timestamp.valueOf(entity.getEndTime()) : null);
            stmt.setString(6, entity.getStatus() != null ? entity.getStatus().name() : null);
            stmt.setLong(7, entity.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("update Ride id=" + entity.getId(), e);
        }
    }

    @Override
    public void delete(long id) {
        try (Connection c = getConnection();
             PreparedStatement stmt = c.prepareStatement(DELETE_SQL)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("delete Ride id=" + id, e);
        }
    }

    @Override
    public List<Ride> getByPassengerId(long passengerId) {
        List<Ride> res = new ArrayList<>();
        try (Connection c = getConnection();
             PreparedStatement stmt = c.prepareStatement(SELECT_BY_PASSENGER)) {
            stmt.setLong(1, passengerId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) res.add(mapRow(rs));
            }
        } catch (SQLException e) {
            LOGGER.error("getByPassengerId", e);
        }
        return res;
    }

    @Override
    public List<Ride> getByDriverId(long driverPersonId) {
        List<Ride> res = new ArrayList<>();
        try (Connection c = getConnection();
             PreparedStatement stmt = c.prepareStatement(SELECT_BY_DRIVER)) {
            stmt.setLong(1, driverPersonId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) res.add(mapRow(rs));
            }
        } catch (SQLException e) {
            LOGGER.error("getByDriverId", e);
        }
        return res;
    }

    @Override
    public void acceptRide(long rideId, long driverPersonId) {
        try (Connection c = getConnection();
             PreparedStatement stmt = c.prepareStatement(ACCEPT_RIDE_SQL)) {
            stmt.setLong(1, driverPersonId);
            stmt.setString(2, RideStatus.ACCEPTED.name());
            stmt.setLong(3, rideId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("acceptRide id=" + rideId, e);
        }
    }

    @Override
    public void completeRide(long rideId) {
        try (Connection c = getConnection();
             PreparedStatement stmt = c.prepareStatement(COMPLETE_RIDE_SQL)) {
            stmt.setString(1, RideStatus.COMPLETED.name());
            stmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setLong(3, rideId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("completeRide id=" + rideId, e);
        }
    }

    private Ride mapRow(ResultSet rs) throws SQLException {
        Ride r = new Ride();
        r.setId(rs.getLong("Id"));

        Person passenger = new Person();
        passenger.setId(rs.getLong("PassengerId"));
        r.setPassenger(passenger);

        long driverId = rs.getLong("DriverId");
        if (driverId > 0) {
            Person driver = new Person();
            driver.setId(driverId);
            r.setDriver(driver);
        }

        Service s = new Service();
        s.setId(rs.getLong("ServiceId"));
        r.setService(s);

        Timestamp st = rs.getTimestamp("start_time");
        if (st != null) r.setStartTime(st.toLocalDateTime());

        Timestamp et = rs.getTimestamp("end_time");
        if (et != null) r.setEndTime(et.toLocalDateTime());

        String statusStr = rs.getString("status");
        if (statusStr != null) {
            r.setStatus(RideStatus.valueOf(statusStr));
        }

        try {
            long promoId = rs.getLong("promo_id");
            if (!rs.wasNull() && promoId > 0) {
                Promo p = new Promo();
                p.setId(promoId);
                r.setPromo(p);
            }
        } catch (SQLException ignored) {}

        return r;
    }
}
