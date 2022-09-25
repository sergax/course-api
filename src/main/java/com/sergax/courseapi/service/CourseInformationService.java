package com.sergax.courseapi.service;


import com.sergax.courseapi.dto.CourseInformationDto;

public interface CourseInformationService {
    CourseInformationDto addStudentToCourse(Long courseId, CourseInformationDto courseInformationDto, String studentEmail);
    CourseInformationDto addLikesAndCommentsToCourseByStudent(Long courseId, CourseInformationDto courseInformationDto, String studentEmail);
}
