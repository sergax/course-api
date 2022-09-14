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
    private List<String> roles;

    public MentorDto(User mentor) {
        this.id = mentor.getId();
        this.firstName = mentor.getFirstName();
        this.lastName = mentor.getLastName();
        this.email = mentor.getEmail();
        this.roles = mentor.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());
    }

}
