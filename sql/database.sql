-- Student Attendance Management System Database Schema (Numeric IDs with Specific Start Values)
CREATE DATABASE IF NOT EXISTS student_attendance_db;

USE student_attendance_db;

-- 1. Users Table (for Authentication)
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(50) NOT NULL,
    role ENUM('ADMIN', 'FACULTY', 'STUDENT') NOT NULL
);

-- 2. Subjects Table
CREATE TABLE IF NOT EXISTS subjects (
    id INT AUTO_INCREMENT PRIMARY KEY,
    subject_code VARCHAR(20) UNIQUE NOT NULL,
    subject_name VARCHAR(100) NOT NULL
) AUTO_INCREMENT = 1;

-- 3. Students Table
CREATE TABLE IF NOT EXISTS students (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT UNIQUE NOT NULL,
    student_roll VARCHAR(20) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    department VARCHAR(50),
    section INT DEFAULT 1,
    semester INT DEFAULT 1,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
) AUTO_INCREMENT = 1000;

-- 4. Faculty Table
CREATE TABLE IF NOT EXISTS faculty (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT UNIQUE NOT NULL,
    faculty_id VARCHAR(20) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    department VARCHAR(50),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
) AUTO_INCREMENT = 100;

-- 5. Faculty_Subjects (Assignment)
CREATE TABLE IF NOT EXISTS faculty_subjects (
    faculty_db_id INT,
    subject_id INT,
    PRIMARY KEY (faculty_db_id, subject_id),
    FOREIGN KEY (faculty_db_id) REFERENCES faculty (id) ON DELETE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES subjects (id) ON DELETE CASCADE
);

-- 6. Attendance Table
CREATE TABLE IF NOT EXISTS attendance (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_db_id INT,
    subject_id INT,
    attendance_date DATE NOT NULL,
    status ENUM('PRESENT', 'ABSENT') NOT NULL,
    UNIQUE KEY (
        student_db_id,
        subject_id,
        attendance_date
    ),
    FOREIGN KEY (student_db_id) REFERENCES students (id) ON DELETE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES subjects (id) ON DELETE CASCADE
);

-- 7. Reports Table
CREATE TABLE IF NOT EXISTS reports (
    id INT AUTO_INCREMENT PRIMARY KEY,
    report_date DATE NOT NULL,
    total_students INT,
    total_present INT,
    average_attendance DECIMAL(5, 2),
    description TEXT
);

-- Seed Admin
INSERT INTO
    users (username, password, role)
VALUES ('admin', 'admin123', 'ADMIN');