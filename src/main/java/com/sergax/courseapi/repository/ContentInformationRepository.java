package com.sergax.courseapi.repository;

import com.sergax.courseapi.dto.StudentProgressDto;
import com.sergax.courseapi.model.course.ContentInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentInformationRepository extends JpaRepository<ContentInformation, Long> {
    @Query(value = "select count(*) " +
            "/(select count(*) " +
            "from courses_contents " +
            "where course_id = ?1) * 100 " +
            "from courses c " +
            "left join  courses_contents cc on c.id = cc.course_id " +
            "left join contents_students cs on cc.id = cs.content_id " +
            "where cs.passed = 1 " +
            "and c.id = ?1 " +
            "and cs.student_id = ?2 " +
            "group by c.id, cs.student_id ", nativeQuery = true)
    Integer findProgressStudentByStudentIdAndCourseId(Long courseId, Long studentId);
}

