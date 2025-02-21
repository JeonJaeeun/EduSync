package org.edusync.tutor.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "exam_scores")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamScore extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id")
    private Exam exam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private User student;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false)
    private Integer score;

    private String grade;

    private Integer unit;

    @Version
    private Long version;
} 