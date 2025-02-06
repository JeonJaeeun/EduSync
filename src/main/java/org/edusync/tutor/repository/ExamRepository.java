package org.edusync.tutor.repository;

import org.edusync.tutor.entity.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
    List<Exam> findByLesson_Id(Long lessonId);
} 