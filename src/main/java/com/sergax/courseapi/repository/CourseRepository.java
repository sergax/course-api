package com.sergax.courseapi.repository;

import com.sergax.courseapi.model.course.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query(value = "select if (exists(select course_id , mentor_id " +
            " from courses_mentors " +
            " where course_id = ?1 and mentor_id = ?2), 'true', 'false')",
            nativeQuery = true)
    boolean existsCourseByMentorId(Long courseId, Long mentorId);
}

