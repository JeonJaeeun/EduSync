package org.edusync.service;

import org.edusync.dto.LectureRequest;
import org.edusync.entity.Lecture;
import org.edusync.entity.User;
import org.edusync.exception.LectureNotFoundException;
import org.edusync.repository.LectureRepository;
import org.edusync.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class LectureService {
    
    private final LectureRepository lectureRepository;
    private final UserRepository userRepository;

    public LectureService(LectureRepository lectureRepository, UserRepository userRepository) {
        this.lectureRepository = lectureRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Lecture createLecture(LectureRequest request) {
        User teacher = getCurrentUser();
        User student = userRepository.findById(request.getStudentId())
            .orElseThrow(() -> new RuntimeException("Student not found"));

        Lecture lecture = new Lecture();
        lecture.setTeacher(teacher);
        lecture.setStudent(student);
        lecture.setSubject(request.getSubject());
        lecture.setLessonDay(request.getLessonDay());
        lecture.setLessonStartTime(LocalTime.parse(request.getLessonStartTime()));
        lecture.setLessonEndTime(LocalTime.parse(request.getLessonEndTime()));
        lecture.setTuition(request.getTuition());
        lecture.setTuitionCycle(request.getTuitionCycle());

        return lectureRepository.save(lecture);
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Current user not found"));
    }

    public List<Lecture> getAllLectures() {
        return lectureRepository.findAll();
    }

    public Lecture getLecture(Long id) {
        return lectureRepository.findById(id)
            .orElseThrow(() -> new LectureNotFoundException(id));
    }

    @Transactional
    public Lecture updateLecture(Long id, LectureRequest request) {
        Lecture lecture = getLecture(id);
        User student = userRepository.findById(request.getStudentId())
            .orElseThrow(() -> new RuntimeException("Student not found"));
        
        lecture.setStudent(student);
        lecture.setSubject(request.getSubject());
        lecture.setLessonDay(request.getLessonDay());
        lecture.setLessonStartTime(LocalTime.parse(request.getLessonStartTime()));
        lecture.setLessonEndTime(LocalTime.parse(request.getLessonEndTime()));
        lecture.setTuition(request.getTuition());
        lecture.setTuitionCycle(request.getTuitionCycle());
        
        return lectureRepository.save(lecture);
    }

    @Transactional
    public void deleteLecture(Long id) {
        if (!lectureRepository.existsById(id)) {
            throw new LectureNotFoundException(id);
        }
        lectureRepository.deleteById(id);
    }
}