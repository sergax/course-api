package com.sergax.courseapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sergax.courseapi.model.User;
import com.sergax.courseapi.model.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private LocalDate created;
    private LocalDate updated;
    private UserStatus status;
    private Set<RoleDto> roleDto;

    public UserDto(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.password = user.getPassword();
        this.created = user.getCreated();
        this.updated = user.getUpdated();
        this.status = user.getStatus();
        if (this.roleDto == null) {
            this.roleDto = new HashSet<>();
        } else {
            this.roleDto = user.getRoles().stream()
                    .map(RoleDto::new)
                    .collect(Collectors.toSet());
        }
    }

    public User toUser() {
        return new User()
                .setId(this.id)
                .setFirstName(this.firstName)
                .setLastName(this.lastName)
                .setEmail(this.email)
                .setCreated(this.created)
                .setUpdated(this.updated);
    }
}
