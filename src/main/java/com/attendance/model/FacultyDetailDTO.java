package com.attendance.model;

public class FacultyDetailDTO {
    private String facultyId;
    private String facultyName;
    private String department;
    private String subjectCode;
    private String subjectName;

    public FacultyDetailDTO(String facultyId, String facultyName, String department,
            String subjectCode, String subjectName) {
        this.facultyId = facultyId;
        this.facultyName = facultyName;
        this.department = department;
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
    }

    public String getFacultyId() {
        return facultyId;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public String getDepartment() {
        return department;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public String getSubjectName() {
        return subjectName;
    }
}
