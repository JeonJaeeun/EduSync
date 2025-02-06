package org.edusync.tutor.repository;

import org.edusync.tutor.entity.ExamScore;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExamScoreRepository extends JpaRepository<ExamScore, Long> {
    List<ExamScore> findByExamId(Long examId);
} 