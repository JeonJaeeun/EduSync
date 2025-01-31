package org.edusync.repository;

import org.edusync.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
    // 필요한 추가 쿼리 메서드
}