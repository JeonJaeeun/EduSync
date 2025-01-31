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
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private User teacher;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private User student;
    
    private String subject;
    
    @Column(name = "lesson_day")
    private String lessonDay;
    
    @Column(name = "lesson_start_time")
    private LocalTime lessonStartTime;
    
    @Column(name = "lesson_end_time")
    private LocalTime lessonEndTime;
    
    private Integer tuition;
    
    @Column(name = "tuition_cycle")
    private String tuitionCycle;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
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

    // Getters
    public Long getId() { return id; }
    public User getTeacher() { return teacher; }
    public User getStudent() { return student; }
    public String getSubject() { return subject; }
    public String getLessonDay() { return lessonDay; }
    public LocalTime getLessonStartTime() { return lessonStartTime; }
    public LocalTime getLessonEndTime() { return lessonEndTime; }
    public Integer getTuition() { return tuition; }
    public String getTuitionCycle() { return tuitionCycle; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setTeacher(User teacher) { this.teacher = teacher; }
    public void setStudent(User student) { this.student = student; }
    public void setSubject(String subject) { this.subject = subject; }
    public void setLessonDay(String lessonDay) { this.lessonDay = lessonDay; }
    public void setLessonStartTime(LocalTime lessonStartTime) { this.lessonStartTime = lessonStartTime; }
    public void setLessonEndTime(LocalTime lessonEndTime) { this.lessonEndTime = lessonEndTime; }
    public void setTuition(Integer tuition) { this.tuition = tuition; }
    public void setTuitionCycle(String tuitionCycle) { this.tuitionCycle = tuitionCycle; }
}
