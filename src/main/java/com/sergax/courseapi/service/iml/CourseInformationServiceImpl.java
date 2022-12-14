package com.sergax.courseapi.service.iml;

import com.sergax.courseapi.dto.CourseInformationDto;
import com.sergax.courseapi.repository.CourseInformationRepository;
import com.sergax.courseapi.repository.CourseRepository;
import com.sergax.courseapi.repository.UserRepository;
import com.sergax.courseapi.service.CourseInformationService;
import com.sergax.courseapi.service.exception.NotUniqueDataException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseInformationServiceImpl implements CourseInformationService {
    private final CourseInformationRepository courseInformationRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    @Override
    public Integer findAmountOfLikesByCourseId(Long courseId) {
        return courseInformationRepository.findAmountOfLikesByCourseId(courseId);
    }

    @Override
    @Transactional
    public CourseInformationDto addStudentToCourse(
            Long courseId,
            String studentEmail) {
        var user = userRepository.findByEmail(studentEmail)
                .orElseThrow(EntityNotFoundException::new);
        var course = courseRepository.getById(courseId);
        if (courseRepository.existsCourseByMentorId(courseId, user.getId())) {
            throw new NotUniqueDataException(
                    format("Mentor by ID: %d is creator of course ID: %d", user.getId(), courseId));
        }
        if (courseRepository.existsCourseByStudentId(courseId, user.getId())) {
            throw new NotUniqueDataException(
                    format("Student by ID: %d already in course ID: %d", user.getId(), courseId));
        }

        var courseInformation = new CourseInformationDto().toCourseInformation()
                .setCourse(course)
                .setStudent(user)
                .setDateRegistered(LocalDate.now());
        var savedCourseInformation = courseInformationRepository.save(courseInformation);

        log.info("IN addStudentToCourse: {}", new CourseInformationDto(savedCourseInformation));
        return new CourseInformationDto(savedCourseInformation);

    }

    @Override
    @Transactional
    public CourseInformationDto addLikesToCourseByStudent(
            Long courseId,
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
                .setLikes(Boolean.TRUE);

        log.info("IN addLikesAndCommentsToCourseByStudent: {}",
                new CourseInformationDto(courseInformationDtoByCourseIdAndStudentId));
        return new CourseInformationDto(courseInformationDtoByCourseIdAndStudentId);
    }

}
