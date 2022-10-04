package com.sergax.courseapi.service;


import com.sergax.courseapi.dto.ContentInformationDto;
import com.sergax.courseapi.dto.StudentProgressDto;

public interface ContentInformationService {
    Integer findProgressByCourseIdAndStudentId(Long courseId, Long studentId);

    ContentInformationDto passedContentByStudent(Long contentId, String studentEmail);
}
