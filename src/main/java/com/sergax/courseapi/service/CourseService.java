package com.sergax.courseapi.service;

import com.sergax.courseapi.dto.ContentDto;
import com.sergax.courseapi.dto.CourseDto;

public interface CourseService extends BaseService<CourseDto, Long> {
    CourseDto createCourseByMentor(CourseDto courseDto, String mentorEmail);
    boolean isMentorInCourse(Long courseId, Long mentorId);
}
