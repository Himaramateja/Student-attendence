package com.attendance.dao;

import com.attendance.model.Student;
import com.attendance.util.DbConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    public void addStudent(Student student) throws SQLException {
        // DB handles 'id' increment starting from 1000
        String query = "INSERT INTO students (user_id, student_roll, name, department, section, semester) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DbConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, student.getUserId());
            pstmt.setString(2, student.getStudentRoll());
            pstmt.setString(3, student.getName());
            pstmt.setString(4, student.getDepartment());
            pstmt.setInt(5, student.getSection());
            pstmt.setInt(6, student.getSemester());
            pstmt.executeUpdate();
        }
    }

    public List<Student> getAllStudents() throws SQLException {
        List<Student> students = new ArrayList<>();
        String query = "SELECT * FROM students";
        try (Connection conn = DbConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                students.add(new Student(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("student_roll"),
                        rs.getString("name"),
                        rs.getString("department"),
                        rs.getInt("section"),
                        rs.getInt("semester")));
            }
        }
        return students;
    }

    public Student getStudentByRoll(String roll) throws SQLException {
        String query = "SELECT * FROM students WHERE student_roll = ?";
        try (Connection conn = DbConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, roll);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Student(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("student_roll"),
                        rs.getString("name"),
                        rs.getString("department"),
                        rs.getInt("section"),
                        rs.getInt("semester"));
            }
        }
        return null;
    }

    public List<Student> getStudentsByClass(int section, int semester) throws SQLException {
        List<Student> students = new ArrayList<>();
        String query = "SELECT * FROM students WHERE section = ? AND semester = ?";
        try (Connection conn = DbConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, section);
            pstmt.setInt(2, semester);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                students.add(new Student(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("student_roll"),
                        rs.getString("name"),
                        rs.getString("department"),
                        rs.getInt("section"),
                        rs.getInt("semester")));
            }
        }
        return students;
    }

    public List<Student> getStudentsBySection(int section) throws SQLException {
        List<Student> students = new ArrayList<>();
        String query = "SELECT * FROM students WHERE section = ? ORDER BY student_roll";
        try (Connection conn = DbConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, section);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                students.add(new Student(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("student_roll"),
                        rs.getString("name"),
                        rs.getString("department"),
                        rs.getInt("section"),
                        rs.getInt("semester")));
            }
        }
        return students;
    }

    public List<Integer> getDistinctSections() throws SQLException {
        List<Integer> sections = new ArrayList<>();
        String query = "SELECT DISTINCT section FROM students ORDER BY section";
        try (Connection conn = DbConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next())
                sections.add(rs.getInt(1));
        }
        return sections;
    }

    public List<Integer> getDistinctSemesters() throws SQLException {
        List<Integer> semesters = new ArrayList<>();
        String query = "SELECT DISTINCT semester FROM students ORDER BY semester";
        try (Connection conn = DbConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next())
                semesters.add(rs.getInt(1));
        }
        return semesters;
    }

    public List<String> getDistinctDepartments() throws SQLException {
        List<String> departments = new ArrayList<>();
        String query = "SELECT DISTINCT department FROM students ORDER BY department";
        try (Connection conn = DbConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next())
                departments.add(rs.getString(1));
        }
        return departments;
    }

    public List<Student> searchStudents(Integer section, String department, Integer semester, String roll) throws SQLException {
        List<Student> students = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT * FROM students WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (section != null) { query.append(" AND section = ?"); params.add(section); }
        if (department != null && !department.isEmpty()) { query.append(" AND department = ?"); params.add(department); }
        if (semester != null) { query.append(" AND semester = ?"); params.add(semester); }
        if (roll != null && !roll.isEmpty()) { query.append(" AND student_roll = ?"); params.add(roll); }
        query.append(" ORDER BY student_roll");

        try (Connection conn = DbConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query.toString())) {
            for (int i = 0; i < params.size(); i++) {
                Object p = params.get(i);
                if (p instanceof Integer) pstmt.setInt(i + 1, (Integer) p);
                else pstmt.setString(i + 1, (String) p);
            }
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                students.add(new Student(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("student_roll"),
                        rs.getString("name"),
                        rs.getString("department"),
                        rs.getInt("section"),
                        rs.getInt("semester")));
            }
        }
        return students;
    }

    public void updateStudent(Student student) throws SQLException {
        String query = "UPDATE students SET name = ?, department = ?, section = ?, semester = ? WHERE student_roll = ?";
        try (Connection conn = DbConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getDepartment());
            pstmt.setInt(3, student.getSection());
            pstmt.setInt(4, student.getSemester());
            pstmt.setString(5, student.getStudentRoll());
            pstmt.executeUpdate();
        }
    }

    public void deleteStudent(String roll) throws SQLException {
        String query = "DELETE FROM students WHERE student_roll = ?";
        try (Connection conn = DbConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, roll);
            pstmt.executeUpdate();
        }
    }

    public int getStudentCount() throws SQLException {
        String query = "SELECT COUNT(*) FROM students";
        try (Connection conn = DbConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next())
                return rs.getInt(1);
        }
        return 0;
    }
}
