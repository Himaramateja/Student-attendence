-- Add Reports Table (Optional requirement)
CREATE TABLE IF NOT EXISTS reports (
    id INT AUTO_INCREMENT PRIMARY KEY,
    report_date DATE NOT NULL,
    total_students INT,
    total_present INT,
    average_attendance DECIMAL(5, 2),
    description TEXT
);