package com.sergax.courseapi.dto;

import com.sergax.courseapi.model.user.User;
import com.sergax.courseapi.model.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper=false)
public class UserDto extends BaseEntityDto {
    @Pattern(regexp = "^[A-Za-z,.'-]+$",
            message = "First name can contain only English alphabet letters, commas, dots, apostrophes and dashes")
    @NotNull(message = "First name is required")
    @Size(max = 50, message = "Maximum size of first name is 50 characters")
    private String firstName;
    @Pattern(regexp = "^[A-Za-z,.'-]+$",
            message = "First name can contain only English alphabet letters, commas, dots, apostrophes and dashes")
    @NotNull(message = "First name is required")
    @Size(max = 50, message = "Maximum size of first name is 50 characters")
    private String lastName;
    @NotNull(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;
    @NotNull(message = "Email is required")
    @Size(max = 255, message = "Maximum size of email is 255 characters")
    private String email;
    private LocalDate created;
    private LocalDate updated;
    private Status status;
    private List<RoleDto> roles;

    public UserDto(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.created = user.getCreated();
        this.updated = user.getUpdated();
        this.status = user.getStatus();
        this.roles = user.getRoles().stream()
                .map(RoleDto::new)
                .collect(Collectors.toList());
    }

    public User toUser() {
        return new User()
                .setId(this.getId())
                .setFirstName(this.getFirstName())
                .setLastName(this.getLastName())
                .setPassword(this.getPassword())
                .setEmail(this.getEmail())
                .setCreated(this.getCreated())
                .setUpdated(this.getUpdated())
                .setStatus(this.getStatus())
                .setRoles(this.getRoles().stream()
                        .map(RoleDto::toRole)
                        .collect(Collectors.toList()));
    }

}
