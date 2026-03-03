-- Student Table
CREATE TABLE students (
    student_id INT PRIMARY KEY,
    student_name VARCHAR(50) NOT NULL,
    department VARCHAR(50),
    section INT,
    semester INT
);

-- Admin Table
CREATE TABLE admin (
    admin_id INT PRIMARY KEY,
    admin_name VARCHAR(50) NOT NULL,
    email VARCHAR(50) UNIQUE,
    password VARCHAR(50) NOT NULL
);

-- Faculty Table
CREATE TABLE faculty (
    faculty_id INT PRIMARY KEY,
    faculty_name VARCHAR(50) NOT NULL,
    department VARCHAR(50),
    designation VARCHAR(50),
    email VARCHAR(50) UNIQUE
);