package com.attendance.dao;

import com.attendance.model.Attendance;
import com.attendance.model.SectionReportDTO;
import com.attendance.util.DbConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AttendanceDAO {

    public void markAttendance(int studentDbId, int subjectId, Date date, int period, String status)
            throws SQLException {
        String query = "INSERT INTO attendance (student_db_id, subject_id, attendance_date, period, status) " +
                "VALUES (?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE status = ?";
        try (Connection conn = DbConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, studentDbId);
            pstmt.setInt(2, subjectId);
            pstmt.setDate(3, date);
            pstmt.setInt(4, period);
            pstmt.setString(5, status);
            pstmt.setString(6, status);
            pstmt.executeUpdate();
        }
    }

    public List<Attendance> getAttendanceByStudent(int studentDbId) throws SQLException {
        List<Attendance> list = new ArrayList<>();
        String query = "SELECT * FROM attendance WHERE student_db_id = ?";
        try (Connection conn = DbConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, studentDbId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new Attendance(
                        rs.getInt("id"),
                        rs.getInt("student_db_id"),
                        rs.getInt("subject_id"),
                        rs.getDate("attendance_date"),
                        rs.getInt("period"),
                        rs.getString("status")));
            }
        }
        return list;
    }

    public List<Attendance> getAttendanceBySubject(int subjectId) throws SQLException {
        List<Attendance> list = new ArrayList<>();
        String query = "SELECT * FROM attendance WHERE subject_id = ?";
        try (Connection conn = DbConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, subjectId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new Attendance(
                        rs.getInt("id"),
                        rs.getInt("student_db_id"),
                        rs.getInt("subject_id"),
                        rs.getDate("attendance_date"),
                        rs.getInt("period"),
                        rs.getString("status")));
            }
        }
        return list;
    }

    public List<String> getDetailedAttendance(int subjectId, Date date, int section) throws SQLException {
        List<String> list = new ArrayList<>();
        String query = "SELECT s.student_roll, s.name, a.status FROM attendance a " +
                "JOIN students s ON a.student_db_id = s.id " +
                "WHERE a.subject_id = ? AND a.attendance_date = ? AND s.section = ?";
        try (Connection conn = DbConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, subjectId);
            pstmt.setDate(2, date);
            pstmt.setInt(3, section);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(rs.getString("student_roll") + " | " + rs.getString("name") + " | " + rs.getString("status"));
            }
        }
        return list;
    }

    public com.attendance.model.StudentStatsDTO getStudentStats(int studentDbId) throws SQLException {
        String query = "SELECT COUNT(*) as total, SUM(CASE WHEN status='PRESENT' THEN 1 ELSE 0 END) as present " +
                "FROM attendance WHERE student_db_id = ?";
        try (Connection conn = DbConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, studentDbId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new com.attendance.model.StudentStatsDTO(rs.getInt("total"), rs.getInt("present"));
            }
        }
        return new com.attendance.model.StudentStatsDTO(0, 0);
    }

    public List<com.attendance.model.SubjectAttendanceDTO> getSubjectWiseAttendance(int studentDbId)
            throws SQLException {
        List<com.attendance.model.SubjectAttendanceDTO> list = new ArrayList<>();
        String query = "SELECT s.subject_code, s.subject_name, " +
                "COUNT(a.id) as total, " +
                "SUM(CASE WHEN a.status='PRESENT' THEN 1 ELSE 0 END) as present " +
                "FROM subjects s " +
                "LEFT JOIN attendance a ON s.id = a.subject_id AND a.student_db_id = ? " +
                "GROUP BY s.id, s.subject_code, s.subject_name";
        try (Connection conn = DbConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, studentDbId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new com.attendance.model.SubjectAttendanceDTO(
                        rs.getString("subject_code"),
                        rs.getString("subject_name"),
                        rs.getInt("total"),
                        rs.getInt("present")));
            }
        }
        return list;
    }

    public List<com.attendance.model.StudentAttendanceReportDTO> getAttendanceSummaryByClass(int subjectId, int section,
            int semester) throws SQLException {
        List<com.attendance.model.StudentAttendanceReportDTO> list = new ArrayList<>();
        String query = "SELECT s.student_roll, s.name, " +
                "COUNT(a.id) as total, " +
                "SUM(CASE WHEN a.status='PRESENT' THEN 1 ELSE 0 END) as present " +
                "FROM students s " +
                "LEFT JOIN attendance a ON s.id = a.student_db_id AND a.subject_id = ? " +
                "WHERE s.section = ? AND s.semester = ? " +
                "GROUP BY s.id, s.student_roll, s.name";
        try (Connection conn = DbConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, subjectId);
            pstmt.setInt(2, section);
            pstmt.setInt(3, semester);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new com.attendance.model.StudentAttendanceReportDTO(
                        rs.getString("student_roll"),
                        rs.getString("name"),
                        rs.getInt("total"),
                        rs.getInt("present")));
            }
        }
        return list;
    }

    public List<SectionReportDTO> getSectionWiseReport(Date date, Integer section) throws SQLException {
        List<SectionReportDTO> reports = new ArrayList<>();
        StringBuilder query = new StringBuilder(
                "SELECT s.section, s.semester, COUNT(a.id) as total, SUM(CASE WHEN a.status='PRESENT' THEN 1 ELSE 0 END) as present "
                        +
                        "FROM attendance a " +
                        "JOIN students s ON a.student_db_id = s.id " +
                        "WHERE a.attendance_date = ? ");
        if (section != null) {
            query.append("AND s.section = ? ");
        }
        query.append("GROUP BY s.section, s.semester");

        try (Connection conn = DbConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query.toString())) {
            pstmt.setDate(1, date);
            if (section != null) {
                pstmt.setInt(2, section);
            }
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                reports.add(new SectionReportDTO(
                        date,
                        rs.getInt("section"),
                        rs.getInt("semester"),
                        rs.getInt("total"),
                        rs.getInt("present")));
            }
        }
        return reports;
    }
}
