package com.softworld.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {
    public static Connection getConnection() throws SQLException {
        String dbUrl = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");

        String url = String.format("jdbc:mysql://%s?useSSL=false&allowPublicKeyRetrieval=true", dbUrl);
        return DriverManager.getConnection(url, user, password);
    }
}