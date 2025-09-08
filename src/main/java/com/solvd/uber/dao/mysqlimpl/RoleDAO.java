package com.solvd.uber.dao.mysqlimpl;

import com.solvd.uber.dao.IRoleDAO;
import com.solvd.uber.models.Role;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoleDAO extends MySQL implements IRoleDAO<Role> {
    private static final Logger LOGGER = LogManager.getLogger(RoleDAO.class);

    private static final String INSERT_SQL = "INSERT INTO Role (PersonId, role_name, assigned_at) VALUES (?, ?, ?)";
    private static final String SELECT_BY_ID = "SELECT * FROM Role WHERE Id = ?";
    private static final String SELECT_ALL = "SELECT * FROM Role";
    private static final String UPDATE_SQL = "UPDATE Role SET PersonId=?, role_name=?, assigned_at=? WHERE Id=?";
    private static final String DELETE_SQL = "DELETE FROM Role WHERE Id=?";
    private static final String SELECT_BY_PERSON = "SELECT * FROM Role WHERE PersonId = ?";

    @Override
    public Role getById(long id) {
        try (Connection c = getConnection(); PreparedStatement stmt = c.prepareStatement(SELECT_BY_ID)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) { if (rs.next()) return mapRow(rs); }
        } catch (SQLException e) { LOGGER.error("getById Role", e); }
        return null;
    }

    @Override
    public List<Role> getAll() {
        List<Role> res = new ArrayList<>();
        try (Connection c = getConnection(); PreparedStatement stmt = c.prepareStatement(SELECT_ALL); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) res.add(mapRow(rs));
        } catch (SQLException e) { LOGGER.error("getAll Role", e); }
        return res;
    }

    @Override
    public void insert(Role entity) {
        try (Connection c = getConnection();
             PreparedStatement stmt = c.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, entity.getId());
            stmt.setString(2, entity.getName());
            stmt.setTimestamp(3, Timestamp.valueOf(java.time.LocalDateTime.now()));
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    entity.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            LOGGER.error("insert Role", e);
        }
    }


    @Override
    public void update(Role entity) {
        try (Connection c = getConnection(); PreparedStatement stmt = c.prepareStatement(UPDATE_SQL)) {
            stmt.setLong(1, entity.getId()); // adapt if model has personId
            stmt.setString(2, entity.getName());
            stmt.setTimestamp(3, Timestamp.valueOf(java.time.LocalDateTime.now()));
            stmt.setLong(4, entity.getId());
            stmt.executeUpdate();
        } catch (SQLException e) { LOGGER.error("update Role", e); }
    }

    @Override
    public void delete(long id) {
        try (Connection c = getConnection(); PreparedStatement stmt = c.prepareStatement(DELETE_SQL)) {
            stmt.setLong(1, id); stmt.executeUpdate();
        } catch (SQLException e) { LOGGER.error("delete Role id=" + id, e); }
    }

    @Override
    public List<Role> getRolesByPersonId(long personId) {
        List<Role> res = new ArrayList<>();
        try (Connection c = getConnection(); PreparedStatement stmt = c.prepareStatement(SELECT_BY_PERSON)) {
            stmt.setLong(1, personId);
            try (ResultSet rs = stmt.executeQuery()) { while (rs.next()) res.add(mapRow(rs)); }
        } catch (SQLException e) { LOGGER.error("getRolesByPersonId", e); }
        return res;
    }

    private Role mapRow(ResultSet rs) throws SQLException {
        Role r = new Role();
        r.setId(rs.getLong("Id"));
        r.setName(rs.getString("role_name"));
        return r;
    }
}
