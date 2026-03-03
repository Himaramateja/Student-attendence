package com.attendance.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    // Keep this compatible with both console and web.
    // In Spring, we could inject these values from application.properties,
    // but User wants "Core Java JDBC".
    private static final String URL = "jdbc:mysql://localhost:3306/student_attendance_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "Teja@1234"; // Update as per your MySQL configuration

    public static Connection getConnection() throws SQLException {
        try {
            // Explicitly load driver for web apps sometimes needed
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL Driver not found.");
            e.printStackTrace();
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
