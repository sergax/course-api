package com.sergax.courseapi.service;

import com.sergax.courseapi.dto.CourseDto;
import com.sergax.courseapi.model.course.Course;

public interface CourseService extends BaseService<CourseDto, Long> {
    CourseDto createCourseByMentor(CourseDto courseDto, String mentorEmail);
    void existMentorInCourse(Long courseId, Long mentorId);
}
