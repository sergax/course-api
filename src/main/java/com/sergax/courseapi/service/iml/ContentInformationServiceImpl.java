package com.sergax.courseapi.service.iml;

import com.sergax.courseapi.dto.ContentInformationDto;
import com.sergax.courseapi.dto.StudentProgressDto;
import com.sergax.courseapi.repository.ContentInformationRepository;
import com.sergax.courseapi.repository.ContentRepository;
import com.sergax.courseapi.repository.CourseRepository;
import com.sergax.courseapi.repository.UserRepository;
import com.sergax.courseapi.service.ContentInformationService;
import com.sergax.courseapi.service.exception.NotUniqueDataException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContentInformationServiceImpl implements ContentInformationService {
    private final ContentInformationRepository contentInformationRepository;
    private final CourseRepository courseRepository;
    private final ContentRepository contentRepository;
    private final UserRepository userRepository;

    @Override
    public Integer findProgressByCourseIdAndStudentId(Long courseId, Long studentId) {
        return contentInformationRepository.findProgressStudentByStudentIdAndCourseId(courseId, studentId);
    }

    @Override
    public ContentInformationDto passedContentByStudent(Long contentId, ContentInformationDto contentInformationDto, String studentEmail) {
        var content = contentRepository.getById(contentId);
        var user = userRepository.findByEmail(studentEmail).orElseThrow(EntityNotFoundException::new);
        if (courseRepository.existsCourseByMentorId(content.getCourse().getId(), user.getId())) {
            throw new NotUniqueDataException(
                    format("Mentor by ID: %d is creator of course ID: %d", user.getId(), content.getCourse().getId()));
        }
        var contentInformation = contentInformationDto.toContentInformation()
                .setStudent(user)
                .setContent(content)
                .setPassed(Boolean.TRUE);
        var savedContentInformation = contentInformationRepository.save(contentInformation);

        log.info("IN passedContentByStudent: {}", new ContentInformationDto(savedContentInformation));
        return new ContentInformationDto(savedContentInformation);
    }
}
