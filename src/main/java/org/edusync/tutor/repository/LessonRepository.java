package org.edusync.tutor.repository;

import org.edusync.tutor.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByTeacherId(Long teacherId);
    List<Lesson> findByStudentId(Long studentId);
} 