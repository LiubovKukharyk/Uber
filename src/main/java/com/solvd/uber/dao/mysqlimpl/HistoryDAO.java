package com.solvd.uber.dao.mysqlimpl;

import com.solvd.uber.dao.IHistoryDAO;
import com.solvd.uber.enums.RideStatus;
import com.solvd.uber.models.Account;
import com.solvd.uber.models.History;
import com.solvd.uber.models.Ride;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HistoryDAO extends MySQL implements IHistoryDAO<History> {
    private static final Logger LOGGER = LogManager.getLogger(HistoryDAO.class);

    private static final String INSERT_SQL =
            "INSERT INTO History (RideId, AccountId, status, changed_at) VALUES (?, ?, ?, ?)";
    private static final String SELECT_BY_ID =
            "SELECT * FROM History WHERE Id = ?";
    private static final String SELECT_ALL =
            "SELECT * FROM History";
    private static final String UPDATE_SQL =
            "UPDATE History SET RideId=?, AccountId=?, status=?, changed_at=? WHERE Id=?";
    private static final String DELETE_SQL =
            "DELETE FROM History WHERE Id=?";
    private static final String SELECT_BY_ACCOUNT =
            "SELECT * FROM History WHERE AccountId=? ORDER BY changed_at DESC";

    @Override
    public History getById(long id) {
        try (Connection c = getConnection();
             PreparedStatement stmt = c.prepareStatement(SELECT_BY_ID)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            LOGGER.error("getById History", e);
        }
        return null;
    }

    @Override
    public List<History> getAll() {
        List<History> res = new ArrayList<>();
        try (Connection c = getConnection();
             PreparedStatement stmt = c.prepareStatement(SELECT_ALL);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) res.add(mapRow(rs));
        } catch (SQLException e) {
            LOGGER.error("getAll History", e);
        }
        return res;
    }

    @Override
    public void insert(History entity) {
        try (Connection c = getConnection();
             PreparedStatement stmt = c.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, entity.getRide().getId());
            stmt.setLong(2, entity.getAccount().getId());
            stmt.setString(3, entity.getStatus().name());
            stmt.setTimestamp(4, Timestamp.valueOf(entity.getChangedAt()));
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) entity.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            LOGGER.error("insert History", e);
        }
    }

    @Override
    public void update(History entity) {
        try (Connection c = getConnection();
             PreparedStatement stmt = c.prepareStatement(UPDATE_SQL)) {
            stmt.setLong(1, entity.getRide().getId());
            stmt.setLong(2, entity.getAccount().getId());
            stmt.setString(3, entity.getStatus().name());
            stmt.setTimestamp(4, Timestamp.valueOf(entity.getChangedAt()));
            stmt.setLong(5, entity.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("update History", e);
        }
    }

    @Override
    public void delete(long id) {
        try (Connection c = getConnection();
             PreparedStatement stmt = c.prepareStatement(DELETE_SQL)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("delete History id=" + id, e);
        }
    }

    @Override
    public List<History> getByAccountId(long accountId) {
        List<History> res = new ArrayList<>();
        try (Connection c = getConnection();
             PreparedStatement stmt = c.prepareStatement(SELECT_BY_ACCOUNT)) {
            stmt.setLong(1, accountId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) res.add(mapRow(rs));
            }
        } catch (SQLException e) {
            LOGGER.error("getByAccountId History", e);
        }
        return res;
    }

    private History mapRow(ResultSet rs) throws SQLException {
        History h = new History();
        h.setId(rs.getLong("Id"));

        Ride ride = new Ride();
        ride.setId(rs.getLong("RideId"));
        h.setRide(ride);

        Account account = new Account();
        account.setId(rs.getLong("AccountId"));
        h.setAccount(account);

        String statusStr = rs.getString("status");
        if (statusStr != null) h.setStatus(RideStatus.valueOf(statusStr));

        Timestamp ts = rs.getTimestamp("changed_at");
        if (ts != null) h.setChangedAt(ts.toLocalDateTime());

        return h;
    }

}
