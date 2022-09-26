package com.sergax.courseapi.controller;

import com.sergax.courseapi.dto.*;
import com.sergax.courseapi.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("api/v1/courses")
@RequiredArgsConstructor
public class CourseRestControllerV1 {
    private final CourseService courseService;
    private final ContentService contentService;
    private final CourseInformationService courseInformationService;
    private final ContentInformationService contentInformationService;
    private final UserService userService;

    @GetMapping("/all")
    @Secured(value = "ROLE_USER, ROLE_ADMIN")
    public ResponseEntity<List<CourseDto>> findAllCourses() {
        return ResponseEntity.ok(courseService.findAll());
    }

    @GetMapping("/{courseId}/likes")
    @Secured(value = "ROLE_USER, ROLE_ADMIN")
    public ResponseEntity<Integer> findAmountOfLikesByCourseId(@PathVariable Long courseId) {
        return ResponseEntity.ok(courseInformationService.findAmountOfLikesByCourseId(courseId));
    }

    @GetMapping("/{courseId}/progress")
    @Secured(value = "ROLE_USER, ROLE_ADMIN")
    public ResponseEntity<Integer> findProgressByCourseIdAndStudentId(@PathVariable Long courseId, Principal principal) {
        var userId = userService.findUserByEmail(principal.getName()).getId();
        return ResponseEntity.ok(contentInformationService.findProgressByCourseIdAndStudentId(courseId, userId));
    }


    @GetMapping("/{courseId}")
    @Secured(value = "ROLE_USER, ROLE_ADMIN")
    public ResponseEntity<CourseDto> findCourseById(@PathVariable Long courseId) {
        return ResponseEntity.ok(courseService.findById(courseId));
    }

    @GetMapping("/contents/{contentId}")
    @Secured(value = "ROLE_USER, ROLE_ADMIN")
    public ResponseEntity<ContentDto> findContentById(@PathVariable Long contentId) {
        return ResponseEntity.ok(contentService.findById(contentId));
    }

    @PostMapping
    @Secured(value = "ROLE_USER, ROLE_ADMIN")
    public ResponseEntity<CourseDto> createCourseByMentor(@RequestBody CourseDto courseDto,
                                                          Principal principal) {
        return new ResponseEntity<>(
                courseService.createCourseByMentor(courseDto, principal.getName()),
                HttpStatus.CREATED);
    }

    @PostMapping("/{courseId}/contents")
    @Secured(value = "ROLE_USER, ROLE_ADMIN")
    public ResponseEntity<ContentDto> addContentToCourse(@PathVariable Long courseId,
                                                         @RequestBody ContentDto contentDto,
                                                         Principal principal) {
        return new ResponseEntity<>(
                contentService.addContentToCourse(courseId, contentDto, principal.getName()),
                HttpStatus.CREATED);
    }

    @PutMapping("/{courseId}")
    @Secured(value = "ROLE_USER, ROLE_ADMIN")
    public ResponseEntity<CourseDto> updateCourse(@PathVariable Long courseId,
                                                  @RequestBody CourseDto courseDto,
                                                  Principal principal) {
        return ResponseEntity.ok(courseService.updateCourseByMentor(courseId, courseDto, principal.getName()));
    }

    @PutMapping("/contents/{contentId}")
    @Secured(value = "ROLE_USER, ROLE_ADMIN")
    public ResponseEntity<ContentDto> updateContent(@PathVariable Long contentId,
                                                    @RequestBody ContentDto contentDto,
                                                    Principal principal) {
        return ResponseEntity.ok(
                contentService.updateContentByMentor(contentId, contentDto, principal.getName()));
    }

    @PatchMapping("/{courseId}/students")
    @Secured(value = "ROLE_USER, ROLE_ADMIN")
    public ResponseEntity<CourseInformationDto> addStudentToCourse(@PathVariable Long courseId,
                                                                   @RequestBody CourseInformationDto courseInformationDto,
                                                                   Principal principal) {
        return ResponseEntity.ok(
                courseInformationService.addStudentToCourse(courseId, courseInformationDto, principal.getName()));
    }

    @PatchMapping("/{courseId}/info")
    @Secured(value = "ROLE_USER, ROLE_ADMIN")
    public ResponseEntity<CourseInformationDto> addLikesAndCommentsToCourseByStudent(@PathVariable Long courseId,
                                                                                     @RequestBody CourseInformationDto courseInformationDto,
                                                                                     Principal principal) {
        return ResponseEntity.ok(
                courseInformationService.addLikesAndCommentsToCourseByStudent(courseId, courseInformationDto, principal.getName()));
    }

    @PostMapping("/contents/{contentId}")
    @Secured(value = "ROLE_USER, ROLE_ADMIN")
    public ResponseEntity<ContentInformationDto> passedContentByStudent(@PathVariable Long contentId,
                                                                        @RequestBody ContentInformationDto contentInformationDto,
                                                                        Principal principal) {
        return ResponseEntity.ok(
                contentInformationService.passedContentByStudent(contentId, contentInformationDto, principal.getName()));
    }

}
