package com.attendance.service;

import com.attendance.dao.*;
import com.attendance.model.*;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class AdminService {
    private StudentDAO studentDAO = new StudentDAO();
    private FacultyDAO facultyDAO = new FacultyDAO();
    private UserDAO userDAO = new UserDAO();
    private SubjectDAO subjectDAO = new SubjectDAO();
    private ReportDAO reportDAO = new ReportDAO();
    private AttendanceDAO attendanceDAO = new AttendanceDAO();

    public void addAdmin(String username, String password) throws SQLException {
        userDAO.addUser(username, password, "ADMIN");
    }

    public void addStudent(String roll, String name, String dept, int section, int semester) throws SQLException {
        int userId = userDAO.addUser(roll, "Aditya123", "STUDENT");
        studentDAO.addStudent(new Student(0, userId, roll, name, dept, section, semester));
    }

    public void updateStudent(String roll, String name, String dept, int section, int semester) throws SQLException {
        Student existing = studentDAO.getStudentByRoll(roll);
        if (existing != null) {
            existing.setName(name);
            existing.setDepartment(dept);
            existing.setSection(section);
            existing.setSemester(semester);
            studentDAO.updateStudent(existing);
        }
    }

    public void deleteStudent(String roll) throws SQLException {
        userDAO.deleteUser(roll);
    }

    public List<Student> getAllStudents() throws SQLException {
        return studentDAO.getAllStudents();
    }

    public int getStudentCount() throws SQLException {
        return studentDAO.getStudentCount();
    }

    public void addFaculty(String facultyId, String name, String dept) throws SQLException {
        int userId = userDAO.addUser(facultyId, "faculty123", "FACULTY");
        facultyDAO.addFaculty(new Faculty(0, userId, facultyId, name, dept));
    }

    public void deleteFaculty(String facultyId) throws SQLException {
        userDAO.deleteUser(facultyId);
    }

    public List<Faculty> getAllFaculty() throws SQLException {
        return facultyDAO.getAllFaculty();
    }

    public int getFacultyCount() throws SQLException {
        return facultyDAO.getFacultyCount();
    }

    public void addSubject(String code, String name) throws SQLException {
        subjectDAO.addSubject(new Subject(0, code, name));
    }

    public List<Subject> getAllSubjects() throws SQLException {
        return subjectDAO.getAllSubjects();
    }

    public void assignSubject(String facultyCode, String subjectCode) throws SQLException {
        Faculty f = facultyDAO.getFacultyByCode(facultyCode);
        Subject s = subjectDAO.getSubjectByCode(subjectCode);
        if (f != null && s != null) {
            subjectDAO.assignSubjectToFaculty(f.getId(), s.getId());
        }
    }

    public void deleteSubject(String code) throws SQLException {
        subjectDAO.deleteSubject(code);
    }

    public Report generateDailyReport(Date date) throws SQLException {
        return reportDAO.generateDailyReport(date);
    }

    public List<Report> getAllReports() throws SQLException {
        return reportDAO.getAllReports();
    }

    public List<SectionReportDTO> getSectionWiseReport(Date date, Integer section) throws SQLException {
        return attendanceDAO.getSectionWiseReport(date, section);
    }

    public List<FacultyAssignmentDTO> getAllFacultyAssignments() throws SQLException {
        return subjectDAO.getAllFacultyAssignments();
    }

    public void removeSubjectAssignment(int facultyDbId, int subjectId) throws SQLException {
        subjectDAO.removeSubjectAssignment(facultyDbId, subjectId);
    }
}
