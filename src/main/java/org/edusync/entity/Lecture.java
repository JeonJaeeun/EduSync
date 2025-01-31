package org.edusync.entity;

import jakarta.persistence.*;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Entity
@Table(name = "lectures")
public class Lecture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 500)
    private String description;

    @ManyToOne
    @JoinColumn(name = "tutor_id", nullable = false)
    private User tutor;

    private String subject;

    @Column(name = "lesson_day")
    private String lessonDay;

    @Column(name = "lesson_start_time")
    private LocalTime lessonStartTime;

    @Column(name = "lesson_end_time")
    private LocalTime lessonEndTime;

    @Column(nullable = false)
    private Integer maxStudents;

    private Integer tuition;

    @Column(name = "tuition_cycle")
    private String tuitionCycle;

    @Column(nullable = false)
    private Boolean isActive = true;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getTutor() {
        return tutor;
    }

    public void setTutor(User tutor) {
        this.tutor = tutor;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getLessonDay() {
        return lessonDay;
    }

    public void setLessonDay(String lessonDay) {
        this.lessonDay = lessonDay;
    }

    public LocalTime getLessonStartTime() {
        return lessonStartTime;
    }

    public void setLessonStartTime(LocalTime lessonStartTime) {
        this.lessonStartTime = lessonStartTime;
    }

    public LocalTime getLessonEndTime() {
        return lessonEndTime;
    }

    public void setLessonEndTime(LocalTime lessonEndTime) {
        this.lessonEndTime = lessonEndTime;
    }

    public Integer getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(Integer maxStudents) {
        this.maxStudents = maxStudents;
    }

    public Integer getTuition() {
        return tuition;
    }

    public void setTuition(Integer tuition) {
        this.tuition = tuition;
    }

    public String getTuitionCycle() {
        return tuitionCycle;
    }

    public void setTuitionCycle(String tuitionCycle) {
        this.tuitionCycle = tuitionCycle;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setTeacher(User teacher) {
    }

    public void setStudent(User student) {
    }
}
