package com.attendance.model;

import java.sql.Date;

public class Report {
    private int id;
    private Date reportDate;
    private int totalStudents;
    private int totalPresent;
    private double averageAttendance;
    private String description;

    public Report() {
    }

    public Report(int id, Date reportDate, int totalStudents, int totalPresent, double averageAttendance,
            String description) {
        this.id = id;
        this.reportDate = reportDate;
        this.totalStudents = totalStudents;
        this.totalPresent = totalPresent;
        this.averageAttendance = averageAttendance;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getReportDate() {
        return reportDate;
    }

    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }

    public int getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(int totalStudents) {
        this.totalStudents = totalStudents;
    }

    public int getTotalPresent() {
        return totalPresent;
    }

    public void setTotalPresent(int totalPresent) {
        this.totalPresent = totalPresent;
    }

    public double getAverageAttendance() {
        return averageAttendance;
    }

    public void setAverageAttendance(double averageAttendance) {
        this.averageAttendance = averageAttendance;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
