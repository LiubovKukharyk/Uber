package com.solvd.uber.dao.mysqlimpl;

import com.solvd.uber.dao.ILocationsDAO;
import com.solvd.uber.models.Location;
import com.solvd.uber.models.Ride;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LocationDAO extends MySQL implements ILocationsDAO<Location> {
    private static final Logger LOGGER = LogManager.getLogger(LocationDAO.class);

    private static final String INSERT_SQL = "INSERT INTO Location (RideId, location_type, latitude, longitude) VALUES (?, ?, ?, ?)";
    private static final String SELECT_BY_ID = "SELECT * FROM Location WHERE Id = ?";
    private static final String SELECT_ALL = "SELECT * FROM Location";
    private static final String UPDATE_SQL = "UPDATE Location SET RideId=?, location_type=?, latitude=?, longitude=? WHERE Id=?";
    private static final String DELETE_SQL = "DELETE FROM Location WHERE Id=?";
    private static final String SELECT_BY_RIDE = "SELECT * FROM Location WHERE RideId = ?";
    private static final String SELECT_BY_PERSON = "SELECT l.* FROM Location l JOIN Ride r ON l.RideId = r.Id WHERE r.PassengerId = ? OR r.DriverId = ? ORDER BY l.Id";

    @Override
    public Location getById(long id) {
        try (Connection c = getConnection(); PreparedStatement stmt = c.prepareStatement(SELECT_BY_ID)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) { if (rs.next()) return mapRow(rs); }
        } catch (SQLException e) { LOGGER.error("getById Location", e); }
        return null;
    }

    @Override
    public List<Location> getAll() {
        List<Location> res = new ArrayList<>();
        try (Connection c = getConnection(); PreparedStatement stmt = c.prepareStatement(SELECT_ALL); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) res.add(mapRow(rs));
        } catch (SQLException e) { LOGGER.error("getAll Location", e); }
        return res;
    }

    @Override
    public void insert(Location entity) {
        try (Connection c = getConnection();
             PreparedStatement stmt = c.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, entity.getRide().getId());
            stmt.setString(2, entity.getLocationType().name());
            stmt.setDouble(3, entity.getLatitude());
            stmt.setDouble(4, entity.getLongitude());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    entity.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            LOGGER.error("insert Location", e);
        }
    }


    @Override
    public void update(Location entity) {
        try (Connection c = getConnection(); PreparedStatement stmt = c.prepareStatement(UPDATE_SQL)) {
            stmt.setLong(1, entity.getRide().getId());
            stmt.setString(2, entity.getLocationType().name());
            stmt.setDouble(3, entity.getLatitude());
            stmt.setDouble(4, entity.getLongitude());
            stmt.setLong(5, entity.getId());
            stmt.executeUpdate();
        } catch (SQLException e) { LOGGER.error("update Location", e); }
    }

    @Override
    public void delete(long id) {
        try (Connection c = getConnection(); PreparedStatement stmt = c.prepareStatement(DELETE_SQL)) {
            stmt.setLong(1, id); stmt.executeUpdate();
        } catch (SQLException e) { LOGGER.error("delete Location id=" + id, e); }
    }

    @Override
    public List<Location> getByRideId(long rideId) {
        List<Location> res = new ArrayList<>();
        try (Connection c = getConnection(); PreparedStatement stmt = c.prepareStatement(SELECT_BY_RIDE)) {
            stmt.setLong(1, rideId);
            try (ResultSet rs = stmt.executeQuery()) { while (rs.next()) res.add(mapRow(rs)); }
        } catch (SQLException e) { LOGGER.error("getByRideId", e); }
        return res;
    }

    @Override
    public List<Location> getByPersonId(long personId) {
        List<Location> res = new ArrayList<>();
        try (Connection c = getConnection(); PreparedStatement stmt = c.prepareStatement(SELECT_BY_PERSON)) {
            stmt.setLong(1, personId);
            stmt.setLong(2, personId);
            try (ResultSet rs = stmt.executeQuery()) { while (rs.next()) res.add(mapRow(rs)); }
        } catch (SQLException e) { LOGGER.error("getByPersonId", e); }
        return res;
    }

    private Location mapRow(ResultSet rs) throws SQLException {
        Location l = new Location();
        l.setId(rs.getLong("Id"));
        Ride r = new Ride();
        r.setId(rs.getLong("RideId"));
        l.setRide(r);
        l.setLocationType(com.solvd.uber.enums.LocationType.valueOf(rs.getString("location_type")));
        l.setLatitude(rs.getDouble("latitude"));
        l.setLongitude(rs.getDouble("longitude"));
        return l;
    }
}
