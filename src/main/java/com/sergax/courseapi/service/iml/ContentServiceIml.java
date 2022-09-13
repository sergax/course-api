package com.sergax.courseapi.service.iml;

import com.sergax.courseapi.dto.ContentDto;
import com.sergax.courseapi.model.course.Content;
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
import javax.transaction.Transactional;
import java.util.List;

import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContentServiceIml implements ContentService {
    private final ContentRepository contentRepository;
    private final UserService userService;
    private final CourseService courseService;

    @Override
    public List<Content> findAll() {
        return contentRepository.findAll();
    }

    @Override
    public Content findById(Long contentId) {
        return contentRepository.findById(contentId)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Content save(Content content) {
        setContentType(content);
        return contentRepository.save(content);
    }

    @Override
    @Transactional
    public ContentDto addContentToCourse(Long courseId, ContentDto contentDto, String mentorEmail) {
        var mentorDto = userService.findUserByEmail(mentorEmail);
        var course = courseService.findById(courseId);
        if (!courseService.isMentorInCourse(courseId, mentorDto.getId())) {
            throw new InvalidMentorException(
                    format("User: %s not a mentor on this course: %s", mentorDto.getEmail(), course.getName()));
        }
        Content content = contentDto.toContent().setCourse(course);
        Content save = save(content);

        log.info("IN addContentToCourse: {}", new ContentDto(save));
        return new ContentDto(save);
    }

    @Override
    public Content update(Long contentId, Content content) {
        var existingContent = findById(contentId);
        existingContent.setName(content.getName())
                .setText(content.getText())
                .setMovieUrl(content.getMovieUrl());
        setContentType(existingContent);

        log.info("IN update content: {}", existingContent);
        return existingContent;
    }

    @Override
    public void deleteById(Long contentId) {
        contentRepository.deleteById(contentId);
        log.info("IN deleteById content by ID: {}", contentId);
    }

    private void setContentType(Content content) {
        if (content.getText() != null && content.getMovieUrl() != null) {
            content.setTypeContent(TypeContent.MIXED);
        } else if (content.getText() != null) {
            content.setTypeContent(TypeContent.TEXT);
        } else if (content.getMovieUrl() != null) {
            content.setTypeContent(TypeContent.VIDEO);
        } else {
            content.setTypeContent(TypeContent.MIXED);
        }
    }

}
