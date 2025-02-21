package org.edusync.tutor.repository;

import org.edusync.tutor.entity.ExamScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ExamScoreRepository extends JpaRepository<ExamScore, Long> {
    List<ExamScore> findByExamId(Long examId);
} 