package com.sergax.courseapi.service.iml;

import com.sergax.courseapi.dto.ContentDto;
import com.sergax.courseapi.dto.CourseDto;
import com.sergax.courseapi.dto.UserDto;
import com.sergax.courseapi.model.course.Content;
import com.sergax.courseapi.model.course.Course;
import com.sergax.courseapi.model.course.TypeContent;
import com.sergax.courseapi.model.user.Role;
import com.sergax.courseapi.model.user.User;
import com.sergax.courseapi.repository.ContentRepository;
import com.sergax.courseapi.repository.CourseRepository;
import com.sergax.courseapi.service.CourseService;
import com.sergax.courseapi.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContentServiceImlTest {
    @Mock
    private ContentRepository contentRepositoryMock;
    @Mock
    private UserService userServiceMock;
    @Mock
    private CourseService courseServiceMock;
    @Mock
    private CourseRepository courseRepositoryMock;
    @InjectMocks
    private ContentServiceIml contentServiceImlMock;
    private final Content contentTest = new Content();
    private final Course courseTest = new Course();
    private final User mentorTest = new User();

    @BeforeEach
    void setUp() {
        courseTest
                .setId(2L);

        mentorTest.setId(50L)
                .setRoles(List.of(new Role()))
                .setEmail("mentorTest@mail.com");

        contentTest
                .setId(1L)
                .setText("text")
                .setName("name")
                .setTypeContent(TypeContent.MIXED)
                .setMovieUrl("https://music.youtube.com/playlist?list=RDCLAK5uy_mPolD_J22gS1SKxufARWcTZd1UrAH_0ZI")
                .setCourse(courseTest);
    }

    @Test
    void canFindAllContents() {
        contentServiceImlMock.findAll();
        verify(contentRepositoryMock).findAll();
    }

    @Test
    void canFindContentById() {
        when(contentRepositoryMock.findById(anyLong())).thenReturn(Optional.of(new Content()));
        contentServiceImlMock.findById(anyLong());
        verify(contentRepositoryMock).findById(anyLong());
    }

    @Test
    void canSaveContent() {
        var content = new Content();
        content.setTypeContent(TypeContent.MIXED);
        var expectedContentDto = new ContentDto();
        when(contentRepositoryMock.save(content)).thenReturn(content);

        var actualContent = contentServiceImlMock.save(expectedContentDto);
        assertEquals(expectedContentDto, actualContent);
    }

    @Test
    void canAddContentToCourse() {
        var expectedContentDto = new ContentDto(contentTest);
        when(courseServiceMock.findById(courseTest.getId())).thenReturn(new CourseDto(courseTest));
        when(userServiceMock.findUserByEmail(mentorTest.getEmail())).thenReturn(new UserDto(mentorTest));
        when(contentRepositoryMock.save(contentTest)).thenReturn(contentTest);

        var actualContent =
                contentServiceImlMock.addContentToCourse(courseTest.getId(), expectedContentDto, mentorTest.getEmail());
        assertEquals(expectedContentDto, actualContent);
    }

    @Test
    void canUpdateCourse() {
        var contentDto = new ContentDto(contentTest);
        contentDto.setName("new name");
        contentDto.setText("new text");
        contentDto.setMovieUrl(null);
        when(contentRepositoryMock.findById(anyLong())).thenReturn(Optional.of(contentTest));

        var actualContent = contentServiceImlMock.update(contentDto.getId(), contentDto);
        assertEquals("new name", actualContent.getName());
        assertEquals("new text", actualContent.getText());
        assertEquals(TypeContent.TEXT, actualContent.getTypeContent());
    }

    @Test
    void canDeleteContentById() {
        contentServiceImlMock.deleteById(contentTest.getId());
        verify(contentRepositoryMock).deleteById(contentTest.getId());
    }

}