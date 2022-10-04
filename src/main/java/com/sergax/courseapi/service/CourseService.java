package com.sergax.courseapi.service;

import com.sergax.courseapi.dto.CourseDto;
import com.sergax.courseapi.model.course.Course;

import java.util.List;

public interface CourseService extends BaseService<CourseDto, Long> {
    List<CourseDto> findAllPublicCourses();
    CourseDto createCourseByMentor(CourseDto courseDto, String mentorEmail);
    CourseDto updateCourseByMentor(Long courseId, CourseDto courseDto, String mentorEmail);
    void existMentorInCourse(Long courseId, Long mentorId);
}
