package com.solvd.uber.dao.mysqlimpl;

import com.solvd.uber.dao.IAccountDAO;
import com.solvd.uber.models.Account;
import com.solvd.uber.models.Person;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO extends MySQL implements IAccountDAO<Account> {
    private static final Logger LOGGER = LogManager.getLogger(AccountDAO.class);

    private static final String INSERT_SQL = "INSERT INTO Account (PersonId, account_type, is_business, company_name, created_at) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_BY_ID = "SELECT * FROM Account WHERE Id = ?";
    private static final String SELECT_ALL = "SELECT * FROM Account";
    private static final String UPDATE_SQL = "UPDATE Account SET PersonId=?, account_type=?, is_business=?, company_name=?, created_at=? WHERE Id=?";
    private static final String DELETE_SQL = "DELETE FROM Account WHERE Id=?";
    private static final String SELECT_BY_PERSON = "SELECT * FROM Account WHERE PersonId = ?";

    @Override
    public Account getById(long id) {
        try (Connection c = getConnection();
             PreparedStatement stmt = c.prepareStatement(SELECT_BY_ID)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            LOGGER.error("getById Account", e);
        }
        return null;
    }

    @Override
    public List<Account> getAll() {
        List<Account> res = new ArrayList<>();
        try (Connection c = getConnection();
             PreparedStatement stmt = c.prepareStatement(SELECT_ALL);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) res.add(mapRow(rs));
        } catch (SQLException e) {
            LOGGER.error("getAll Account", e);
        }
        return res;
    }

    @Override
    public void insert(Account entity) {
        try (Connection c = getConnection();
             PreparedStatement stmt = c.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, entity.getPerson().getId());
            stmt.setString(2, entity.getType());
            stmt.setBoolean(3, entity.isBusiness());
            stmt.setString(4, entity.getCompanyName());
            stmt.setTimestamp(5, Timestamp.valueOf(entity.getCreatedAt() != null ? entity.getCreatedAt() : LocalDateTime.now()));
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) entity.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            LOGGER.error("insert Account", e);
        }
    }

    @Override
    public void update(Account entity) {
        try (Connection c = getConnection();
             PreparedStatement stmt = c.prepareStatement(UPDATE_SQL)) {
            stmt.setLong(1, entity.getPerson().getId());
            stmt.setString(2, entity.getType());
            stmt.setBoolean(3, entity.isBusiness());
            stmt.setString(4, entity.getCompanyName());
            stmt.setTimestamp(5, Timestamp.valueOf(entity.getCreatedAt() != null ? entity.getCreatedAt() : LocalDateTime.now()));
            stmt.setLong(6, entity.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("update Account id=" + entity.getId(), e);
        }
    }

    @Override
    public void delete(long id) {
        try (Connection c = getConnection();
             PreparedStatement stmt = c.prepareStatement(DELETE_SQL)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("delete Account id=" + id, e);
        }
    }

    @Override
    public Account getByPersonId(long personId) {
        try (Connection c = getConnection();
             PreparedStatement stmt = c.prepareStatement(SELECT_BY_PERSON)) {
            stmt.setLong(1, personId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            LOGGER.error("getByPersonId Account personId=" + personId, e);
        }
        return null;
    }

    private Account mapRow(ResultSet rs) throws SQLException {
        Account a = new Account();
        a.setId(rs.getLong("Id"));
        Person p = new Person();
        p.setId(rs.getLong("PersonId"));
        a.setPerson(p);
        a.setType(rs.getString("account_type"));
       // a.setBusiness(rs.getBoolean("is_business"));
        a.setCompanyName(rs.getString("company_name"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) a.setCreatedAt(ts.toLocalDateTime());
        return a;
    }
}
