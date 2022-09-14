package com.sergax.courseapi.service.iml;

import com.sergax.courseapi.dto.RoleDto;
import com.sergax.courseapi.model.user.Role;
import com.sergax.courseapi.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleServiceImlTest {
    @Mock
    private RoleRepository roleRepositoryMock;
    @InjectMocks
    private RoleServiceIml roleServiceImlTest;
    private final Role roleTest = new Role();

    @BeforeEach
    void setUp() {
        roleTest.setName("name");
    }

    @Test
    void findAll() {
        roleServiceImlTest.findAll();
        verify(roleRepositoryMock).findAll();
    }

    @Test
    void findById() {
        when(roleRepositoryMock.findById(anyLong())).thenReturn(Optional.of(new Role()));
        roleServiceImlTest.findById(anyLong());
        verify(roleRepositoryMock).findById(anyLong());
    }

    @Test
    void save() {
        var roleDto = new RoleDto();
        when(roleRepositoryMock.save(new Role())).thenReturn(new Role());

        var savedRole = roleServiceImlTest.save(roleDto);
        assertEquals(roleDto, savedRole);
    }

    @Test
    void update() {
        var roleDto = new RoleDto();
        roleDto.setName("new name");
        when(roleRepositoryMock.findById(roleTest.getId())).thenReturn(Optional.of(roleTest));

        var updatedRole = roleServiceImlTest.update(roleDto.getId(), roleDto);
        assertEquals("new name", updatedRole.getName());
    }

    @Test
    void deleteById() {
        roleServiceImlTest.deleteById(anyLong());
        verify(roleRepositoryMock).deleteById(anyLong());
    }
}