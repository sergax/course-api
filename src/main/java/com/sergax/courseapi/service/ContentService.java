package com.sergax.courseapi.service;

import com.sergax.courseapi.dto.ContentDto;
import com.sergax.courseapi.dto.CourseDto;
import com.sergax.courseapi.model.course.Content;

public interface ContentService extends BaseService<ContentDto, Long> {
    ContentDto addContentToCourse(Long courseId, ContentDto contentDto, String mentorEmail);
}
