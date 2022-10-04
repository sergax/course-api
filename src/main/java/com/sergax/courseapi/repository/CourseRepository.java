package com.sergax.courseapi.repository;

import com.sergax.courseapi.dto.CourseDto;
import com.sergax.courseapi.model.course.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query("select c from Course c where c.courseStatus = 'PUBLIC'")
    List<CourseDto> findAllByStatusPublic();
    @Query(value = "select if (exists(select course_id , mentor_id " +
            " from courses_mentors " +
            " where course_id = ?1 and mentor_id = ?2), 'true', 'false')",
            nativeQuery = true)
    boolean existsCourseByMentorId(Long courseId, Long mentorId);

    @Query(value = "select if (exists(select course_id , student_id " +
            " from courses_students " +
            " where course_id = ?1 and student_id = ?2), 'true', 'false')",
            nativeQuery = true)
    boolean existsCourseByStudentId(Long courseId, Long studentId);

}

