package com.sergax.courseapi.service.iml;

import com.sergax.courseapi.dto.ContentInformationDto;
import com.sergax.courseapi.model.course.Content;
import com.sergax.courseapi.model.course.ContentInformation;
import com.sergax.courseapi.model.course.Course;
import com.sergax.courseapi.model.user.User;
import com.sergax.courseapi.repository.ContentInformationRepository;
import com.sergax.courseapi.repository.ContentRepository;
import com.sergax.courseapi.repository.CourseRepository;
import com.sergax.courseapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContentInformationServiceImplTest {
    @Mock
    private ContentInformationRepository contentInformationRepositoryMock;
    @Mock
    private CourseRepository courseRepositoryMock;
    @Mock
    private ContentRepository contentRepositoryMock;
    @Mock
    private UserRepository userRepositoryMock;
    @InjectMocks
    private ContentInformationServiceImpl contentInformationServiceTest;
    private final ContentInformation contentInformationTest = new ContentInformation();
    private final User studentTest = new User();
    private final Content contentTest = new Content();
    private final Course courseTest = new Course();

    @BeforeEach
    void setUp() {
        courseTest
                .setId(555L);

        contentTest
                .setId(4L)
                .setCourse(courseTest);

        studentTest
                .setId(21L)
                .setEmail("student@mail.com");

        contentInformationTest
                .setId(47L)
                .setPassed(true)
                .setContent(contentTest)
                .setStudent(studentTest);
    }

    @Test
    void canFindProgressByCourseIdAndStudentId() {
        when(contentInformationRepositoryMock.findProgressStudentByStudentIdAndCourseId(anyLong(), anyLong())).thenReturn(50);
        contentInformationServiceTest.findProgressByCourseIdAndStudentId(anyLong(), anyLong());
        verify(contentInformationRepositoryMock).findProgressStudentByStudentIdAndCourseId(anyLong(), anyLong());
    }

    @Test
    void canPasseContentByStudent() {
        var expectedContentInformationDto = new ContentInformationDto(contentInformationTest);
        when(contentRepositoryMock.getById(contentTest.getId())).thenReturn(contentTest);
        when(userRepositoryMock.findByEmail(studentTest.getEmail())).thenReturn(Optional.of(studentTest));
        when(courseRepositoryMock.existsCourseByMentorId(courseTest.getId(), studentTest.getId())).thenReturn(false);
        when(contentInformationRepositoryMock.save(contentInformationTest)).thenReturn(contentInformationTest);

        var actualContentInformationDto =
                contentInformationServiceTest.passedContentByStudent(contentTest.getId(), expectedContentInformationDto, studentTest.getEmail());
        assertEquals(expectedContentInformationDto, actualContentInformationDto);
    }

}

