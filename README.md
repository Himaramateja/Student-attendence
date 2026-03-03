# Student Attendance System - Web Version

This is the **Web Application** version of the Student Attendance System, built with **Spring Boot** and **Thymeleaf**, using the same **Core JDBC** logic for data access.

## Features
- **Web-Based UI**: Modern, responsive interface using Bootstrap.
- **Roles**: Admin, Faculty, Student portals.
- **Reporting**: Visual dashboards for attendance stats.

## Prerequisites
- **Java 17** or higher.
- **Maven** (bundled or installed).
- **MySQL Database** (use `setup_db.bat`).

## How to Run

### Method 1: Using IntelliJ IDEA (Recommended)
1.  Open IntelliJ IDEA.
2.  Select **File > Open** and choose the `studentattendence` folder.
3.  IntelliJ updates the dependencies from `pom.xml`.
4.  Navigate to `src/main/java/com/attendance/AttendanceApplication.java`.
5.  Right-click and select **Run 'AttendanceApplication'**.
6.  Open browser: `http://localhost:8080`.

### Method 2: Using Command Line (Maven)
1.  Ensure you have Maven installed.
2.  Run `mvn spring-boot:run`.
3.  Open browser: `http://localhost:8080`.

## Database Config
Update `src/main/java/com/attendance/util/DbConnection.java` if your MySQL username/password is different from default (`root` / empty).

## Login Credentials
- **Admin**: `admin` / `admin123`
- **Faculty**: `faculty1` / `faculty123`
- **Student**: `student1` / `student123`
