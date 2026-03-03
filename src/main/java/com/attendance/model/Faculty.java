package com.attendance.model;

public class Faculty {
    private int id; // Start from 100
    private int userId;
    private String facultyId;
    private String name;
    private String department;

    public Faculty() {
    }

    public Faculty(int id, int userId, String facultyId, String name, String department) {
        this.id = id;
        this.userId = userId;
        this.facultyId = facultyId;
        this.name = name;
        this.department = department;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(String facultyId) {
        this.facultyId = facultyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
