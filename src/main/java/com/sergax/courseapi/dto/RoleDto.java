package com.sergax.courseapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sergax.courseapi.model.user.Role;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleDto extends BaseEntityDto {
    private String name;
    private List<UserDto> users;

    public RoleDto(Role role) {
        this.id = role.getId();
        this.name = role.getName();
        this.users = role.getUsers().stream()
                .map(UserDto::new)
                .collect(Collectors.toList());
    }

}
