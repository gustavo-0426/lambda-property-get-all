package com.softworld.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseUtil {

    private static final HikariDataSource dataSource;

    static {
        String dbUrl = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");

        if (dbUrl == null || user == null || password == null) {
            throw new IllegalStateException(
                    "Missing required environment variables: DB_URL, DB_USER, or DB_PASSWORD"
            );
        }

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(String.format("jdbc:mysql://%s", dbUrl));
        config.setUsername(user);
        config.setPassword(password);

        config.setMaximumPoolSize(2);
        config.setMinimumIdle(0);
        config.setConnectionTimeout(5000);
        config.setIdleTimeout(60000);
        config.setMaxLifetime(600000);
        config.setKeepaliveTime(30000);

        config.addDataSourceProperty("useSSL", "true");
        config.addDataSourceProperty("requireSSL", "true");
        config.addDataSourceProperty("verifyServerCertificate", "false");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", "true");

        dataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void closePool() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}