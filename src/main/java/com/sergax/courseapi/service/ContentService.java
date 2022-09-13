package com.sergax.courseapi.service;

import com.sergax.courseapi.dto.ContentDto;
import com.sergax.courseapi.dto.CourseDto;

public interface ContentService extends BaseService<ContentDto, Long> {
    ContentDto addContentToCourse(Long courseId, ContentDto contentDto, String mentorEmail);
}
