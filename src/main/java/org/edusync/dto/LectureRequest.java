package org.edusync.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LectureRequest {
    private Long studentId;
    private String subject;
    private String lessonDay;
    private String lessonStartTime;
    private String lessonEndTime;
    private Integer tuition;
    private String tuitionCycle;

    // Getters
    public Long getStudentId() { return studentId; }
    public String getSubject() { return subject; }
    public String getLessonDay() { return lessonDay; }
    public String getLessonStartTime() { return lessonStartTime; }
    public String getLessonEndTime() { return lessonEndTime; }
    public Integer getTuition() { return tuition; }
    public String getTuitionCycle() { return tuitionCycle; }

    // Setters
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public void setSubject(String subject) { this.subject = subject; }
    public void setLessonDay(String lessonDay) { this.lessonDay = lessonDay; }
    public void setLessonStartTime(String lessonStartTime) { this.lessonStartTime = lessonStartTime; }
    public void setLessonEndTime(String lessonEndTime) { this.lessonEndTime = lessonEndTime; }
    public void setTuition(Integer tuition) { this.tuition = tuition; }
    public void setTuitionCycle(String tuitionCycle) { this.tuitionCycle = tuitionCycle; }
}