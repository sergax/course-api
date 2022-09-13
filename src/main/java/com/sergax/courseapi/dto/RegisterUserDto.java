package com.sergax.courseapi.dto;

import com.sergax.courseapi.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserDto {
    @Pattern(regexp = "^[A-Za-z,.'-]+$",
            message = "First name can contain only English alphabet letters, commas, dots, apostrophes and dashes")
    @NotNull(message = "First name is required")
    @Size(max = 50, message = "Maximum size of first name is 50 characters")
    private String firstName;
    @Pattern(regexp = "^[A-Za-z,.'-]+$",
            message = "Last name can contain only English alphabet letters, commas, dots, apostrophes and dashes")
    @NotNull(message = "Last name is required")
    @Size(max = 50, message = "Maximum size of last name is 50 characters")
    private String lastName;
    @NotNull(message = "Email is required")
    @Size(max = 255, message = "Maximum size of email is 255 characters")
    private String email;
    @NotNull(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;
    private LocalDate created;
    private LocalDate updated;


    public User toUser() {
        User user = new User();
        user.setEmail(this.email);
        user.setFirstName(this.firstName);
        user.setLastName(this.lastName);
        user.setPassword(this.password);
        user.setCreated(this.created);
        user.setUpdated(this.updated);
        return user;
    }
}
