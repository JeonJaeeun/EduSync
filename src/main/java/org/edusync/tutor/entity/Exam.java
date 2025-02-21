package org.edusync.tutor.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "exams")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Exam extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id")
    private User createdBy;
    
    @Column(nullable = false)
    private String examType;
    
    @Column(nullable = false)
    private String examName;
    
    @Column(nullable = false)
    private LocalDate examDate;
    
    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExamScore> scores = new ArrayList<>();
    
    public Long getId() {
        return id;
    }
    
    public Lesson getLesson() {
        return lesson;
    }
} 