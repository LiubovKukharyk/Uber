package com.solvd.uber.dao.mysqlimpl;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class MySQL {
    private static DataSource dataSource;

    static {
        MysqlConnectionPoolDataSource pool = new MysqlConnectionPoolDataSource();
        pool.setServerName("localhost");
        pool.setPort(3306);
        pool.setDatabaseName("Uber");
        pool.setUser("root");
        pool.setPassword("");
        try {
			pool.setAllowMultiQueries(true);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        dataSource = pool;
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
