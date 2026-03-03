package com.attendance.model;

public class StudentAttendanceReportDTO {
    private String studentRoll;
    private String studentName;
    private int totalPeriods;
    private int attendedPeriods;
    private double percentage;

    public StudentAttendanceReportDTO(String studentRoll, String studentName, int totalPeriods, int attendedPeriods) {
        this.studentRoll = studentRoll;
        this.studentName = studentName;
        this.totalPeriods = totalPeriods;
        this.attendedPeriods = attendedPeriods;
        this.percentage = totalPeriods == 0 ? 0 : (double) attendedPeriods / totalPeriods * 100;
    }

    public String getStudentRoll() {
        return studentRoll;
    }

    public String getStudentName() {
        return studentName;
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
