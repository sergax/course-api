package com.sergax.courseapi.service;


import com.sergax.courseapi.dto.CourseInformationDto;

public interface CourseInformationService {
    Integer findAmountOfLikesByCourseId(Long courseId);
    CourseInformationDto addStudentToCourse(Long courseId, String studentEmail);
    CourseInformationDto addLikesToCourseByStudent(Long courseId, String studentEmail);
}
