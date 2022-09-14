package com.sergax.courseapi.dto;

import com.sergax.courseapi.model.user.Role;
import com.sergax.courseapi.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MentorDto extends BaseEntityDto {
    private String firstName;
    private String lastName;
    private String email;
    private List<RoleDto> roles;

    public MentorDto(User mentor) {
        this.id = mentor.getId();
        this.firstName = mentor.getFirstName();
        this.lastName = mentor.getLastName();
        this.email = mentor.getEmail();
        this.roles = mentor.getRoles().stream()
                .map(RoleDto::new)
                .collect(Collectors.toList());
    }

    public User toMentor() {
        return new User()
                .setId(this.id)
                .setFirstName(this.firstName)
                .setLastName(this.lastName)
                .setEmail(this.email)
                .setRoles(this.roles.stream()
                        .map(RoleDto::toRole)
                        .collect(Collectors.toList()));
    }

}
