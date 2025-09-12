package com.solvd.uber.dao.mysqlimpl;

import com.solvd.uber.dao.IPromoDAO;
import com.solvd.uber.models.Promo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PromoDAO extends MySQL implements IPromoDAO<Promo> {
    private static final Logger LOGGER = LogManager.getLogger(PromoDAO.class);

    private static final String INSERT_SQL =
            "INSERT INTO Promo (code, is_voucher, discount_percent, discount_amount, valid_until, created_at) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_SQL =
            "UPDATE Promo SET code=?, is_voucher=?, discount_percent=?, discount_amount=?, valid_until=?, created_at=? WHERE Id=?";
    private static final String DELETE_SQL = "DELETE FROM Promo WHERE Id=?";
    private static final String SELECT_BY_ID = "SELECT * FROM Promo WHERE Id=?";
    private static final String SELECT_ALL = "SELECT * FROM Promo";
    
    @Override
    public Promo getById(long id) {
        try (Connection c = getConnection();
             PreparedStatement stmt = c.prepareStatement(SELECT_BY_ID)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            LOGGER.error("getById Promo", e);
        }
        return null;
    }

    @Override
    public List<Promo> getAll() {
        List<Promo> res = new ArrayList<>();
        try (Connection c = getConnection();
             PreparedStatement stmt = c.prepareStatement(SELECT_ALL);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                res.add(mapRow(rs));
            }

        } catch (SQLException e) {
            LOGGER.error("getAll Promo", e);
        }
        return res;
    }

    @Override
    public void insert(Promo entity) {
        try (Connection c = getConnection();
             PreparedStatement stmt = c.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, entity.getCode());
            stmt.setBoolean(2, entity.isVoucher());
            if (entity.getDiscountPercent() != null) stmt.setDouble(3, entity.getDiscountPercent());
            else stmt.setNull(3, Types.DOUBLE);
            if (entity.getDiscountAmount() != null) stmt.setDouble(4, entity.getDiscountAmount());
            else stmt.setNull(4, Types.DOUBLE);
            stmt.setTimestamp(5, entity.getValidUntil() != null ? new Timestamp(entity.getValidUntil().getTime()) : null);
            stmt.setTimestamp(6, new Timestamp((entity.getCreatedAt() != null ? entity.getCreatedAt() : new Date()).getTime()));

            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) entity.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            LOGGER.error("insert Promo", e);
        }
    }

    @Override
    public void update(Promo entity) {
        try (Connection c = getConnection();
             PreparedStatement stmt = c.prepareStatement(UPDATE_SQL)) {
            stmt.setString(1, entity.getCode());
            stmt.setBoolean(2, entity.isVoucher());
            if (entity.getDiscountPercent() != null) stmt.setDouble(3, entity.getDiscountPercent());
            else stmt.setNull(3, Types.DOUBLE);
            if (entity.getDiscountAmount() != null) stmt.setDouble(4, entity.getDiscountAmount());
            else stmt.setNull(4, Types.DOUBLE);
            stmt.setTimestamp(5, entity.getValidUntil() != null ? new Timestamp(entity.getValidUntil().getTime()) : null);
            stmt.setTimestamp(6, new Timestamp((entity.getCreatedAt() != null ? entity.getCreatedAt() : new Date()).getTime()));
            stmt.setLong(7, entity.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("update Promo", e);
        }
    }

    @Override
    public void delete(long id) {
        try (Connection c = getConnection();
             PreparedStatement stmt = c.prepareStatement(DELETE_SQL)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("delete Promo id=" + id, e);
        }
    }

    private Promo mapRow(ResultSet rs) throws SQLException {
        Promo p = new Promo();
        p.setId(rs.getLong("Id"));
        p.setCode(rs.getString("code"));
        p.setVoucher(rs.getBoolean("is_voucher"));

        double dp = rs.getDouble("discount_percent");
        if (!rs.wasNull()) p.setDiscountPercent(dp);

        double da = rs.getDouble("discount_amount");
        if (!rs.wasNull()) p.setDiscountAmount(da);

        Timestamp vt = rs.getTimestamp("valid_until");
        if (vt != null) p.setValidUntil(new Date(vt.getTime()));

        Timestamp ct = rs.getTimestamp("created_at");
        if (ct != null) p.setCreatedAt(new Date(ct.getTime()));

        return p;
    }

	@Override
	public List<Promo> getByPersonId(long personId) {
		// TODO Auto-generated method stub
		return null;
	}
}
