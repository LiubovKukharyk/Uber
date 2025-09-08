package com.solvd.uber.dao.mysqlimpl;

import com.solvd.uber.dao.INotificationDAO;
import com.solvd.uber.models.Notification;
import com.solvd.uber.models.Person;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO extends MySQL implements INotificationDAO<Notification> {
    private static final Logger LOGGER = LogManager.getLogger(NotificationDAO.class);

    private static final String INSERT_SQL = "INSERT INTO Notification (PersonId, message, is_read, created_at) VALUES (?, ?, ?, ?)";
    private static final String SELECT_BY_ID = "SELECT * FROM Notification WHERE Id = ?";
    private static final String SELECT_ALL = "SELECT * FROM Notification";
    private static final String UPDATE_SQL = "UPDATE Notification SET PersonId=?, message=?, is_read=?, created_at=? WHERE Id=?";
    private static final String DELETE_SQL = "DELETE FROM Notification WHERE Id=?";
    private static final String SELECT_BY_PERSON = "SELECT * FROM Notification WHERE PersonId = ? ORDER BY created_at DESC";

    @Override
    public Notification getById(long id) {
        try (Connection c = getConnection(); PreparedStatement stmt = c.prepareStatement(SELECT_BY_ID)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) { if (rs.next()) return mapRow(rs); }
        } catch (SQLException e) { LOGGER.error("getById Notification", e); }
        return null;
    }

    @Override
    public List<Notification> getAll() {
        List<Notification> res = new ArrayList<>();
        try (Connection c = getConnection(); PreparedStatement stmt = c.prepareStatement(SELECT_ALL); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) res.add(mapRow(rs));
        } catch (SQLException e) { LOGGER.error("getAll Notification", e); }
        return res;
    }

    @Override
    public void insert(Notification entity) {
        try (Connection c = getConnection();
             PreparedStatement stmt = c.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, entity.getPersonId());
            stmt.setString(2, entity.getMessage());
            stmt.setBoolean(3, entity.isRead());
            stmt.setTimestamp(4, Timestamp.valueOf(entity.getCreatedAt()));
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    entity.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            LOGGER.error("insert Notification", e);
        }
    }


    @Override
    public void update(Notification entity) {
        try (Connection c = getConnection(); PreparedStatement stmt = c.prepareStatement(UPDATE_SQL)) {
            stmt.setLong(1, entity.getPersonId());
            stmt.setString(2, entity.getMessage());
            stmt.setBoolean(3, entity.isRead());
            stmt.setTimestamp(4, Timestamp.valueOf(entity.getCreatedAt()));
            stmt.setLong(5, entity.getId());
            stmt.executeUpdate();
        } catch (SQLException e) { LOGGER.error("update Notification", e); }
    }

    @Override
    public void delete(long id) {
        try (Connection c = getConnection(); PreparedStatement stmt = c.prepareStatement(DELETE_SQL)) {
            stmt.setLong(1, id); stmt.executeUpdate();
        } catch (SQLException e) { LOGGER.error("delete Notification id=" + id, e); }
    }

    @Override
    public List<Notification> getByPersonId(long personId) {
        List<Notification> res = new ArrayList<>();
        try (Connection c = getConnection(); PreparedStatement stmt = c.prepareStatement(SELECT_BY_PERSON)) {
            stmt.setLong(1, personId);
            try (ResultSet rs = stmt.executeQuery()) { while (rs.next()) res.add(mapRow(rs)); }
        } catch (SQLException e) { LOGGER.error("getByPersonId Notification", e); }
        return res;
    }

    private Notification mapRow(ResultSet rs) throws SQLException {
        Notification n = new Notification();
        n.setId(rs.getLong("Id"));
        n.setPersonId(rs.getLong("PersonId"));
        n.setMessage(rs.getString("message"));
        n.setRead(rs.getBoolean("is_read"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) n.setCreatedAt(ts.toLocalDateTime());
        return n;
    }
}
