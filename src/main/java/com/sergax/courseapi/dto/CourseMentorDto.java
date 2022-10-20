package com.sergax.courseapi.dto;

import com.sergax.courseapi.model.course.CourseStatus;

import java.util.Set;

public record CourseMentorDto(Long id,
                              String name,
                              String description,
                              String logoUrl,
                              String movieUrl,
                              CourseStatus status,
                              Set<Long> mentorsId) {
}
