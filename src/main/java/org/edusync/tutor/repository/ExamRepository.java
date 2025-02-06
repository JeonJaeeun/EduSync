package org.edusync.tutor.repository;

import org.edusync.tutor.entity.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExamRepository extends JpaRepository<Exam, Long> {
    List<Exam> findByLessonId(Long lessonId);
} 