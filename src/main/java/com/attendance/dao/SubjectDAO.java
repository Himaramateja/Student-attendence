package com.attendance.dao;

import com.attendance.model.Subject;
import com.attendance.model.FacultyAssignmentDTO;
import com.attendance.util.DbConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubjectDAO {

    public void addSubject(Subject subject) throws SQLException {
        // DB handles 'id' increment starting from 1
        String query = "INSERT INTO subjects (subject_code, subject_name) VALUES (?, ?)";
        try (Connection conn = DbConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, subject.getSubjectCode());
            pstmt.setString(2, subject.getSubjectName());
            pstmt.executeUpdate();
        }
    }

    public List<Subject> getAllSubjects() throws SQLException {
        List<Subject> subjects = new ArrayList<>();
        String query = "SELECT * FROM subjects";
        try (Connection conn = DbConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                subjects.add(new Subject(rs.getInt("id"), rs.getString("subject_code"), rs.getString("subject_name")));
            }
        }
        return subjects;
    }

    public Subject getSubjectByCode(String code) throws SQLException {
        String query = "SELECT * FROM subjects WHERE subject_code = ?";
        try (Connection conn = DbConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, code);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Subject(rs.getInt("id"), rs.getString("subject_code"), rs.getString("subject_name"));
            }
        }
        return null;
    }

    public void assignSubjectToFaculty(int facultyDbId, int subjectId) throws SQLException {
        String query = "INSERT INTO faculty_subjects (faculty_db_id, subject_id) VALUES (?, ?)";
        try (Connection conn = DbConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, facultyDbId);
            pstmt.setInt(2, subjectId);
            pstmt.executeUpdate();
        }
    }

    public List<Subject> getSubjectsByFaculty(int facultyDbId) throws SQLException {
        List<Subject> subjects = new ArrayList<>();
        String query = "SELECT s.* FROM subjects s JOIN faculty_subjects fs ON s.id = fs.subject_id WHERE fs.faculty_db_id = ?";
        try (Connection conn = DbConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, facultyDbId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                subjects.add(new Subject(rs.getInt("id"), rs.getString("subject_code"), rs.getString("subject_name")));
            }
        }
        return subjects;
    }

    public void deleteSubject(String code) throws SQLException {
        String query = "DELETE FROM subjects WHERE subject_code = ?";
        try (Connection conn = DbConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, code);
            pstmt.executeUpdate();
        }
    }

    public List<FacultyAssignmentDTO> getAllFacultyAssignments() throws SQLException {
        List<FacultyAssignmentDTO> assignments = new ArrayList<>();
        String query = "SELECT f.faculty_id, f.name as faculty_name, s.subject_code, s.subject_name, fs.faculty_db_id, fs.subject_id "
                +
                "FROM faculty_subjects fs " +
                "JOIN faculty f ON fs.faculty_db_id = f.id " +
                "JOIN subjects s ON fs.subject_id = s.id";
        try (Connection conn = DbConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                assignments.add(new FacultyAssignmentDTO(
                        rs.getString("faculty_id"),
                        rs.getString("faculty_name"),
                        rs.getString("subject_code"),
                        rs.getString("subject_name"),
                        rs.getInt("faculty_db_id"),
                        rs.getInt("subject_id")));
            }
        }
        return assignments;
    }

    public void removeSubjectAssignment(int facultyDbId, int subjectId) throws SQLException {
        String query = "DELETE FROM faculty_subjects WHERE faculty_db_id = ? AND subject_id = ?";
        try (Connection conn = DbConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, facultyDbId);
            pstmt.setInt(2, subjectId);
            pstmt.executeUpdate();
        }
    }
}
