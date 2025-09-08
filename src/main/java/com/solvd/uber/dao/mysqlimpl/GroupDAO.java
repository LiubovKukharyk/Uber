package com.solvd.uber.dao.mysqlimpl;

import com.solvd.uber.dao.IGroupDAO;
import com.solvd.uber.models.Group;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GroupDAO extends MySQL implements IGroupDAO<Group> {
    private static final Logger LOGGER = LogManager.getLogger(GroupDAO.class);

    private static final String INSERT_SQL = "INSERT INTO `Group` (AccountId, type, name, created_at) VALUES (?, ?, ?, ?)";
    private static final String SELECT_BY_ID = "SELECT * FROM `Group` WHERE Id = ?";
    private static final String SELECT_ALL = "SELECT * FROM `Group`";
    private static final String UPDATE_SQL = "UPDATE `Group` SET AccountId=?, type=?, name=?, created_at=? WHERE Id=?";
    private static final String DELETE_SQL = "DELETE FROM `Group` WHERE Id=?";
    private static final String SELECT_BY_ACCOUNT = "SELECT * FROM `Group` WHERE AccountId = ?";
    private static final String SELECT_BUSINESS_BY_ACCOUNT = "SELECT * FROM `Group` WHERE AccountId = ? AND type = 'business'";
    private static final String SELECT_PERSONAL_BY_ACCOUNT = "SELECT * FROM `Group` WHERE AccountId = ? AND type = 'personal'";

    @Override
    public Group getById(long id) {
        try (Connection c = getConnection(); PreparedStatement stmt = c.prepareStatement(SELECT_BY_ID)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) { if (rs.next()) return mapRow(rs); }
        } catch (SQLException e) { LOGGER.error("getById Group", e); }
        return null;
    }

    @Override
    public List<Group> getAll() {
        List<Group> res = new ArrayList<>();
        try (Connection c = getConnection(); PreparedStatement stmt = c.prepareStatement(SELECT_ALL); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) res.add(mapRow(rs));
        } catch (SQLException e) { LOGGER.error("getAll Group", e); }
        return res;
    }

    @Override
    public void insert(Group entity) {
        try (Connection c = getConnection();
             PreparedStatement stmt = c.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, entity.getAccount().getId());
            stmt.setString(2, entity.getType());
            stmt.setString(3, entity.getName());
            stmt.setTimestamp(4, Timestamp.valueOf(entity.getCreatedAt()));
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    entity.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            LOGGER.error("insert Group", e);
        }
    }


    @Override
    public void update(Group entity) {
        try (Connection c = getConnection(); PreparedStatement stmt = c.prepareStatement(UPDATE_SQL)) {
            stmt.setLong(1, entity.getAccount().getId());
            stmt.setString(2, entity.getType());
            stmt.setString(3, entity.getName());
            stmt.setTimestamp(4, Timestamp.valueOf(entity.getCreatedAt()));
            stmt.setLong(5, entity.getId());
            stmt.executeUpdate();
        } catch (SQLException e) { LOGGER.error("update Group", e); }
    }

    @Override
    public void delete(long id) {
        try (Connection c = getConnection();
             PreparedStatement stmt = c.prepareStatement(DELETE_SQL)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("delete Group", e);
        }
    }

    @Override
    public List<Group> getByAccountId(long accountId) {
        List<Group> res = new ArrayList<>();
        try (Connection c = getConnection(); PreparedStatement stmt = c.prepareStatement(SELECT_BY_ACCOUNT)) {
            stmt.setLong(1, accountId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) res.add(mapRow(rs));
            }
        } catch (SQLException e) { LOGGER.error("getByAccountId Group", e); }
        return res;
    }

    @Override
    public List<Group> getBusinessGroupsByAccountId(long accountId) {
        return getListByQuery(SELECT_BUSINESS_BY_ACCOUNT, accountId);
    }

    @Override
    public List<Group> getPersonalGroupsByAccountId(long accountId) {
        return getListByQuery(SELECT_PERSONAL_BY_ACCOUNT, accountId);
    }

    private List<Group> getListByQuery(String sql, long accountId) {
        List<Group> res = new ArrayList<>();
        try (Connection c = getConnection(); PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setLong(1, accountId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) res.add(mapRow(rs));
            }
        } catch (SQLException e) { LOGGER.error("getListByQuery Group", e); }
        return res;
    }

    private Group mapRow(ResultSet rs) throws SQLException {
        Group g = new Group();
        g.setId(rs.getLong("Id"));
        // map account id only (full account can be fetched separately)
        com.solvd.uber.models.Account a = new com.solvd.uber.models.Account();
        a.setId(rs.getLong("AccountId"));
        g.setAccount(a);
        g.setType(rs.getString("type"));
        g.setName(rs.getString("name"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) g.setCreatedAt(ts.toLocalDateTime());
        return g;
    }
}
