package com.attendance.dao;

import com.attendance.model.Report;
import com.attendance.util.DbConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReportDAO {

    public void saveReport(Report report) throws SQLException {
        String query = "INSERT INTO reports (report_date, total_students, total_present, average_attendance, description) "
                +
                "VALUES (?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE total_students=?, total_present=?, average_attendance=?, description=?";
        try (Connection conn = DbConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setDate(1, report.getReportDate());
            pstmt.setInt(2, report.getTotalStudents());
            pstmt.setInt(3, report.getTotalPresent());
            pstmt.setDouble(4, report.getAverageAttendance());
            pstmt.setString(5, report.getDescription());

            pstmt.setInt(6, report.getTotalStudents());
            pstmt.setInt(7, report.getTotalPresent());
            pstmt.setDouble(8, report.getAverageAttendance());
            pstmt.setString(9, report.getDescription());

            pstmt.executeUpdate();
        }
    }

    public List<Report> getAllReports() throws SQLException {
        List<Report> reports = new ArrayList<>();
        String query = "SELECT * FROM reports ORDER BY report_date DESC";
        try (Connection conn = DbConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                reports.add(new Report(
                        rs.getInt("id"),
                        rs.getDate("report_date"),
                        rs.getInt("total_students"),
                        rs.getInt("total_present"),
                        rs.getDouble("average_attendance"),
                        rs.getString("description")));
            }
        }
        return reports;
    }

    public Report generateDailyReport(Date date) throws SQLException {
        String statsQuery = "SELECT COUNT(DISTINCT student_db_id) as total, " +
                "SUM(CASE WHEN status='PRESENT' THEN 1 ELSE 0 END) as present " +
                "FROM attendance WHERE attendance_date = ?";

        try (Connection conn = DbConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(statsQuery)) {
            pstmt.setDate(1, date);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int total = rs.getInt("total");
                int present = rs.getInt("present");
                double avg = total == 0 ? 0 : (double) present / total * 100;
                Report r = new Report(0, date, total, present, avg, "System Generated Report");
                saveReport(r);
                return r;
            }
        }
        return new Report(0, date, 0, 0, 0, "No data for this date");
    }
}
