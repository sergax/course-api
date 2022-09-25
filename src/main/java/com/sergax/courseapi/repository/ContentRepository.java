package com.sergax.courseapi.repository;

import com.sergax.courseapi.model.course.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseStatus;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {
}