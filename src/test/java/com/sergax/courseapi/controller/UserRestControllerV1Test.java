package com.sergax.courseapi.controller;

import com.sergax.courseapi.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserRestControllerV1Test {
    @Mock
    private UserService userServiceMock;
    @InjectMocks
    private UserRestControllerV1 userRestControllerV1;

    @Test
    void findAllUsers() {
        userRestControllerV1.findAllUsers();
        verify(userServiceMock).findAll();
    }

    @Test
    void findUserById() {
        userRestControllerV1.findUserById(1L);
        verify(userServiceMock).findById(1L);
    }

    @Test
    void createUser() {
        userRestControllerV1.createUser(any());
        verify(userServiceMock).save(any());
    }

    @Test
    void updateUser() {
        userRestControllerV1.updateUser(anyLong(), any());
        verify(userServiceMock).update(anyLong(), any());
    }

    @Test
    void addRoleToUser() {
        userRestControllerV1.addRoleToUser(anyLong(), anyLong());
        verify(userServiceMock).addRoleForUserById(anyLong(), anyLong());
    }

    @Test
    void deleteUser() {
        userRestControllerV1.deleteUser(anyLong());
        verify(userServiceMock).deleteById(anyLong());
    }
}