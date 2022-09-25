package com.sergax.courseapi.repository;

import com.sergax.courseapi.dto.CourseInformationDto;
import com.sergax.courseapi.model.course.CourseInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseInformationRepository extends JpaRepository<CourseInformation, Long> {
    Optional<CourseInformation> findCourseInformationByCourseIdAndStudentId(Long courseId, Long studentId);
}

