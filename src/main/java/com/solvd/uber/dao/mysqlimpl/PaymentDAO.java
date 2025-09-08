package com.solvd.uber.dao.mysqlimpl;

import com.solvd.uber.dao.IPaymentDAO;
import com.solvd.uber.models.Payment;
import com.solvd.uber.models.Wallet;
import com.solvd.uber.models.Ride;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO extends MySQL implements IPaymentDAO<Payment> {
    private static final Logger LOGGER = LogManager.getLogger(PaymentDAO.class);

    private static final String INSERT_SQL = "INSERT INTO Payment (RideId, WalletId, method, amount, status, created_at) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SELECT_BY_ID = "SELECT * FROM Payment WHERE Id = ?";
    private static final String SELECT_ALL = "SELECT * FROM Payment";
    private static final String UPDATE_SQL = "UPDATE Payment SET RideId=?, WalletId=?, method=?, amount=?, status=?, created_at=? WHERE Id=?";
    private static final String DELETE_SQL = "DELETE FROM Payment WHERE Id=?";
    private static final String SELECT_BY_WALLET = "SELECT * FROM Payment WHERE WalletId=?";
    private static final String SELECT_BY_RIDE = "SELECT * FROM Payment WHERE RideId=?";

    @Override
    public Payment getById(long id) {
        try (Connection c = getConnection(); PreparedStatement stmt = c.prepareStatement(SELECT_BY_ID)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) { if (rs.next()) return mapRow(rs); }
        } catch (SQLException e) { LOGGER.error("getById Payment", e); }
        return null;
    }

    @Override
    public List<Payment> getAll() {
        List<Payment> res = new ArrayList<>();
        try (Connection c = getConnection();
             PreparedStatement stmt = c.prepareStatement(SELECT_ALL);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) res.add(mapRow(rs));
        } catch (SQLException e) { LOGGER.error("getAll Payment", e); }
        return res;
    }

    @Override
    public void insert(Payment entity) {
        try (Connection c = getConnection();
             PreparedStatement stmt = c.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, entity.getRide().getId());
            stmt.setLong(2, entity.getWallet().getId());
            stmt.setString(3, entity.getMethod());
            stmt.setDouble(4, entity.getAmount());
            stmt.setString(5, entity.getStatus());
            stmt.setTimestamp(6, Timestamp.valueOf(entity.getCreatedAt() != null ? entity.getCreatedAt() : LocalDateTime.now()));
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    entity.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            LOGGER.error("insert Payment", e);
        }
    }


    @Override
    public void update(Payment entity) {
        try (Connection c = getConnection();
             PreparedStatement stmt = c.prepareStatement(UPDATE_SQL)) {
            stmt.setLong(1, entity.getRide().getId());
            stmt.setLong(2, entity.getWallet().getId());
            stmt.setString(3, entity.getMethod());
            stmt.setDouble(4, entity.getAmount());
            stmt.setString(5, entity.getStatus());
            stmt.setTimestamp(6, Timestamp.valueOf(entity.getCreatedAt()));
            stmt.setLong(7, entity.getId());
            stmt.executeUpdate();
        } catch (SQLException e) { LOGGER.error("update Payment", e); }
    }

    @Override
    public void delete(long id) {
        try (Connection c = getConnection(); PreparedStatement stmt = c.prepareStatement(DELETE_SQL)) {
            stmt.setLong(1, id); stmt.executeUpdate();
        } catch (SQLException e) { LOGGER.error("delete Payment", e); }
    }

    @Override
    public List<Payment> getByWalletId(long walletId) {
        List<Payment> res = new ArrayList<>();
        try (Connection c = getConnection(); PreparedStatement stmt = c.prepareStatement(SELECT_BY_WALLET)) {
            stmt.setLong(1, walletId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) res.add(mapRow(rs));
            }
        } catch (SQLException e) { LOGGER.error("getByWalletId Payment", e); }
        return res;
    }

    @Override
    public List<Payment> getByRideId(long rideId) {
        List<Payment> res = new ArrayList<>();
        try (Connection c = getConnection(); PreparedStatement stmt = c.prepareStatement(SELECT_BY_RIDE)) {
            stmt.setLong(1, rideId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) res.add(mapRow(rs));
            }
        } catch (SQLException e) { LOGGER.error("getByRideId Payment", e); }
        return res;
    }

    private Payment mapRow(ResultSet rs) throws SQLException {
        Payment p = new Payment();
        p.setId(rs.getLong("Id"));
        Wallet w = new Wallet();
        w.setId(rs.getLong("WalletId"));
        p.setWallet(w);
        Ride r = new Ride();
        r.setId(rs.getLong("RideId"));
        p.setRide(r);
        p.setMethod(rs.getString("method"));
        p.setAmount(rs.getDouble("amount"));
        p.setStatus(rs.getString("status"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) p.setCreatedAt(ts.toLocalDateTime());
        return p;
    }
}
