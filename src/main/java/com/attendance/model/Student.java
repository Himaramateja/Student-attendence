package com.attendance.model;

public class Student {
    private int id; // Start from 1000
    private int userId;
    private String studentRoll;
    private String name;
    private String department;
    private int section;
    private int semester;

    public Student() {
    }

    public Student(int id, int userId, String studentRoll, String name, String department, int section, int semester) {
        this.id = id;
        this.userId = userId;
        this.studentRoll = studentRoll;
        this.name = name;
        this.department = department;
        this.section = section;
        this.semester = semester;
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

    public String getStudentRoll() {
        return studentRoll;
    }

    public void setStudentRoll(String studentRoll) {
        this.studentRoll = studentRoll;
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

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }
}
