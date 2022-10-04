package com.sergax.courseapi.controller;

import com.sergax.courseapi.dto.*;
import com.sergax.courseapi.model.user.User;
import com.sergax.courseapi.service.*;
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
import static org.mockito.Mockito.*;

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
    @Mock
    private UserService userServiceMock;
    @InjectMocks
    private CourseRestControllerV1 courseRestControllerV1Test;
    private final Principal principal = mock(Principal.class);
    private final User mentorTest = new User();
    private final CourseDto courseDtoTest = new CourseDto();
    private final ContentDto contentDtoTest = new ContentDto();

    @BeforeEach
    void setUp() {
        mentorTest
                .setId(5L)
                .setEmail("mentorTest@mail.com");
        Mockito.when(principal.getName()).thenReturn(mentorTest.getEmail());
    }

    @Test
    void contextLoads() {
        assertThat(courseRestControllerV1Test).isNotNull();
    }

    @Test
    void findAmountOfLikesByCourseId() {
        courseRestControllerV1Test.findAmountOfLikesByCourseId(1L);
        verify(courseInformationServiceMock).findAmountOfLikesByCourseId(1L);
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
        courseRestControllerV1Test.addStudentToCourse(1L, principal);
        verify(courseInformationServiceMock).addStudentToCourse(1L, mentorTest.getEmail());
    }

    @Test
    void addLikesAndCommentsToCourseByStudent() {
        var courseInformationDto = new CourseInformationDto();
        courseRestControllerV1Test.addLikesToCourseByStudent(1L, principal);
        verify(courseInformationServiceMock).addLikesToCourseByStudent(1L, mentorTest.getEmail());
    }

    @Test
    void passedContentByStudent() {
        courseRestControllerV1Test.passedContentByStudent(1L, principal);
        verify(contentInformationServiceMock).passedContentByStudent(1L, mentorTest.getEmail());
    }
}