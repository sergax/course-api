package com.sergax.courseapi.service.iml;

import com.sergax.courseapi.dto.CourseInformationDto;
import com.sergax.courseapi.model.course.Course;
import com.sergax.courseapi.model.course.CourseInformation;
import com.sergax.courseapi.model.user.User;
import com.sergax.courseapi.repository.CourseInformationRepository;
import com.sergax.courseapi.repository.CourseRepository;
import com.sergax.courseapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourseInformationServiceImplTest {
    @Mock
    private CourseInformationRepository courseInformationRepositoryMock;
    @Mock
    private CourseRepository courseRepositoryMock;
    @Mock
    private UserRepository userRepositoryMock;
    @InjectMocks
    private CourseInformationServiceImpl courseInformationServiceTest;
    private final CourseInformation courseInformation = new CourseInformation();
    private final User studentTest = new User();
    private final Course courseTest = new Course();

    @BeforeEach
    void setUp() {
        courseInformation
                .setDateRegistered(LocalDate.now())
                .setCourse(courseTest)
                .setStudent(studentTest);

        studentTest.setEmail("student@mail.com");

        courseTest.setId(5L);
    }

    @Test
    void canFindAmountOfLikesByCourseId() {
        courseInformationServiceTest.findAmountOfLikesByCourseId(1L);
        verify(courseInformationRepositoryMock).findAmountOfLikesByCourseId(1L);
    }

    @Test
    void canAddStudentToCourse() {
        var expectedCourseInformationDto = new CourseInformationDto(courseInformation);
        when(userRepositoryMock.findByEmail(studentTest.getEmail())).thenReturn(Optional.of(studentTest));
        when(courseRepositoryMock.getById(courseTest.getId())).thenReturn(courseTest);
        when(courseRepositoryMock.existsCourseByMentorId(courseTest.getId(), studentTest.getId())).thenReturn(false);
        when(courseRepositoryMock.existsCourseByStudentId(courseTest.getId(), studentTest.getId())).thenReturn(false);
        when(courseInformationRepositoryMock.save(courseInformation)).thenReturn(courseInformation);

        var actualCourseInformationDto =
                courseInformationServiceTest.addStudentToCourse(courseTest.getId(), studentTest.getEmail());
        assertEquals(expectedCourseInformationDto, actualCourseInformationDto);
    }

    @Test
    void canAddLikesAndCommentsToCourseByStudent() {
        var courseInformationDto = new CourseInformationDto(courseInformation)
                .setComments("comments")
                .setLikes(true);
        when(userRepositoryMock.findByEmail(studentTest.getEmail())).thenReturn(Optional.of(studentTest));
        when(courseInformationRepositoryMock.findCourseInformationByCourseIdAndStudentId(courseTest.getId(), studentTest.getId()))
                .thenReturn(Optional.of(courseInformation));

        var actualCourseInformationDto =
                courseInformationServiceTest.addLikesToCourseByStudent(courseTest.getId(), studentTest.getEmail());
        assertEquals("comments", actualCourseInformationDto.getComments());
        assertTrue(actualCourseInformationDto.isLikes());
    }
}