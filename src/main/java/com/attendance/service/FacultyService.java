package com.attendance.service;

import com.attendance.dao.AttendanceDAO;
import com.attendance.dao.FacultyDAO;
import com.attendance.dao.StudentDAO;
import com.attendance.dao.SubjectDAO;
import com.attendance.model.*;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class FacultyService {
    private AttendanceDAO attendanceDAO = new AttendanceDAO();
    private SubjectDAO subjectDAO = new SubjectDAO();
    private StudentDAO studentDAO = new StudentDAO();
    private FacultyDAO facultyDAO = new FacultyDAO();

    public void markAttendance(String studentRoll, String subjectCode, Date date, int period, String status)
            throws SQLException {
        Student s = studentDAO.getStudentByRoll(studentRoll);
        Subject sb = subjectDAO.getSubjectByCode(subjectCode);
        if (s != null && sb != null) {
            attendanceDAO.markAttendance(s.getId(), sb.getId(), date, period, status);
        }
    }

    public List<Subject> getAssignedSubjects(String facultyCode) throws SQLException {
        Faculty f = facultyDAO.getFacultyByCode(facultyCode);
        if (f != null) {
            return subjectDAO.getSubjectsByFaculty(f.getId());
        }
        return java.util.Collections.emptyList();
    }

    public List<Student> getAllStudents() throws SQLException {
        return studentDAO.getAllStudents();
    }

    public List<Student> getStudentsByClass(int section, int semester) throws SQLException {
        return studentDAO.getStudentsByClass(section, semester);
    }

    public List<Integer> getAllSections() throws SQLException {
        return studentDAO.getDistinctSections();
    }

    public List<Integer> getAllSemesters() throws SQLException {
        return studentDAO.getDistinctSemesters();
    }

    public List<Attendance> getSubjectAttendance(String subjectCode) throws SQLException {
        Subject sb = subjectDAO.getSubjectByCode(subjectCode);
        if (sb != null) {
            return attendanceDAO.getAttendanceBySubject(sb.getId());
        }
        return java.util.Collections.emptyList();
    }

    public List<String> getDetailedSectionAttendance(String subjectCode, Date date, int section) throws SQLException {
        Subject sb = subjectDAO.getSubjectByCode(subjectCode);
        if (sb != null) {
            return attendanceDAO.getDetailedAttendance(sb.getId(), date, section);
        }
        return java.util.Collections.emptyList();
    }

    public List<StudentAttendanceReportDTO> getClassAttendanceSummary(String subjectCode, int section, int semester)
            throws SQLException {
        Subject sb = subjectDAO.getSubjectByCode(subjectCode);
        if (sb != null) {
            return attendanceDAO.getAttendanceSummaryByClass(sb.getId(), section, semester);
        }
        return java.util.Collections.emptyList();
    }
}
