package com.sergax.courseapi.controller;

import com.sergax.courseapi.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserRestControllerV1Test {
    @Mock
    private UserService userServiceMock;
    @InjectMocks
    private UserRestControllerV1 userRestControllerV1Test;

    @Test
    void contextLoads() {
        assertThat(userRestControllerV1Test).isNotNull();
    }

    @Test
    void findAllUsers() {
        userRestControllerV1Test.findAllUsers();
        verify(userServiceMock).findAll();
    }

    @Test
    void findUserById() {
        userRestControllerV1Test.findUserById(1L);
        verify(userServiceMock).findById(1L);
    }

    @Test
    void createUser() {
        userRestControllerV1Test.createUser(any());
        verify(userServiceMock).save(any());
    }

    @Test
    void updateUser() {
        userRestControllerV1Test.updateUser(anyLong(), any());
        verify(userServiceMock).update(anyLong(), any());
    }

    @Test
    void addRoleToUser() {
        userRestControllerV1Test.addRoleToUser(anyLong(), anyLong());
        verify(userServiceMock).addRoleForUserById(anyLong(), anyLong());
    }

    @Test
    void deleteUser() {
        userRestControllerV1Test.deleteUser(anyLong());
        verify(userServiceMock).deleteById(anyLong());
    }
}