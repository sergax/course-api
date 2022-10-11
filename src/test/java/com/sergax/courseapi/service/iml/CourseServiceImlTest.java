package com.sergax.courseapi.service.iml;

import com.sergax.courseapi.dto.CourseDto;
import com.sergax.courseapi.dto.UserDto;
import com.sergax.courseapi.model.course.Content;
import com.sergax.courseapi.model.course.Course;
import com.sergax.courseapi.model.course.CourseStatus;
import com.sergax.courseapi.model.user.Role;
import com.sergax.courseapi.model.user.User;
import com.sergax.courseapi.repository.CourseRepository;
import com.sergax.courseapi.service.UserService;
import com.sergax.courseapi.service.exception.InvalidMentorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourseServiceImlTest {
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private UserService userService;
    @InjectMocks
    private CourseServiceIml courseServiceIml;
    private final Course courseTest = new Course();
    private final Content content = new Content();
    private final User mentorTest = new User();

    @BeforeEach
    void setUp() {
        mentorTest
                .setId(20L)
                .setEmail("mentorTest@mail.com")
                .setRoles(List.of(new Role()));

        content.setId(600L);

        courseTest
                .setId(55L)
                .setDateStart(LocalDate.now())
                .setDateEnd(LocalDate.now().plusDays(100L))
                .setCourseStatus(CourseStatus.PRIVATE);
    }

    @Test
    void canFindAll() {
        courseServiceIml.findAll();
        verify(courseRepository).findAll();
    }

    @Test
    void canFindById() {
        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(new Course()));
        courseServiceIml.findById(anyLong());
        verify(courseRepository).findById(anyLong());
    }

    @Test
    void canSave() {
        var expectedCourse = new CourseDto(courseTest);
        when(courseRepository.save(courseTest)).thenReturn(courseTest);

        var actualCourse = courseServiceIml.save(expectedCourse);
        assertEquals(expectedCourse, actualCourse);
    }

    @Test
    void canCreateCourseByMentor() {
        courseTest.setMentors(Set.of(mentorTest));
        var courseDto = new CourseDto(courseTest);
        var mentorDto = new UserDto(mentorTest);
        when(userService.findUserByEmail(mentorTest.getEmail())).thenReturn(mentorDto);
        when(courseRepository.save(courseTest)).thenReturn(courseTest);

        var courseByMentor = courseServiceIml.createCourseByMentor(courseDto, mentorDto.getEmail());
        assertEquals(1, courseByMentor.getMentorsId().size());
    }

    @Test
    void canUpdateCourse() {
        var courseDto = new CourseDto(courseTest);
        courseDto.setName("new name")
                .setStatus(CourseStatus.PUBLIC)
                .setDateEnd(LocalDate.now().plusDays(1L));
        when(courseRepository.getById(courseTest.getId())).thenReturn(courseTest);

        var updatedCourse = courseServiceIml.update(courseDto.getId(), courseDto);
        assertEquals("new name", updatedCourse.getName());
        assertEquals(LocalDate.now().plusDays(1L), updatedCourse.getDateEnd());
        assertEquals(CourseStatus.PUBLIC, updatedCourse.getStatus());
    }

    @Test
    void canDeleteCourseById() {
        var courseDto = new CourseDto(courseTest);
        courseDto.setStatus(CourseStatus.DELETED);
        when(courseRepository.findById(courseTest.getId())).thenReturn(Optional.of(courseTest));

        courseServiceIml.deleteById(courseDto.getId());
        assertEquals(CourseStatus.DELETED, courseDto.getStatus());
    }

    @Test
    void shouldThrow_whenMentorNotInCourse() {
        when(courseRepository.existsCourseByMentorId(anyLong(), anyLong())).thenReturn(false);

        assertThatThrownBy(() -> courseServiceIml.existMentorInCourse(
                 courseTest.getId(), mentorTest.getId()))
                .isInstanceOf(InvalidMentorException.class)
                .hasMessage("User ID: %d not a mentor on this course ID: %d", mentorTest.getId(), courseTest.getId());
    }
}