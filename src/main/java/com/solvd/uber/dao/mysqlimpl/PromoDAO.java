package com.solvd.uber.dao.mysqlimpl;

import com.solvd.uber.dao.IPromoDAO;
import com.solvd.uber.models.Promo;
import com.solvd.uber.models.Person;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PromoDAO extends MySQL implements IPromoDAO<Promo> {
    private static final Logger LOGGER = LogManager.getLogger(PromoDAO.class);

    private static final String INSERT_SQL = "INSERT INTO Promo (PersonId, code, is_voucher, discount_percent, discount_amount, valid_until, created_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_BY_ID = "SELECT * FROM Promo WHERE Id = ?";
    private static final String SELECT_ALL = "SELECT * FROM Promo";
    private static final String UPDATE_SQL = "UPDATE Promo SET PersonId=?, code=?, is_voucher=?, discount_percent=?, discount_amount=?, valid_until=?, created_at=? WHERE Id=?";
    private static final String DELETE_SQL = "DELETE FROM Promo WHERE Id=?";
    private static final String SELECT_BY_PERSON = "SELECT * FROM Promo WHERE PersonId = ?";

    @Override
    public Promo getById(long id) {
        try (Connection c = getConnection(); PreparedStatement stmt = c.prepareStatement(SELECT_BY_ID)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) { if (rs.next()) return mapRow(rs); }
        } catch (SQLException e) { LOGGER.error("getById Promo", e); }
        return null;
    }

    @Override
    public List<Promo> getAll() {
        List<Promo> res = new ArrayList<>();
        try (Connection c = getConnection(); PreparedStatement stmt = c.prepareStatement(SELECT_ALL); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) res.add(mapRow(rs));
        } catch (SQLException e) { LOGGER.error("getAll Promo", e); }
        return res;
    }

    @Override
    public void insert(Promo entity) {
        try (Connection c = getConnection();
             PreparedStatement stmt = c.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, entity.getPerson().getId());
            stmt.setString(2, entity.getCode());
            stmt.setBoolean(3, entity.isVoucher());
            if (entity.getDiscountPercent() != null) {
                stmt.setDouble(4, entity.getDiscountPercent());
            } else {
                stmt.setNull(4, Types.DOUBLE);
            }
            if (entity.getDiscountAmount() != null) {
                stmt.setDouble(5, entity.getDiscountAmount());
            } else {
                stmt.setNull(5, Types.DOUBLE);
            }
            stmt.setTimestamp(6, entity.getValidUntil() != null ? Timestamp.valueOf(entity.getValidUntil()) : null);
            stmt.setTimestamp(7, Timestamp.valueOf(entity.getCreatedAt() != null ? entity.getCreatedAt() : LocalDateTime.now()));
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    entity.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            LOGGER.error("insert Promo", e);
        }
    }


    @Override
    public void update(Promo entity) {
        try (Connection c = getConnection(); PreparedStatement stmt = c.prepareStatement(UPDATE_SQL)) {
            stmt.setLong(1, entity.getPerson().getId());
            stmt.setString(2, entity.getCode());
            stmt.setBoolean(3, entity.isVoucher());
            if (entity.getDiscountPercent() != null) stmt.setDouble(4, entity.getDiscountPercent()); else stmt.setNull(4, Types.DOUBLE);
            if (entity.getDiscountAmount() != null) stmt.setDouble(5, entity.getDiscountAmount()); else stmt.setNull(5, Types.DOUBLE);
            stmt.setTimestamp(6, entity.getValidUntil() != null ? Timestamp.valueOf(entity.getValidUntil()) : null);
            stmt.setTimestamp(7, Timestamp.valueOf(entity.getCreatedAt() != null ? entity.getCreatedAt() : LocalDateTime.now()));
            stmt.setLong(8, entity.getId());
            stmt.executeUpdate();
        } catch (SQLException e) { LOGGER.error("update Promo", e); }
    }

    @Override
    public void delete(long id) {
        try (Connection c = getConnection(); PreparedStatement stmt = c.prepareStatement(DELETE_SQL)) {
            stmt.setLong(1, id); stmt.executeUpdate();
        } catch (SQLException e) { LOGGER.error("delete Promo id=" + id, e); }
    }

    @Override
    public List<Promo> getByPersonId(long personId) {
        List<Promo> res = new ArrayList<>();
        try (Connection c = getConnection(); PreparedStatement stmt = c.prepareStatement(SELECT_BY_PERSON)) {
            stmt.setLong(1, personId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) res.add(mapRow(rs));
            }
        } catch (SQLException e) { LOGGER.error("getByPersonId Promo", e); }
        return res;
    }

    private Promo mapRow(ResultSet rs) throws SQLException {
        Promo p = new Promo();
        p.setId(rs.getLong("Id"));
        Person per = new Person();
        per.setId(rs.getLong("PersonId"));
        p.setPerson(per);
        p.setCode(rs.getString("code"));
        p.setVoucher(rs.getBoolean("is_voucher"));
        double dp = rs.getDouble("discount_percent");
        if (!rs.wasNull()) p.setDiscountPercent(dp);
        double da = rs.getDouble("discount_amount");
        if (!rs.wasNull()) p.setDiscountAmount(da);
        Timestamp vt = rs.getTimestamp("valid_until");
        if (vt != null) p.setValidUntil(vt.toLocalDateTime());
        Timestamp ct = rs.getTimestamp("created_at");
        if (ct != null) p.setCreatedAt(ct.toLocalDateTime());
        return p;
    }
}
