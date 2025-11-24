package com.example.learning;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DB {
    private static final String DB_URL = "jdbc:sqlite:cvify.db";
    private static Connection connection;
    private static final String CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS sample_nodes (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,json_data TEXT NOT NULL);";

    public static void initDatabase() throws SQLException {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection("jdbc:sqlite:cvify.db");
                if (connection != null) {
                    System.out.println("Connected to SQLite!");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
