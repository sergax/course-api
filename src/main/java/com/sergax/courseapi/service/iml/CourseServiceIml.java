package com.sergax.courseapi.service.iml;

import com.sergax.courseapi.dto.ContentDto;
import com.sergax.courseapi.dto.CourseDto;
import com.sergax.courseapi.model.Status;
import com.sergax.courseapi.model.course.Course;
import com.sergax.courseapi.repository.CourseRepository;
import com.sergax.courseapi.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseServiceIml implements CourseService {
    private final CourseRepository courseRepository;
    private final UserServiceIml userService;

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
        courseDto.setStatus(Status.ACTIVE)
                .setDateStart(LocalDate.now());
        var course = toCourse(courseDto);
        var savedCourse = courseRepository.save(course);

        log.info("IN save course: {}", savedCourse.getId());
        return new CourseDto(savedCourse);
    }

    @Override
    public CourseDto createCourseByMentor(CourseDto courseDto, String mentorEmail) {
        var mentorDto = userService.findUserByEmail(mentorEmail);
        var savedCourse = save(courseDto);
        savedCourse.getMentors().add(mentorDto);

        log.info("IN createCourseByMentor: <{}> course created", savedCourse);
        return savedCourse;
    }

    @Override
    public CourseDto update(Long courseId, CourseDto courseDto) {
        CourseDto existingCourseDto = findById(courseId);
        existingCourseDto
                .setName(courseDto.getName())
                .setDescription(courseDto.getDescription())
                .setDateStart(courseDto.getDateStart())
                .setDateEnd(courseDto.getDateEnd())
                .setLogoUrl(courseDto.getLogoUrl())
                .setMovie_url(courseDto.getMovie_url());

        log.info("IN update course: <{}>", existingCourseDto);
        return existingCourseDto;
    }

    @Override
    public void deleteById(Long courseId) {
        CourseDto existingCourseDto = findById(courseId);
        existingCourseDto.setStatus(Status.DELETED);

        log.info("IN deleteById course: <{}>", existingCourseDto);
    }

    @Override
    public boolean isMentorInCourse(Long courseId, Long mentorId) {
        return courseRepository.existsMentorInCourse(mentorId, courseId);
    }

    public Course toCourse(CourseDto courseDto) {
        return new Course()
                .setId(courseDto.getId())
                .setName(courseDto.getName())
                .setDescription(courseDto.getDescription())
                .setLogoUrl(courseDto.getLogoUrl())
                .setMovie_url(courseDto.getMovie_url())
                .setDateStart(courseDto.getDateStart())
                .setDateEnd(courseDto.getDateEnd())
                .setStatus(courseDto.getStatus())
                .setMentors(courseDto.getMentors().stream()
                        .map(userService::toUser)
                        .collect(Collectors.toList()))
                .setContents(courseDto.getContents().stream()
                        .map(ContentDto::toContent)
                        .collect(Collectors.toList()));
    }
}
