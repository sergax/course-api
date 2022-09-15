package com.sergax.courseapi.service.iml;

import com.sergax.courseapi.dto.ContentDto;
import com.sergax.courseapi.dto.UserDto;
import com.sergax.courseapi.model.course.Content;
import com.sergax.courseapi.model.course.TypeContent;
import com.sergax.courseapi.repository.ContentRepository;
import com.sergax.courseapi.service.ContentService;
import com.sergax.courseapi.service.CourseService;
import com.sergax.courseapi.service.UserService;
import com.sergax.courseapi.service.exception.InvalidUrlException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
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
        var newContent = contentDto.toContent();
        setContentType(newContent);
        var savedContent = contentRepository.save(newContent);
        return new ContentDto(savedContent);
    }

    @Override
    @Transactional
    public ContentDto addContentToCourse(Long courseId, ContentDto contentDto, String mentorEmail) {
        var mentorDto = userService.findUserByEmail(mentorEmail);
        var courseDto = courseService.findById(courseId);
        courseService.existMentorInCourse(courseDto.getId(), mentorDto.getId());
        isUrlValid(contentDto.getMovieUrl());
        setContentType(contentDto.toContent());
        var course = courseDto.toCourse();
        var content = contentDto.toContent().setCourse(course);
        var savedContent = contentRepository.save(content);

        log.info("IN addContentToCourse: {}", new ContentDto(savedContent));
        return new ContentDto(savedContent);
    }

    @Override
    public ContentDto update(Long contentId, ContentDto contentDto) {
        var existingContent = contentRepository.getById(contentId);
        isUrlValid(contentDto.getMovieUrl());
        existingContent
                .setName(contentDto.getName())
                .setText(contentDto.getText())
                .setMovieUrl(contentDto.getMovieUrl());
        setContentType(existingContent);

        return new ContentDto(existingContent);
    }

    @Override
    @Transactional
    public ContentDto updateContentByMentor(Long contentId, ContentDto contentDto, String mentorEmail) {
        var userId = userService.findUserByEmail(mentorEmail).getId();
        var courseId = contentRepository.getById(contentId).getCourse().getId();
        courseService.existMentorInCourse(courseId, userId);
        var updatedContent = update(contentId, contentDto);

        log.info("IN updateContentByMentor : {}", updatedContent);
        return updatedContent;
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
            content.setTypeContent(TypeContent.NO_CONTENT);
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
