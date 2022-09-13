package com.sergax.courseapi.service.iml;

import com.sergax.courseapi.dto.ContentDto;
import com.sergax.courseapi.dto.CourseDto;
import com.sergax.courseapi.model.course.TypeContent;
import com.sergax.courseapi.repository.ContentRepository;
import com.sergax.courseapi.service.ContentService;
import com.sergax.courseapi.service.CourseService;
import com.sergax.courseapi.service.UserService;
import com.sergax.courseapi.service.exception.InvalidMentorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContentServiceIml implements ContentService {
    private final ContentRepository contentRepository;
    private final UserService userService;
    private final CourseService courseService;

    @Override
    public List<ContentDto> findAll() {
        return contentRepository.findAll().stream()
                .map(ContentDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public ContentDto findById(Long contentId) {
        return contentRepository.findById(contentId)
                .map(ContentDto::new)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public ContentDto save(ContentDto contentDto) {
        setContentType(contentDto);
        return new ContentDto(contentRepository.save(contentDto.toContent()));
    }

    @Override
    public ContentDto addContentToCourse(Long courseId, ContentDto contentDto, String mentorEmail) {
        var mentorDto = userService.findUserByEmail(mentorEmail);
        var courseDto = courseService.findById(courseId);
        if (!courseService.isMentorInCourse(mentorDto.getId(), courseId)) {
            throw new InvalidMentorException(
                    format("User: %s not a mentor on this course: %s", mentorDto.getEmail(), courseDto.getName()));
        }
        var savedContent = save(contentDto);
        courseDto.getContents().add(savedContent);

        log.info("IN addContentToCourse: {}", savedContent);
        return savedContent;
    }

    @Override
    public ContentDto update(Long contentId, ContentDto contentDto) {
        var existingContent = findById(contentId);
        existingContent.setName(contentDto.getName())
                .setText(contentDto.getText())
                .setMovie_url(contentDto.getMovie_url());
        setContentType(existingContent);

        log.info("IN update content: {}", existingContent);
        return existingContent;
    }

    @Override
    public void deleteById(Long contentId) {
        contentRepository.deleteById(contentId);
        log.info("IN deleteById content by ID: {}", contentId);
    }

    private void setContentType(ContentDto contentDto) {
        if (contentDto.getText() != null && contentDto.getMovie_url() != null) {
            contentDto.setTypeContent(TypeContent.MIXED);
        } else if (contentDto.getText() != null) {
            contentDto.setTypeContent(TypeContent.TEXT);
        } else if (contentDto.getMovie_url() != null) {
            contentDto.setTypeContent(TypeContent.VIDEO);
        } else {
            contentDto.setTypeContent(TypeContent.MIXED);
        }
    }

}
