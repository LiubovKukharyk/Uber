package com.solvd.uber.dao.mysqlimpl;

import com.solvd.uber.dao.IRatingDAO;
import com.solvd.uber.models.Rating;
import com.solvd.uber.models.Person;
import com.solvd.uber.models.Ride;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RatingDAO extends MySQL implements IRatingDAO<Rating> {
    private static final Logger LOGGER = LogManager.getLogger(RatingDAO.class);

    private static final String INSERT_SQL = "INSERT INTO Rating (RideId, RaterId, RateeId, score, created_at) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_BY_ID = "SELECT * FROM Rating WHERE Id = ?";
    private static final String SELECT_ALL = "SELECT * FROM Rating";
    private static final String UPDATE_SQL = "UPDATE Rating SET RideId=?, RaterId=?, RateeId=?, score=?, created_at=? WHERE Id=?";
    private static final String DELETE_SQL = "DELETE FROM Rating WHERE Id=?";
    private static final String SELECT_BY_DRIVer = "SELECT * FROM Rating WHERE RateeId = ?"; // driver as ratee
    private static final String SELECT_BY_PASSENGER = "SELECT * FROM Rating WHERE RateeId = ?"; // passenger as ratee

    @Override
    public Rating getById(long id) {
        try (Connection c = getConnection(); PreparedStatement stmt = c.prepareStatement(SELECT_BY_ID)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) { if (rs.next()) return mapRow(rs); }
        } catch (SQLException e) { LOGGER.error("getById Rating", e); }
        return null;
    }

    @Override
    public List<Rating> getAll() {
        List<Rating> res = new ArrayList<>();
        try (Connection c = getConnection(); PreparedStatement stmt = c.prepareStatement(SELECT_ALL); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) res.add(mapRow(rs));
        } catch (SQLException e) { LOGGER.error("getAll Rating", e); }
        return res;
    }

    @Override
    public void insert(Rating entity) {
        try (Connection c = getConnection();
             PreparedStatement stmt = c.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, entity.getRide().getId());
            stmt.setLong(2, entity.getRater().getId());
            stmt.setLong(3, entity.getRatee().getId());
            stmt.setInt(4, entity.getScore());
            stmt.setTimestamp(5, Timestamp.valueOf(entity.getCreatedAt() != null ? entity.getCreatedAt() : LocalDateTime.now()));
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    entity.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            LOGGER.error("insert Rating", e);
        }
    }


    @Override
    public void update(Rating entity) {
        try (Connection c = getConnection(); PreparedStatement stmt = c.prepareStatement(UPDATE_SQL)) {
            stmt.setLong(1, entity.getRide().getId());
            stmt.setLong(2, entity.getRater().getId());
            stmt.setLong(3, entity.getRatee().getId());
            stmt.setInt(4, entity.getScore());
            stmt.setTimestamp(5, Timestamp.valueOf(entity.getCreatedAt()));
            stmt.setLong(6, entity.getId());
            stmt.executeUpdate();
        } catch (SQLException e) { LOGGER.error("update Rating", e); }
    }

    @Override
    public void delete(long id) {
        try (Connection c = getConnection(); PreparedStatement stmt = c.prepareStatement(DELETE_SQL)) {
            stmt.setLong(1, id); stmt.executeUpdate();
        } catch (SQLException e) { LOGGER.error("delete Rating id=" + id, e); }
    }

    @Override
    public List<Rating> getByDriverId(long driverPersonId) {
        List<Rating> res = new ArrayList<>();
        try (Connection c = getConnection(); PreparedStatement stmt = c.prepareStatement(SELECT_BY_DRIVer)) {
            stmt.setLong(1, driverPersonId);
            try (ResultSet rs = stmt.executeQuery()) { while (rs.next()) res.add(mapRow(rs)); }
        } catch (SQLException e) { LOGGER.error("getByDriverId Rating", e); }
        return res;
    }

    @Override
    public List<Rating> getByPassengerId(long passengerPersonId) {
        List<Rating> res = new ArrayList<>();
        try (Connection c = getConnection(); PreparedStatement stmt = c.prepareStatement(SELECT_BY_PASSENGER)) {
            stmt.setLong(1, passengerPersonId);
            try (ResultSet rs = stmt.executeQuery()) { while (rs.next()) res.add(mapRow(rs)); }
        } catch (SQLException e) { LOGGER.error("getByPassengerId Rating", e); }
        return res;
    }

    private Rating mapRow(ResultSet rs) throws SQLException {
        Rating r = new Rating();
        r.setId(rs.getLong("Id"));
        Ride ride = new Ride();
        ride.setId(rs.getLong("RideId"));
        r.setRide(ride);
        Person rater = new Person();
        rater.setId(rs.getLong("RaterId"));
        r.setRater(rater);
        Person ratee = new Person();
        ratee.setId(rs.getLong("RateeId"));
        r.setRatee(ratee);
        r.setScore(rs.getInt("score"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) r.setCreatedAt(ts.toLocalDateTime());
        return r;
    }
}
