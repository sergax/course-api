package com.sergax.courseapi.service.iml;

import com.sergax.courseapi.dto.CourseDto;
import com.sergax.courseapi.model.course.CourseStatus;
import com.sergax.courseapi.model.user.User;
import com.sergax.courseapi.repository.CourseRepository;
import com.sergax.courseapi.service.CourseService;
import com.sergax.courseapi.service.UserService;
import com.sergax.courseapi.service.exception.InvalidMentorException;
import com.sergax.courseapi.service.exception.InvalidUrlException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseServiceIml implements CourseService {
    private final CourseRepository courseRepository;
    private final UserService userService;

    @Override
    public List<CourseDto> findAll() {
        return courseRepository.findAllBy().stream()
                .map(CourseDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseDto> findAllPublicCourses() {
        return courseRepository.findAllByStatusPublic();
    }

    @Override
    @Transactional
    public CourseDto findById(Long courseId) {
        return courseRepository.findById(courseId)
                .map(CourseDto::new)
                .orElseThrow(() -> new EntityNotFoundException(
                        format("Course by ID: %d not found", courseId)));
    }

    @Override
    @Transactional
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
        var mentor = userService.findUserByEmail(mentorEmail).toUser();
        courseDto.setStatus(CourseStatus.PRIVATE)
                .setDateStart(LocalDate.now());
        var course = courseDto.toCourse();
        course.getMentors().add(mentor);
        var savedCourse = courseRepository.save(course);

        log.info("IN createCourseByMentor: {} course created", new CourseDto(savedCourse));
        return new CourseDto(savedCourse);
    }

    @Override
    public CourseDto update(Long courseId, CourseDto courseDto) {
        var existingCourse = courseRepository.getById(courseId);
        isUrlValid(courseDto.getLogoUrl());
        isUrlValid(courseDto.getMovieUrl());
        existingCourse
                .setName(courseDto.getName())
                .setDescription(courseDto.getDescription())
                .setCourseStatus(courseDto.getStatus())
                .setDateStart(courseDto.getDateStart())
                .setDateEnd(courseDto.getDateEnd())
                .setLogoUrl(courseDto.getLogoUrl())
                .setMovieUrl(courseDto.getMovieUrl());
        return new CourseDto(existingCourse);
    }

    @Override
    @Transactional
    public CourseDto updateCourseByMentor(Long courseId, CourseDto courseDto, String mentorEmail) {
        var mentorId = userService.findUserByEmail(mentorEmail).getId();
        existMentorInCourse(courseId, mentorId);
        var updatedCourse = update(courseId, courseDto);

        log.info("IN updateCourseByMentor: {}", updatedCourse);
        return updatedCourse;
    }

    @Override
    public void deleteById(Long courseId) {
        var existingCourse = findById(courseId);
        existingCourse.setStatus(CourseStatus.DELETED);

        log.info("IN deleteById course: {}", existingCourse);
    }

    @Override
    public void existMentorInCourse(Long courseId, Long mentorId) {
        if (!courseRepository.existsCourseByMentorId(courseId, mentorId)) {
            throw new InvalidMentorException(
                    format("User ID: %d not a mentor on this course ID: %d", mentorId, courseId));
        }
    }

    private void isUrlValid(String url) {
        if (url != null) {
            try {
                new URL(url).toURI();
            } catch (Exception e) {
                throw new InvalidUrlException(
                        format("You url: %s isn't valid", url));
            }
        }
    }

}
