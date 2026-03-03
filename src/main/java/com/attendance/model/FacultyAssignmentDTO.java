package com.attendance.model;

public class FacultyAssignmentDTO {
    private String facultyId;
    private String facultyName;
    private String subjectCode;
    private String subjectName;
    private int facultyDbId;
    private int subjectId;

    public FacultyAssignmentDTO(String facultyId, String facultyName, String subjectCode, String subjectName,
            int facultyDbId, int subjectId) {
        this.facultyId = facultyId;
        this.facultyName = facultyName;
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.facultyDbId = facultyDbId;
        this.subjectId = subjectId;
    }

    public String getFacultyId() {
        return facultyId;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public int getFacultyDbId() {
        return facultyDbId;
    }

    public int getSubjectId() {
        return subjectId;
    }
}
