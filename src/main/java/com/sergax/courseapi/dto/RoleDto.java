package com.sergax.courseapi.dto;

import com.sergax.courseapi.model.Role;
import lombok.Data;

@Data
public class RoleDto {
    private Long id;
    private String name;

    public RoleDto(Role role) {
        this.id = role.getId();
        this.name = role.getName();
    }
}
