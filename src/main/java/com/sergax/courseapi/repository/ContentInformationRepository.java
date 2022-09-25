package com.sergax.courseapi.repository;

import com.sergax.courseapi.model.course.ContentInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentInformationRepository extends JpaRepository<ContentInformation, Long> {
}