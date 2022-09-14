package com.sergax.courseapi.service.iml;

import com.sergax.courseapi.dto.CourseDto;
import com.sergax.courseapi.dto.UserDto;
import com.sergax.courseapi.model.course.Course;
import com.sergax.courseapi.model.course.CourseStatus;
import com.sergax.courseapi.model.user.User;
import com.sergax.courseapi.repository.CourseRepository;
import com.sergax.courseapi.service.CourseService;
import com.sergax.courseapi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseServiceIml implements CourseService {
    private final CourseRepository courseRepository;
    private final UserService userService;

    @Override
    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    @Override
    public Course findById(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Course save(Course course) {
        course.setCourseStatus(CourseStatus.PRIVATE)
                .setDateStart(LocalDate.now());
        var savedCourse = courseRepository.save(course);

        log.info("IN save course: {}", new CourseDto(savedCourse));
        return savedCourse;
    }

    @Override
    @Transactional
    public CourseDto createCourseByMentor(Course course, String mentorEmail) {
        var mentors = new ArrayList<User>();
        var mentor = userService.findUserByEmail(mentorEmail);
        mentors.add(mentor);
        course.setMentors(mentors);
        var savedCourse = save(course);

        log.info("IN createCourseByMentor: <{}> course created", new CourseDto(savedCourse));
        return new CourseDto(savedCourse);
    }

    @Override
    @Transactional
    public Course update(Long courseId, Course course) {
        var existingCourse = findById(courseId);
        existingCourse
                .setName(course.getName())
                .setDescription(course.getDescription())
                .setDateStart(course.getDateStart())
                .setDateEnd(course.getDateEnd())
                .setLogoUrl(course.getLogoUrl())
                .setMovieUrl(course.getMovieUrl());

        log.info("IN update course: <{}>", new CourseDto(existingCourse));
        return existingCourse;
    }

    @Override
    public void deleteById(Long courseId) {
        var existingCourse = findById(courseId);
        existingCourse.setCourseStatus(CourseStatus.DELETED);

        log.info("IN deleteById course: <{}>", new CourseDto(existingCourse));
    }

    @Override
    public boolean existMentorInCourse(Long courseId, Long mentorId) {
        return courseRepository.existsCourseByMentorId(mentorId, courseId);
    }

}
