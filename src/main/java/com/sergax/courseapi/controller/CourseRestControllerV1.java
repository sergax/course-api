package com.sergax.courseapi.controller;

import com.sergax.courseapi.dto.ContentDto;
import com.sergax.courseapi.dto.CourseDto;
import com.sergax.courseapi.service.ContentService;
import com.sergax.courseapi.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/courses")
@RequiredArgsConstructor
public class CourseRestControllerV1 {
    private final CourseService courseService;
    private final ContentService contentService;

    @GetMapping("/all")
    public ResponseEntity<List<CourseDto>> findAllCourses() {
        return ResponseEntity.ok(courseService.findAll());
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<CourseDto> findUserById(@PathVariable Long courseId) {
        return ResponseEntity.ok(courseService.findById(courseId));
    }

    @PostMapping
    public ResponseEntity<CourseDto> createCourseByMentor(@RequestBody CourseDto courseDto,
                                                          Principal principal) {
        return new ResponseEntity<>(
                courseService.createCourseByMentor(courseDto, principal.getName()),
                HttpStatus.CREATED);
    }

    @PostMapping("/{courseId}")
    public ResponseEntity<ContentDto> addContentToCourse(@PathVariable Long courseId,
                                                         @RequestBody ContentDto contentDto,
                                                         Principal principal) {
        return new ResponseEntity<>(
                contentService.addContentToCourse(courseId, contentDto, principal.getName()),
                HttpStatus.CREATED);
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<CourseDto> updateUser(@PathVariable Long courseId,
                                                @RequestBody CourseDto courseDto) {
        return ResponseEntity.ok(courseService.update(courseId, courseDto));
    }

}