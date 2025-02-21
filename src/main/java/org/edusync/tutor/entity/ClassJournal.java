package org.edusync.tutor.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "class_journals")
public class ClassJournal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "class_id", nullable = false) // 외래 키 설정
    private Lesson classId; // 변경된 LessonClass를 참조하도록 수정

    @Column(nullable = false)
    private LocalDate lessonDate;

    @Column(length = 255)
    private String progress;

    @Lob
    private String journalContent;

    private Integer homeworkCompletion;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Lesson getClassId() {
        return classId;
    }

    public void setClassId(Lesson classId) {
        this.classId = classId;
    }

    public LocalDate getLessonDate() {
        return lessonDate;
    }

    public void setLessonDate(LocalDate lessonDate) {
        this.lessonDate = lessonDate;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getJournalContent() {
        return journalContent;
    }

    public void setJournalContent(String journalContent) {
        this.journalContent = journalContent;
    }

    public Integer getHomeworkCompletion() {
        return homeworkCompletion;
    }

    public void setHomeworkCompletion(Integer homeworkCompletion) {
        this.homeworkCompletion = homeworkCompletion;
    }
}