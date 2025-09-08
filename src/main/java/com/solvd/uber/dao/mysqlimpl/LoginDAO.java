package com.solvd.uber.dao.mysqlimpl;

import com.solvd.uber.dao.ILoginDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class LoginDAO extends MySQL implements ILoginDAO {
    private static final Logger LOGGER = LogManager.getLogger(LoginDAO.class);

    private static final String CHECK_SQL = "SELECT COUNT(*) as cnt FROM AccountCredentials WHERE email = ? AND password = ?";
    private static final String RESET_SQL = "UPDATE AccountCredentials SET password = ? WHERE account_id = ?";

    @Override
    public boolean login(String email, String password) {
        try (Connection c = getConnection(); PreparedStatement stmt = c.prepareStatement(CHECK_SQL)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt("cnt") > 0;
            }
        } catch (SQLException e) { LOGGER.error("login error", e); }
        return false;
    }

    @Override
    public void logout(long accountId) {
       
        String sql = "UPDATE Account SET last_logout = ? WHERE Id = ?";
        try (Connection c = getConnection(); PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            stmt.setLong(2, accountId);
            stmt.executeUpdate();
        } catch (SQLException e) { LOGGER.error("logout error", e); }
    }

    @Override
    public void resetPassword(long accountId, String newPassword) {
        try (Connection c = getConnection(); PreparedStatement stmt = c.prepareStatement(RESET_SQL)) {
            stmt.setString(1, newPassword);
            stmt.setLong(2, accountId);
            stmt.executeUpdate();
        } catch (SQLException e) { LOGGER.error("resetPassword error", e); }
    }
}
