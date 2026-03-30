package com.attendance.dao;

import com.attendance.util.DbConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TimetableDAO {

    /**
     * Get the timetable for a specific department/section/semester.
     * Returns a Map: day_of_week -> List of subject_codes (indexed by period-1).
     * Periods with no entry will have null in the list.
     */
    public Map<String, List<String>> getTimetable(String department, int section, int semester) throws SQLException {
        String[] days = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday" };
        Map<String, List<String>> timetable = new LinkedHashMap<>();

        // Initialize with nulls for 7 periods
        for (String day : days) {
            List<String> periods = new ArrayList<>();
            for (int i = 0; i < 7; i++) periods.add(null);
            timetable.put(day, periods);
        }

        String query = "SELECT day_of_week, period, subject_code FROM timetable " +
                "WHERE department = ? AND section = ? AND semester = ? " +
                "ORDER BY FIELD(day_of_week, 'Monday','Tuesday','Wednesday','Thursday','Friday'), period";
        try (Connection conn = DbConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, department);
            pstmt.setInt(2, section);
            pstmt.setInt(3, semester);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String day = rs.getString("day_of_week");
                int period = rs.getInt("period");
                String subjectCode = rs.getString("subject_code");
                if (timetable.containsKey(day) && period >= 1 && period <= 7) {
                    timetable.get(day).set(period - 1, subjectCode);
                }
            }
        }
        return timetable;
    }

    /**
     * Check if a timetable exists in the DB for the given class.
     */
    public boolean hasTimetable(String department, int section, int semester) throws SQLException {
        String query = "SELECT COUNT(*) FROM timetable WHERE department = ? AND section = ? AND semester = ?";
        try (Connection conn = DbConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, department);
            pstmt.setInt(2, section);
            pstmt.setInt(3, semester);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        }
        return false;
    }

    /**
     * Save a single timetable entry (upsert).
     */
    public void saveTimetableEntry(String department, int section, int semester,
            String dayOfWeek, int period, String subjectCode) throws SQLException {
        String query = "INSERT INTO timetable (department, section, semester, day_of_week, period, subject_code) " +
                "VALUES (?, ?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE subject_code = VALUES(subject_code)";
        try (Connection conn = DbConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, department);
            pstmt.setInt(2, section);
            pstmt.setInt(3, semester);
            pstmt.setString(4, dayOfWeek);
            pstmt.setInt(5, period);
            if (subjectCode != null && !subjectCode.isEmpty()) {
                pstmt.setString(6, subjectCode);
            } else {
                pstmt.setNull(6, Types.VARCHAR);
            }
            pstmt.executeUpdate();
        }
    }

    /**
     * Save an entire timetable (clears existing and inserts all entries).
     */
    public void saveTimetable(String department, int section, int semester,
            Map<String, List<String>> timetable) throws SQLException {
        clearTimetable(department, section, semester);
        for (Map.Entry<String, List<String>> entry : timetable.entrySet()) {
            String day = entry.getKey();
            List<String> subjects = entry.getValue();
            for (int i = 0; i < subjects.size(); i++) {
                String subjectCode = subjects.get(i);
                if (subjectCode != null && !subjectCode.isEmpty()) {
                    saveTimetableEntry(department, section, semester, day, i + 1, subjectCode);
                }
            }
        }
    }

    /**
     * Clear all timetable entries for a specific class.
     */
    public void clearTimetable(String department, int section, int semester) throws SQLException {
        String query = "DELETE FROM timetable WHERE department = ? AND section = ? AND semester = ?";
        try (Connection conn = DbConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, department);
            pstmt.setInt(2, section);
            pstmt.setInt(3, semester);
            pstmt.executeUpdate();
        }
    }
}
