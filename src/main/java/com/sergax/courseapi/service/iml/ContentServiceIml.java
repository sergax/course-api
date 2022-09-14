package com.sergax.courseapi.service.iml;

import com.sergax.courseapi.dto.ContentDto;
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
        var content = contentDto.toContent();
        var savedContent = contentRepository.save(content);
        return new ContentDto(savedContent);
    }

    @Override
    @Transactional
    public ContentDto addContentToCourse(Long courseId, ContentDto contentDto, String mentorEmail) {
        var mentorDto = userService.findUserByEmail(mentorEmail);
        var courseDto = courseService.findById(courseId);
        courseService.existMentorInCourse(courseDto.getId(), mentorDto.getId());
        setContentType(contentDto);
        var course = courseDto.toCourse();
        var content = contentDto.toContent().setCourse(course);
        var savedContent = contentRepository.save(content);

        log.info("IN addContentToCourse: {}", new ContentDto(savedContent));
        return new ContentDto(savedContent);
    }

    @Override
    public ContentDto update(Long contentId, ContentDto contentDto) {
        var existingContent = findById(contentId);
        existingContent
                .setName(contentDto.getName())
                .setText(contentDto.getText())
                .setMovieUrl(contentDto.getMovieUrl());
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
        if (contentDto.getText() != null && contentDto.getMovieUrl() != null) {
            contentDto.setTypeContent(TypeContent.MIXED);
        } else if (contentDto.getText() != null) {
            contentDto.setTypeContent(TypeContent.TEXT);
        } else if (contentDto.getMovieUrl() != null) {
            contentDto.setTypeContent(TypeContent.VIDEO);
        } else {
            contentDto.setTypeContent(TypeContent.MIXED);
        }
    }

}
