package org.edusync.tutor.repository;

import org.edusync.tutor.entity.ClassJournal;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ClassJournalRepository extends JpaRepository<ClassJournal, Long> {
    List<ClassJournal> findByLessonId(Long lessonId);
} 