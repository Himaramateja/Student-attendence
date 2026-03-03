package com.attendance.model;

import java.sql.Date;

public class Attendance {
    private int id;
    private int studentDbId; // Points to students.id (1000+)
    private int subjectId; // Points to subjects.id (1+)
    private Date attendanceDate;
    private int period;
    private String status;

    public Attendance() {
    }

    public Attendance(int id, int studentDbId, int subjectId, Date attendanceDate, int period, String status) {
        this.id = id;
        this.studentDbId = studentDbId;
        this.subjectId = subjectId;
        this.attendanceDate = attendanceDate;
        this.period = period;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudentDbId() {
        return studentDbId;
    }

    public void setStudentDbId(int studentDbId) {
        this.studentDbId = studentDbId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public Date getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(Date attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
