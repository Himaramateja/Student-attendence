package com.attendance.dao;

import com.attendance.model.Faculty;
import com.attendance.model.FacultyDetailDTO;
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

    public List<FacultyDetailDTO> getFacultyWithAssignments() throws SQLException {
        List<FacultyDetailDTO> details = new ArrayList<>();
        String query = "SELECT f.faculty_id, f.name, f.department, " +
                "s.subject_code, s.subject_name " +
                "FROM faculty f " +
                "LEFT JOIN faculty_subjects fs ON f.id = fs.faculty_db_id " +
                "LEFT JOIN subjects s ON fs.subject_id = s.id " +
                "ORDER BY f.faculty_id, s.subject_code";
        try (Connection conn = DbConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                details.add(new FacultyDetailDTO(
                        rs.getString("faculty_id"),
                        rs.getString("name"),
                        rs.getString("department"),
                        rs.getString("subject_code"),
                        rs.getString("subject_name")));
            }
        }
        return details;
    }

    public List<FacultyDetailDTO> getFacultyDetailByCode(String facultyId) throws SQLException {
        List<FacultyDetailDTO> details = new ArrayList<>();
        String query = "SELECT f.faculty_id, f.name, f.department, " +
                "s.subject_code, s.subject_name " +
                "FROM faculty f " +
                "LEFT JOIN faculty_subjects fs ON f.id = fs.faculty_db_id " +
                "LEFT JOIN subjects s ON fs.subject_id = s.id " +
                "WHERE f.faculty_id = ? " +
                "ORDER BY s.subject_code";
        try (Connection conn = DbConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, facultyId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                details.add(new FacultyDetailDTO(
                        rs.getString("faculty_id"),
                        rs.getString("name"),
                        rs.getString("department"),
                        rs.getString("subject_code"),
                        rs.getString("subject_name")));
            }
        }
        return details;
    }

    public List<String> getDistinctFacultyDepartments() throws SQLException {
        List<String> departments = new ArrayList<>();
        String query = "SELECT DISTINCT department FROM faculty ORDER BY department";
        try (Connection conn = DbConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next())
                departments.add(rs.getString(1));
        }
        return departments;
    }

    public List<FacultyDetailDTO> searchFaculty(String department, String facultyId) throws SQLException {
        List<FacultyDetailDTO> details = new ArrayList<>();
        StringBuilder query = new StringBuilder(
                "SELECT f.faculty_id, f.name, f.department, " +
                "s.subject_code, s.subject_name " +
                "FROM faculty f " +
                "LEFT JOIN faculty_subjects fs ON f.id = fs.faculty_db_id " +
                "LEFT JOIN subjects s ON fs.subject_id = s.id WHERE 1=1");
        List<String> params = new ArrayList<>();

        if (department != null && !department.isEmpty()) { query.append(" AND f.department = ?"); params.add(department); }
        if (facultyId != null && !facultyId.isEmpty()) { query.append(" AND f.faculty_id = ?"); params.add(facultyId); }
        query.append(" ORDER BY f.faculty_id, s.subject_code");

        try (Connection conn = DbConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query.toString())) {
            for (int i = 0; i < params.size(); i++) {
                pstmt.setString(i + 1, params.get(i));
            }
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                details.add(new FacultyDetailDTO(
                        rs.getString("faculty_id"),
                        rs.getString("name"),
                        rs.getString("department"),
                        rs.getString("subject_code"),
                        rs.getString("subject_name")));
            }
        }
        return details;
    }
}
