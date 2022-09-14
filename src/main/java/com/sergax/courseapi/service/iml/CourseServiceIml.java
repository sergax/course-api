package com.sergax.courseapi.service.iml;

import com.sergax.courseapi.dto.CourseDto;
import com.sergax.courseapi.dto.MentorDto;
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
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseServiceIml implements CourseService {
    private final CourseRepository courseRepository;
    private final UserService userService;

    @Override
    public List<CourseDto> findAll() {
        return courseRepository.findAll().stream()
                .map(CourseDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public CourseDto findById(Long courseId) {
        return courseRepository.findById(courseId)
                .map(CourseDto::new)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public CourseDto save(CourseDto courseDto) {
        courseDto.setStatus(CourseStatus.PRIVATE)
                .setDateStart(LocalDate.now());
        var course = courseDto.toCourse();
        var savedCourse = courseRepository.save(course);

        log.info("IN save course: {}", new CourseDto(savedCourse));
        return new CourseDto(savedCourse);
    }

    @Override
    @Transactional
    public CourseDto createCourseByMentor(CourseDto courseDto, String mentorEmail) {
        var mentors = new ArrayList<User>();
        var mentor = userService.findUserByEmail(mentorEmail).toUser();
        mentors.add(mentor);
        courseDto.setStatus(CourseStatus.PRIVATE)
                .setDateStart(LocalDate.now());
        var course = courseDto.toCourse();
        course.setMentors(mentors);
        var savedCourse = courseRepository.save(course);

        log.info("IN createCourseByMentor: {} course created", new CourseDto(savedCourse));
        return new CourseDto(savedCourse);
    }

    @Override
    @Transactional
    public CourseDto update(Long courseId, CourseDto courseDto) {
        var existingCourse = findById(courseId);
        existingCourse
                .setName(courseDto.getName())
                .setDescription(courseDto.getDescription())
                .setDateStart(courseDto.getDateStart())
                .setDateEnd(courseDto.getDateEnd())
                .setLogoUrl(courseDto.getLogoUrl())
                .setMovieUrl(courseDto.getMovieUrl());

        log.info("IN update course: <{}>", existingCourse);
        return existingCourse;
    }

    @Override
    public void deleteById(Long courseId) {
        var existingCourse = findById(courseId);
        existingCourse.setStatus(CourseStatus.DELETED);

        log.info("IN deleteById course: <{}>", existingCourse);
    }

    @Override
    public boolean existMentorInCourse(Long courseId, Long mentorId) {
        return courseRepository.existsCourseByMentorId(mentorId, courseId);
    }

}
