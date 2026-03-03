package com.attendance.service;

import com.attendance.dao.AttendanceDAO;
import com.attendance.dao.StudentDAO;
import com.attendance.dao.UserDAO;
import com.attendance.model.Attendance;
import com.attendance.model.Student;

import java.sql.SQLException;
import java.util.List;

public class StudentService {
    private AttendanceDAO attendanceDAO = new AttendanceDAO();
    private StudentDAO studentDAO = new StudentDAO();
    private UserDAO userDAO = new UserDAO();

    public List<Attendance> getMyAttendance(String studentRoll) throws SQLException {
        Student s = studentDAO.getStudentByRoll(studentRoll);
        if (s != null) {
            return attendanceDAO.getAttendanceByStudent(s.getId());
        }
        return java.util.Collections.emptyList();
    }

    public com.attendance.model.StudentStatsDTO getSummaryStats(String studentRoll) throws SQLException {
        Student s = studentDAO.getStudentByRoll(studentRoll);
        if (s != null) {
            return attendanceDAO.getStudentStats(s.getId());
        }
        return new com.attendance.model.StudentStatsDTO(0, 0);
    }

    public List<com.attendance.model.SubjectAttendanceDTO> getSubjectWiseAttendance(String studentRoll)
            throws SQLException {
        Student s = studentDAO.getStudentByRoll(studentRoll);
        if (s != null) {
            return attendanceDAO.getSubjectWiseAttendance(s.getId());
        }
        return java.util.Collections.emptyList();
    }

    public void updatePassword(String username, String newPassword) throws SQLException {
        userDAO.updatePassword(username, newPassword);
    }

    public Student getStudentDetails(String studentRoll) throws SQLException {
        return studentDAO.getStudentByRoll(studentRoll);
    }
}
