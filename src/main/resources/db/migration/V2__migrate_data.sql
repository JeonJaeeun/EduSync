INSERT INTO users (email, password, name, phone, school_name, role, created_at, updated_at)
SELECT 'admin@edusync.org', 'encoded_password', 'Admin', '01012345678', 'EduSync', 'ADMIN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE email = 'admin@edusync.org'
); 