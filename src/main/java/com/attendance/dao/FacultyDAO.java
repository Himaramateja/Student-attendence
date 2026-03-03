package com.attendance.dao;

import com.attendance.model.Faculty;
import com.attendance.util.DbConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FacultyDAO {

    public void addFaculty(Faculty faculty) throws SQLException {
        // DB handles 'id' increment starting from 100
        String query = "INSERT INTO faculty (user_id, faculty_id, name, department) VALUES (?, ?, ?, ?)";
        try (Connection conn = DbConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, faculty.getUserId());
            pstmt.setString(2, faculty.getFacultyId());
            pstmt.setString(3, faculty.getName());
            pstmt.setString(4, faculty.getDepartment());
            pstmt.executeUpdate();
        }
    }

    public List<Faculty> getAllFaculty() throws SQLException {
        List<Faculty> facultyList = new ArrayList<>();
        String query = "SELECT * FROM faculty";
        try (Connection conn = DbConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                facultyList.add(new Faculty(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("faculty_id"),
                        rs.getString("name"),
                        rs.getString("department")));
            }
        }
        return facultyList;
    }

    public Faculty getFacultyByCode(String facultyId) throws SQLException {
        String query = "SELECT * FROM faculty WHERE faculty_id = ?";
        try (Connection conn = DbConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, facultyId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Faculty(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("faculty_id"),
                        rs.getString("name"),
                        rs.getString("department"));
            }
        }
        return null;
    }

    public void deleteFaculty(String facultyId) throws SQLException {
        String query = "DELETE FROM faculty WHERE faculty_id = ?";
        try (Connection conn = DbConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, facultyId);
            pstmt.executeUpdate();
        }
    }

    public int getFacultyCount() throws SQLException {
        String query = "SELECT COUNT(*) FROM faculty";
        try (Connection conn = DbConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next())
                return rs.getInt(1);
        }
        return 0;
    }
}
