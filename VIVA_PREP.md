# Student Attendance System - Viva Voce Questions & Answers

## 1. Project Overview
**Q: What is the purpose of this project?**
A: To automate student attendance tracking, replacing manual registers with a digital system that supports Admin, Faculty, and Student roles using Java and MySQL.

**Q: What technologies did you use?**
A: 
- **Language**: Core Java (JDK 17+)
- **Database**: MySQL (Relational DB)
- **Connectivity**: JDBC (Java Database Connectivity)
- **Architecture**: Modular (MVC Pattern - Model, View, Controller/Service)
- **Build Tool**: Custom batch scripts / Maven (for web version)

## 2. Java Concepts Used
**Q: What is JDBC?**
A: JDBC (Java Database Connectivity) is an API that allows Java applications to interact with databases. We used it to execute SQL queries (SELECT, INSERT, UPDATE, DELETE).

**Q: Why did you use `PreparedStatement` instead of `Statement`?**
A: `PreparedStatement` is pre-compiled and faster. More importantly, it prevents **SQL Injection attacks** by treating user input as data, not executable code.

**Q: Explain the Project Structure (DAO vs Service).**
A: 
- **Model**: POJO classes (e.g., `Student`, `User`) representing database tables.
- **DAO (Data Access Object)**: Handle direct DB operations (SQL queries).
- **Service**: Business logic (e.g., verifying login, calculating attendance percentage).
- **UI**: The console interface for user interaction.
*Benefits*: Separation of concerns makes code easier to maintain and test.

**Q: What involves OOP concepts in your project?**
A: 
- **Encapsulation**: Private fields in Model classes with Getters/Setters.
- **Abstraction**: DAO classes hide complex SQL logic from the main UI.
- **Inheritance**: (If applicable) Potential use in User roles or specific extensions.

## 3. Database Concepts
**Q: What is the relationship between Faculty and Subjects?**
A: It is a **Many-to-Many** relationship (A faculty can teach multiple subjects, and a subject can be taught by multiple faculty). We resolved this using a junction table `faculty_subjects`.

**Q: What is a Foreign Key? Where did you use it?**
A: A Foreign Key links two tables. For example, `student_id` in the `attendance` table points to the `id` in the `students` table, ensuring we only mark attendance for valid students.

**Q: How do you handle duplicate attendance?**
A: We used a **UNIQUE Constraint** on `(student_id, subject_id, attendance_date)` in the database. In Java, we use `ON DUPLICATE KEY UPDATE` to update the status if attendance is marked again for the same day.

## 4. Troubleshooting & Logic
**Q: How do you calculate attendance percentage?**
A: `(Total Present Days / Total Class Days) * 100`. This logic resides in the `StudentService` class.

**Q: What happens if the database connection fails?**
A: The `DbConnection` class catches `SQLException`, prints an error message, and the application gracefully informs the user instead of crashing.

**Q: How did you implement Security?**
A: 
- **Passwords**: Stored in the database (hashed in real-world, plain text for this demo).
- **Role-Based Access**: The UI checks the `role` enum (ADMIN, FACULTY, STUDENT) to show different menus.
