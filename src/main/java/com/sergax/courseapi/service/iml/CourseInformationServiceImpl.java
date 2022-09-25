package com.sergax.courseapi.service.iml;

import com.sergax.courseapi.dto.CourseInformationDto;
import com.sergax.courseapi.model.course.CourseInformation;
import com.sergax.courseapi.repository.CourseInformationRepository;
import com.sergax.courseapi.repository.CourseRepository;
import com.sergax.courseapi.repository.UserRepository;
import com.sergax.courseapi.service.CourseInformationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseInformationServiceImpl implements CourseInformationService {
    private final CourseInformationRepository courseInformationRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public CourseInformationDto addStudentToCourse(
            Long courseId,
            CourseInformationDto courseInformationDto,
            String studentEmail) {
        var student = userRepository.findByEmail(studentEmail)
                .orElseThrow(EntityNotFoundException::new);
        var courseInfo = new ArrayList<CourseInformation>();
        var course = courseRepository.getById(courseId);
        if (!courseRepository.existsCourseByMentorId(courseId, student.getId())) {
            courseInformationDto
                    .setCourseId(courseId)
                    .setStudentId(student.getId())
                    .setDateRegistered(LocalDate.now());

        }
        var courseInformation = courseInformationDto.toCourseInformation();
        courseInformation.setCourse(course).setStudent(student);
        var savedCourseInformation = courseInformationRepository.save(courseInformation);

        log.info("IN addStudentToCourse: {}", new CourseInformationDto(savedCourseInformation));
        return new CourseInformationDto(savedCourseInformation);
    }

    @Override
    @Transactional
    public CourseInformationDto addLikesAndCommentsToCourseByStudent(
            Long courseId,
            CourseInformationDto courseInformationDto,
            String studentEmail) {
        var student = userRepository.findByEmail(studentEmail)
                .orElseThrow(EntityNotFoundException::new);
        var courseInformationDtoByCourseIdAndStudentId =
                courseInformationRepository
                        .findCourseInformationByCourseIdAndStudentId(courseId, student.getId())
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        format("Student by ID: %d not in course ID: %d", student.getId(), courseId)));
        courseInformationDtoByCourseIdAndStudentId
                .setComments(courseInformationDto.getComments())
                .setLikes(courseInformationDto.isLikes());

        log.info("IN addLikesAndCommentsToCourseByStudent: {}",
                new CourseInformationDto(courseInformationDtoByCourseIdAndStudentId));
        return new CourseInformationDto(courseInformationDtoByCourseIdAndStudentId);
    }

}
