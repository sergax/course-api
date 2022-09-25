package com.sergax.courseapi.controller;

import com.sergax.courseapi.dto.ContentDto;
import com.sergax.courseapi.dto.ContentInformationDto;
import com.sergax.courseapi.dto.CourseDto;
import com.sergax.courseapi.dto.CourseInformationDto;
import com.sergax.courseapi.model.user.User;
import com.sergax.courseapi.service.ContentInformationService;
import com.sergax.courseapi.service.ContentService;
import com.sergax.courseapi.service.CourseInformationService;
import com.sergax.courseapi.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.Principal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CourseRestControllerV1Test {
    @Mock
    private CourseService courseServiceMock;
    @Mock
    private ContentService contentServiceMock;
    @Mock
    private ContentInformationService contentInformationServiceMock;
    @Mock
    private CourseInformationService courseInformationServiceMock;
    @InjectMocks
    private CourseRestControllerV1 courseRestControllerV1Test;
    private final Principal principal = mock(Principal.class);
    private final User mentorTest = new User();
    private final CourseDto courseDtoTest = new CourseDto();
    private final ContentDto contentDtoTest = new ContentDto();

    @BeforeEach
    void setUp() {
        mentorTest.setEmail("mentorTest@mail.com");
        Mockito.when(principal.getName()).thenReturn(mentorTest.getEmail());
    }

    @Test
    void contextLoads() {
        assertThat(courseRestControllerV1Test).isNotNull();
    }

    @Test
    void findAllCourses() {
        courseRestControllerV1Test.findAllCourses();
        verify(courseServiceMock).findAll();
    }

    @Test
    void findUserById() {
        courseRestControllerV1Test.findCourseById(anyLong());
        verify(courseServiceMock).findById(anyLong());
    }

    @Test
    void createCourseByMentor() {
        courseRestControllerV1Test.createCourseByMentor(courseDtoTest, principal);
        verify(courseServiceMock).createCourseByMentor(courseDtoTest, mentorTest.getEmail());
    }

    @Test
    void addContentToCourse() {
        courseRestControllerV1Test.addContentToCourse(1L, contentDtoTest, principal);
        verify(contentServiceMock).addContentToCourse(1L, contentDtoTest, mentorTest.getEmail());
    }

    @Test
    void addStudentToCourse() {
        var courseInformationDto = new CourseInformationDto();
        courseRestControllerV1Test.addStudentToCourse(1L, courseInformationDto, principal);
        verify(courseInformationServiceMock).addStudentToCourse(1L, courseInformationDto, mentorTest.getEmail());
    }

    @Test
    void addLikesAndCommentsToCourseByStudent() {
        var courseInformationDto = new CourseInformationDto();
        courseRestControllerV1Test.addLikesAndCommentsToCourseByStudent(1L, courseInformationDto, principal);
        verify(courseInformationServiceMock).addLikesAndCommentsToCourseByStudent(1L, courseInformationDto, mentorTest.getEmail());
    }

    @Test
    void passedContentByStudent() {
        var contentInformationDto = new ContentInformationDto();
        courseRestControllerV1Test.passedContentByStudent(1L, contentInformationDto, principal);
        verify(contentInformationServiceMock).passedContentByStudent(1L, contentInformationDto, mentorTest.getEmail());
    }
}