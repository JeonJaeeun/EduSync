package org.edusync.tutor.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "class_journals")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassJournal extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @Column(nullable = false)
    private LocalDate lessonDate;

    private String progress;

    @Column(name = "journal_content")
    @Lob
    private String journalContent;

    @Column(name = "homework_completion")
    private Integer homeworkCompletion;
}