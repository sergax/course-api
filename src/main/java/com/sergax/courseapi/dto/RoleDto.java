package com.sergax.courseapi.dto;

import com.sergax.courseapi.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto {
    private Long id;
    private String name;
    private Set<UserDto> users;

    public RoleDto(Role role) {
        this.id = role.getId();
        this.name = role.getName();
        this.users = role.getUsers().stream()
                .map(UserDto::new)
                .collect(Collectors.toSet());
    }
}
