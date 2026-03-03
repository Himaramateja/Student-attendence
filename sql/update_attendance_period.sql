USE student_attendance_db;

ALTER TABLE attendance ADD COLUMN period INT DEFAULT 1;

-- Drop the old unique key and add a new one including period
ALTER TABLE attendance DROP INDEX student_db_id;

ALTER TABLE attendance
ADD UNIQUE KEY unique_attendance (
    student_db_id,
    subject_id,
    attendance_date,
    period
);