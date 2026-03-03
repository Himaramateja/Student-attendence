package com.attendance.model;

public class SubjectAttendanceDTO {
    private String subjectCode;
    private String subjectName;
    private int totalPeriods;
    private int attendedPeriods;
    private double percentage;

    public SubjectAttendanceDTO(String subjectCode, String subjectName, int totalPeriods, int attendedPeriods) {
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.totalPeriods = totalPeriods;
        this.attendedPeriods = attendedPeriods;
        this.percentage = totalPeriods == 0 ? 0 : (double) attendedPeriods / totalPeriods * 100;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public int getTotalPeriods() {
        return totalPeriods;
    }

    public int getAttendedPeriods() {
        return attendedPeriods;
    }

    public double getPercentage() {
        return percentage;
    }
}
