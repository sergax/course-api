package com.sergax.courseapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sergax.courseapi.model.user.Role;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class RoleDto extends BaseEntityDto {
    private String name;

    public RoleDto(Role role) {
        this.id = role.getId();
        this.name = role.getName();
    }

    public Role toRole() {
        return new Role()
                .setId(this.id)
                .setName(this.name);
    }

}
