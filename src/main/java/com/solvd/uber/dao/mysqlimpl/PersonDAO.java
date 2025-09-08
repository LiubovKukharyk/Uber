package com.solvd.uber.dao.mysqlimpl;

import com.solvd.uber.dao.IPersonDAO;
import com.solvd.uber.models.Person;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PersonDAO extends MySQL implements IPersonDAO<Person> {
    private static final Logger LOGGER = LogManager.getLogger(PersonDAO.class);

    private static final String INSERT_SQL = "INSERT INTO Person (first_name, last_name, birth_date, created_at) VALUES (?, ?, ?, ?)";
    private static final String SELECT_BY_ID = "SELECT * FROM Person WHERE Id = ?";
    private static final String SELECT_ALL = "SELECT * FROM Person";
    private static final String UPDATE_SQL = "UPDATE Person SET first_name=?, last_name=?, birth_date=?, created_at=? WHERE Id=?";
    private static final String DELETE_SQL = "DELETE FROM Person WHERE Id=?";
    private static final String SELECT_BY_ROLE = "SELECT p.* FROM Person p JOIN Role r ON p.Id = r.PersonId WHERE r.role_name = ?";

    @Override
    public Person getById(long id) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            LOGGER.error("Error getById Person id=" + id, e);
        }
        return null;
    }

    @Override
    public List<Person> getAll() {
        List<Person> list = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            LOGGER.error("Error getAll Person", e);
        }
        return list;
    }

    @Override
    public void insert(Person entity) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, entity.getFirstName());
            stmt.setString(2, entity.getLastName());
            if (entity.getBirthDate() != null) stmt.setDate(3, Date.valueOf(LocalDate.parse((CharSequence) entity.getBirthDate())));
            else stmt.setNull(3, Types.DATE);
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) entity.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            LOGGER.error("Error insert Person", e);
        }
    }

    @Override
    public void update(Person entity) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {
            stmt.setString(1, entity.getFirstName());
            stmt.setString(2, entity.getLastName());
            if (entity.getBirthDate() != null) stmt.setDate(3, Date.valueOf(LocalDate.parse((CharSequence) entity.getBirthDate())));
            else stmt.setNull(3, Types.DATE);
          
            stmt.setLong(5, entity.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Error update Person id=" + entity.getId(), e);
        }
    }

    @Override
    public void delete(long id) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Error delete Person id=" + id, e);
        }
    }

    @Override
    public List<Person> getByRole(String roleName) {
        List<Person> list = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ROLE)) {
            stmt.setString(1, roleName);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            LOGGER.error("Error getByRole " + roleName, e);
        }
        return list;
    }

    private Person mapRow(ResultSet resultSet) throws SQLException {
        Person p = new Person();
        p.setId(resultSet.getLong("Id"));
        p.setFirstName(resultSet.getString("first_name"));
        p.setLastName(resultSet.getString("last_name"));
        Date birthDate = resultSet.getDate("birth_date");
        if (birthDate != null) p.setBirthDate(birthDate);
        return p;
    }
}
