-- Update Students table to include Section and Semester
ALTER TABLE students ADD COLUMN section INT DEFAULT 1;

ALTER TABLE students ADD COLUMN semester INT DEFAULT 1;