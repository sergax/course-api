package com.sergax.courseapi.service;

import com.sergax.courseapi.dto.ContentDto;

public interface ContentService extends BaseService<ContentDto, Long> {
    ContentDto addContentToCourse(Long courseId, ContentDto contentDto, String mentorEmail);
    ContentDto updateContentByMentor(Long contentId, ContentDto contentDto, String mentorEmail);
}
