package com.solvd.uber.dao.mysqlimpl;

import com.solvd.uber.dao.IWalletDAO;
import com.solvd.uber.models.Wallet;
import com.solvd.uber.models.Person;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WalletDAO extends MySQL implements IWalletDAO<Wallet> {
    private static final Logger LOGGER = LogManager.getLogger(WalletDAO.class);

    private static final String INSERT_SQL = "INSERT INTO Wallet (PersonId, balance) VALUES (?, ?)";
    private static final String SELECT_BY_ID = "SELECT * FROM Wallet WHERE Id = ?";
    private static final String SELECT_ALL = "SELECT * FROM Wallet";
    private static final String UPDATE_SQL = "UPDATE Wallet SET PersonId=?, balance=? WHERE Id=?";
    private static final String DELETE_SQL = "DELETE FROM Wallet WHERE Id=?";
    private static final String SELECT_BY_PERSON = "SELECT * FROM Wallet WHERE PersonId = ?";

    @Override
    public Wallet getById(long id) {
        try (Connection c = getConnection(); PreparedStatement stmt = c.prepareStatement(SELECT_BY_ID)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) { if (rs.next()) return mapRow(rs); }
        } catch (SQLException e) { LOGGER.error("getById Wallet", e); }
        return null;
    }

    @Override
    public List<Wallet> getAll() {
        List<Wallet> res = new ArrayList<>();
        try (Connection c = getConnection(); PreparedStatement stmt = c.prepareStatement(SELECT_ALL); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) res.add(mapRow(rs));
        } catch (SQLException e) { LOGGER.error("getAll Wallet", e); }
        return res;
    }

    @Override
    public void insert(Wallet entity) {
        try (Connection c = getConnection();
             PreparedStatement stmt = c.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, entity.getPerson().getId());
            stmt.setDouble(2, entity.getBalance());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    entity.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            LOGGER.error("insert Wallet", e);
        }
    }


    @Override
    public void update(Wallet entity) {
        try (Connection c = getConnection(); PreparedStatement stmt = c.prepareStatement(UPDATE_SQL)) {
            stmt.setLong(1, entity.getPerson().getId());
            stmt.setDouble(2, entity.getBalance());
            stmt.setLong(3, entity.getId());
            stmt.executeUpdate();
        } catch (SQLException e) { LOGGER.error("update Wallet", e); }
    }

    @Override
    public void delete(long id) {
        try (Connection c = getConnection(); PreparedStatement stmt = c.prepareStatement(DELETE_SQL)) {
            stmt.setLong(1, id); stmt.executeUpdate();
        } catch (SQLException e) { LOGGER.error("delete Wallet id=" + id, e); }
    }

    @Override
    public Wallet getByAccountId(long accountId) {
        try (Connection c = getConnection(); PreparedStatement stmt = c.prepareStatement(SELECT_BY_PERSON)) {
            stmt.setLong(1, accountId);
            try (ResultSet rs = stmt.executeQuery()) { if (rs.next()) return mapRow(rs); }
        } catch (SQLException e) { LOGGER.error("getByAccountId Wallet", e); }
        return null;
    }

    private Wallet mapRow(ResultSet rs) throws SQLException {
        Wallet w = new Wallet();
        w.setId(rs.getLong("Id"));
        Person p = new Person();
        p.setId(rs.getLong("PersonId"));
        w.setPerson(p);
        w.setBalance(rs.getDouble("balance"));
        return w;
    }
}
