package com.sergax.courseapi.service.iml;

import com.sergax.courseapi.model.course.Content;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContentInformationServiceImplTest {
    @Mock
    private ContentInformationRepository contentInformationRepository;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private ContentRepository contentRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private ContentInformationServiceImplTest contentInformationServiceImplTest;

    @BeforeEach
    void setUp() {
    }

    @Test
    void findProgressByCourseIdAndStudentId() {
        when(contentInformationRepository.findProgressStudentByStudentIdAndCourseId(anyLong(), anyLong())).thenReturn(20);
        contentInformationServiceImplTest.findProgressByCourseIdAndStudentId();
        verify(contentInformationRepository).findProgressStudentByStudentIdAndCourseId(anyLong(), anyLong());
    }

    @Test
    void passedContentByStudent() {
        when(contentRepository.getById(anyLong())).thenReturn(new Content());
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(new User()));
        when(courseRepository.existsCourseByStudentId(anyLong(), anyLong())).thenReturn(false);

        contentInformationServiceImplTest.passedContentByStudent();
    }
}