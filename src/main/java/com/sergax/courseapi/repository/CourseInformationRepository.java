package com.sergax.courseapi.repository;

import com.sergax.courseapi.dto.CourseInformationDto;
import com.sergax.courseapi.model.course.CourseInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseInformationRepository extends JpaRepository<CourseInformation, Long> {
//    @Query(value = "select * " +
//            "from courses_students " +
//            "where course_id = ?1 and student_id = ?2", nativeQuery = true)
    Optional<CourseInformation> findCourseInformationByCourseIdAndStudentId(Long courseId, Long studentId);
}

