package com.sergax.courseapi.service;

import com.sergax.courseapi.dto.CourseDto;
import com.sergax.courseapi.model.course.Course;

public interface CourseService extends BaseService<Course, Long> {
    CourseDto createCourseByMentor(Course course, String mentorEmail);
    boolean existMentorInCourse(Long courseId, Long mentorId);
}
