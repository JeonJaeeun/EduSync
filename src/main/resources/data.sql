-- Sample users -- ChatGPT로 생성해둠.
INSERT INTO users (email, password, nickname, phone_number, school_name, user_type, created_at, updated_at)
VALUES
    ('student1@example.com', 'hashed_password1', 'Student1', '01012345678', 'High School A', 'STUDENT', NOW(), NOW()),
    ('parent1@example.com', 'hashed_password2', 'Parent1', '01087654321', 'High School A', 'PARENT', NOW(), NOW()),
    ('teacher1@example.com', 'hashed_password3', 'Teacher1', '01055556666', 'High School A', 'TEACHER', NOW(), NOW());

-- Sample classes
INSERT INTO classes (teacher_id, student_id, subject, lesson_day, lesson_start_time, lesson_end_time, tuition, tuition_cycle, created_at, updated_at)
VALUES
    (3, 1, 'Math', 'MON,WED', '15:00', '17:00', 300000, 'monthly', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Sample class journals
INSERT INTO class_journals (class_id, lesson_date, progress, journal_content, homework_completion, created_at, updated_at)
VALUES
    (1, '2025-01-10', 'Chapter 1-3', 'Focused on algebra basics', 80, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);