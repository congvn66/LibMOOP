package com.example.proj.Models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static String JDBC_URL = "jdbc:mysql://localhost:3306/shibalib";
    private static String DB_USERNAME = "root";
    private static String DB_PASSWORD = "";

    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            synchronized (DatabaseConnection.class) {
                if (connection == null || connection.isClosed()) {
                    connection = DriverManager.getConnection(JDBC_URL, DB_USERNAME, DB_PASSWORD);
                }
            }
        }
        return connection;
    }

    public static void setTestDatabase(String testJdbcURL, String testUsername, String testPassword) {
        JDBC_URL = testJdbcURL;
        DB_USERNAME = testUsername;
        DB_PASSWORD = testPassword;
        connection = null;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                connection = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
