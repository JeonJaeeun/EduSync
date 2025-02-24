CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    school_name VARCHAR(255),
    role VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE password_reset_tokens (
                                       id SERIAL PRIMARY KEY,
                                       email VARCHAR(255) NOT NULL UNIQUE,
                                       verification_code VARCHAR(10) NOT NULL,
                                       created_at TIMESTAMP NOT NULL DEFAULT NOW(),
                                       expires_at TIMESTAMP NOT NULL
);

CREATE TABLE classes (
                         id SERIAL PRIMARY KEY,
                         teacher_id INT NOT NULL REFERENCES users(id),
                         student_id INT NOT NULL REFERENCES users(id),
                         subject VARCHAR(100) NOT NULL,
                         lesson_day VARCHAR(20) NOT NULL,
                         lesson_start_time TIME,
                         lesson_end_time TIME,
                         tuition INT,
                         tuition_cycle VARCHAR(20),
                         created_at TIMESTAMP NOT NULL DEFAULT NOW(),
                         updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

<<<<<<< HEAD
CREATE TABLE IF NOT EXISTS classes (
    id BIGSERIAL PRIMARY KEY,
    teacher_id BIGINT REFERENCES users(id),
    student_id BIGINT REFERENCES users(id),
    subject VARCHAR(255) NOT NULL,
    lesson_day VARCHAR(50),
    lesson_start_time TIME,
    lesson_end_time TIME,
    tuition INTEGER,
    tuition_cycle VARCHAR(20),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS class_journals (
    id BIGSERIAL PRIMARY KEY,
    class_id BIGINT REFERENCES classes(id),
    lesson_date DATE NOT NULL,
    progress TEXT,
    journal_content TEXT,
    homework_completion INTEGER,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
=======
CREATE TABLE class_journals (
                                id SERIAL PRIMARY KEY,
                                class_id INT NOT NULL REFERENCES classes(id),
                                lesson_date DATE NOT NULL,
                                progress VARCHAR(255),
                                journal_content TEXT,
                                homework_completion INT,
                                created_at TIMESTAMP NOT NULL DEFAULT NOW(),
                                updated_at TIMESTAMP NOT NULL DEFAULT NOW()
>>>>>>> main
);
