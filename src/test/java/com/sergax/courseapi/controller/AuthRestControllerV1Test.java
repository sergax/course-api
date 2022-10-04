package com.sergax.courseapi.controller;

import com.sergax.courseapi.dto.*;
import com.sergax.courseapi.model.Status;
import com.sergax.courseapi.model.user.ConfirmationCode;
import com.sergax.courseapi.security.JwtTokenProvider;
import com.sergax.courseapi.service.CourseService;
import com.sergax.courseapi.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthRestControllerV1Test {
    @Mock
    private AuthenticationManager authenticationManagerMock;
    @Mock
    private UserService userServiceMock;
    @Mock
    private JwtTokenProvider jwtTokenProviderMock;
    @Mock
    private CourseService courseService;
    @InjectMocks
    private AuthRestControllerV1 authRestControllerV1UnderTest;
    private final LoginRequestDto loginRequestDto = new LoginRequestDto();
    private final UserDto userDto = new UserDto();
    private final RoleDto roleDto = new RoleDto();
    private final RegistrationDto registrationDto = new RegistrationDto();
    private final ConfirmationCodeDto confirmationCodeDto = new ConfirmationCodeDto();
    private final ConfirmationCode confirmationCode = new ConfirmationCode();

    @BeforeEach
    void setUp() {
        roleDto.setId(1L);
        userDto.setEmail("user@mail.com")
                .setStatus(Status.NOT_CONFIRMED);
        userDto.setRoles(List.of(roleDto));
    }

    @Test
    void contextLoads() {
        assertThat(authRestControllerV1UnderTest).isNotNull();
    }

    @Test
    void login() {
        when(userServiceMock.findUserByEmail(loginRequestDto.getEmail())).thenReturn(userDto);
        authRestControllerV1UnderTest.login(loginRequestDto);
        verify(authenticationManagerMock).authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getEmail(), loginRequestDto.getPassword()));
    }

    @Test
    void registration() {
        var registerUserDto = registrationDto.toUserDto();
        authRestControllerV1UnderTest.registration(registrationDto);
        verify(userServiceMock).register(registerUserDto);
    }

    @Test
    void emailConfirmation() {
        when(userServiceMock.confirmEmail(confirmationCode)).thenReturn(userDto);
        authRestControllerV1UnderTest.emailConfirmation(confirmationCodeDto);
        verify(userServiceMock).confirmEmail(confirmationCode);
    }
}