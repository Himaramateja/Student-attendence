-- Timetable Table for storing class schedules per department/section/semester
CREATE TABLE IF NOT EXISTS timetable (
    id INT AUTO_INCREMENT PRIMARY KEY,
    department VARCHAR(50) NOT NULL,
    section INT NOT NULL,
    semester INT NOT NULL,
    day_of_week VARCHAR(10) NOT NULL,
    period INT NOT NULL,
    subject_code VARCHAR(20),
    UNIQUE KEY (department, section, semester, day_of_week, period),
    FOREIGN KEY (subject_code) REFERENCES subjects(subject_code) ON DELETE SET NULL
);
