package org.edusync.tutor.repository;

import org.edusync.tutor.entity.ClassJournal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.time.LocalDate;

@Repository
public interface ClassJournalRepository extends JpaRepository<ClassJournal, Long> {
    List<ClassJournal> findByLessonId(Long lessonId);
    List<ClassJournal> findByLessonIdAndLessonDateBetween(Long lessonId, LocalDate startDate, LocalDate endDate);
    boolean existsByLessonIdAndLessonDate(Long lessonId, LocalDate lessonDate);
} 