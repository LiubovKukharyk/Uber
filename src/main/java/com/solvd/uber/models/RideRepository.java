package com.solvd.uber.models;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.solvd.uber.enums.RideStatus;

public class RideRepository {

    private final Connection connection;

    public RideRepository(Connection connection) {
        this.connection = connection;
    }

    public List<Ride> getAllRides() throws SQLException {
        List<Ride> rides = new ArrayList<>();

        String sql = """
            SELECT r.id as ride_id, r.start_time, r.end_time, r.status,
                   p.id as passenger_id, p.first_name as passenger_first_name, p.last_name as passenger_last_name,
                   d.id as driver_id, d.first_name as driver_first_name, d.last_name as driver_last_name,
                   s.id as service_id, s.option_name as service_name,
                   pr.id as promo_id, pr.code as promo_code, pr.is_voucher, pr.discount_percent, pr.discount_amount
            FROM Ride r
            LEFT JOIN Person p ON r.passenger_id = p.id
            LEFT JOIN Person d ON r.driver_id = d.id
            LEFT JOIN Service s ON r.service_id = s.id
            LEFT JOIN Promo pr ON r.promo_id = pr.id
            """;

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                Person passenger = new Person();
                passenger.setId(rs.getLong("passenger_id"));
                passenger.setFirstName(rs.getString("passenger_first_name"));
                passenger.setLastName(rs.getString("passenger_last_name"));

                Person driver = new Person();
                driver.setId(rs.getLong("driver_id"));
                driver.setFirstName(rs.getString("driver_first_name"));
                driver.setLastName(rs.getString("driver_last_name"));

                Service service = new Service();
                service.setId(rs.getLong("service_id"));
                service.setOptionName(rs.getString("service_name"));

                Promo promo = new Promo();
                promo.setId(rs.getLong("promo_id"));
                promo.setCode(rs.getString("promo_code"));
                promo.setVoucher(rs.getBoolean("is_voucher"));
                promo.setDiscountPercent(rs.getDouble("discount_percent"));
                promo.setDiscountAmount(rs.getDouble("discount_amount"));

                Ride ride = new Ride();
                ride.setId(rs.getLong("ride_id"));
                ride.setPassenger(passenger);
                ride.setDriver(driver);
                ride.setService(service);
                ride.setPromo(promo);
                ride.setStartTime(rs.getTimestamp("start_time") != null ? rs.getTimestamp("start_time").toLocalDateTime() : null);
                ride.setEndTime(rs.getTimestamp("end_time") != null ? rs.getTimestamp("end_time").toLocalDateTime() : null);
                ride.setStatus(rs.getString("status") != null ? RideStatus.valueOf(rs.getString("status")) : null);

                rides.add(ride);
            }
        }

        return rides;
    }
}
