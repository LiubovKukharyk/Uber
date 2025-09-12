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
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class RideDAO extends MySQL implements IRideDAO<Ride> {
    private static final Logger LOGGER = LogManager.getLogger(RideDAO.class);

    private static final String SELECT_ALL = """
            SELECT r.Id AS ride_id, r.start_time, r.end_time, r.status,
                   p.Id AS passenger_id, p.first_name AS passenger_first_name, p.last_name AS passenger_last_name, p.birth_date AS passenger_birth_date,
                   d.Id AS driver_id, d.first_name AS driver_first_name, d.last_name AS driver_last_name, d.birth_date AS driver_birth_date,
                   s.Id AS service_id, s.option_name AS service_option,
                   pr.Id AS promo_id, pr.code AS promo_code, pr.isVoucher AS promo_isVoucher,
                   pr.discount_percent AS promo_discount_percent, pr.discount_amount AS promo_discount_amount,
                   pr.valid_until AS promo_valid_until, pr.created_at AS promo_created_at
            FROM Ride r
            JOIN Person p ON r.PassengerId = p.Id
            JOIN Person d ON r.DriverId = d.Id
            JOIN Service s ON r.ServiceId = s.Id
            LEFT JOIN Promo pr ON r.promo_id = pr.Id
            """;

    @Override
    public List<Ride> getAll() {
        List<Ride> rides = new ArrayList<>();
        try (Connection c = getConnection();
             PreparedStatement stmt = c.prepareStatement(SELECT_ALL);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                rides.add(mapRow(rs));
            }

        } catch (SQLException e) {
            LOGGER.error("getAll Ride", e);
        }
        return rides;
    }

    private Ride mapRow(ResultSet rs) throws SQLException {
        Ride ride = new Ride();
        ride.setId(rs.getLong("ride_id"));

        Person passenger = new Person();
        passenger.setId(rs.getLong("passenger_id"));
        passenger.setFirstName(rs.getString("passenger_first_name"));
        passenger.setLastName(rs.getString("passenger_last_name"));
        passenger.setBirthDate(rs.getDate("passenger_birth_date"));
        ride.setPassenger(passenger);

        Person driver = new Person();
        driver.setId(rs.getLong("driver_id"));
        driver.setFirstName(rs.getString("driver_first_name"));
        driver.setLastName(rs.getString("driver_last_name"));
        driver.setBirthDate(rs.getDate("driver_birth_date"));
        ride.setDriver(driver);

        Service service = new Service();
        service.setId(rs.getLong("service_id"));
        service.setOptionName(rs.getString("service_option"));
        ride.setService(service);

        Timestamp stTs = rs.getTimestamp("start_time");
        if (stTs != null) ride.setStartTime(stTs.toLocalDateTime());
        Timestamp etTs = rs.getTimestamp("end_time");
        if (etTs != null) ride.setEndTime(etTs.toLocalDateTime());

        String statusStr = rs.getString("status");
        if (statusStr != null) {
            try {
                ride.setStatus(RideStatus.valueOf(statusStr.trim().replace(' ', '_').toUpperCase()));
            } catch (IllegalArgumentException ignored) {
                ride.setStatus(null);
            }
        }

        long promoId = rs.getLong("promo_id");
        if (!rs.wasNull()) {
            Promo promo = new Promo();
            promo.setId(promoId);
            promo.setCode(rs.getString("promo_code"));
            promo.setVoucher(rs.getBoolean("promo_isVoucher"));
            promo.setDiscountPercent(rs.getObject("promo_discount_percent", Double.class));
            promo.setDiscountAmount(rs.getObject("promo_discount_amount", Double.class));

            Timestamp validUntilTs = rs.getTimestamp("promo_valid_until");
            promo.setValidUntil(validUntilTs != null ? new Date(validUntilTs.getTime()) : null);

            Timestamp createdAtTs = rs.getTimestamp("promo_created_at");
            promo.setCreatedAt(createdAtTs != null ? new Date(createdAtTs.getTime()) : null);

            ride.setPromo(promo);
        }

        return ride;
    }

    @Override
    public Ride getById(long id) { return null; }

    @Override
    public void insert(Ride entity) {}

    @Override
    public void update(Ride entity) {}

    @Override
    public void delete(long id) {}

    @Override
    public List<Ride> getByPassengerId(long passengerId) { return null; }

    @Override
    public List<Ride> getByDriverId(long driverPersonId) { return null; }

    @Override
    public void acceptRide(long rideId, long driverPersonId) {}

    @Override
    public void completeRide(long rideId) {}
}
