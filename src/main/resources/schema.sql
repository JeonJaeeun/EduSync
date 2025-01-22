CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       nickname VARCHAR(50),
                       phone_number VARCHAR(15),
                       school_name VARCHAR(255),
                       user_type VARCHAR(10) NOT NULL,
                       social_provider VARCHAR(20),
                       social_id VARCHAR(255),
                       birth_date DATE,
                       created_at TIMESTAMP NOT NULL DEFAULT NOW(),
                       updated_at TIMESTAMP NOT NULL DEFAULT NOW()
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

CREATE TABLE class_journals (
                                id SERIAL PRIMARY KEY,
                                class_id INT NOT NULL REFERENCES classes(id),
                                lesson_date DATE NOT NULL,
                                progress VARCHAR(255),
                                journal_content TEXT,
                                homework_completion INT,
                                created_at TIMESTAMP NOT NULL DEFAULT NOW(),
                                updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);
