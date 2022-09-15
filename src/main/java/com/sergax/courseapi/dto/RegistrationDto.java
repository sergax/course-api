package com.sergax.courseapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class RegistrationDto {
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

    public UserDto toUserDto() {
        return new UserDto().setFirstName(this.firstName)
                .setLastName(this.lastName)
                .setPassword(this.password)
                .setEmail(this.email)
                .setCreated(this.created)
                .setUpdated(this.updated);

    }
}
