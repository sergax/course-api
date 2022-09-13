package com.sergax.courseapi.dto;

import com.sergax.courseapi.model.user.User;
import com.sergax.courseapi.model.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserDto extends BaseEntityDto {
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private LocalDate created;
    private LocalDate updated;
    private Status status;
    private List<RoleDto> roles;
    private List<CourseDto> courses;

    public UserDto(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.created = user.getCreated();
        this.updated = user.getUpdated();
        this.status = user.getStatus();
        if (this.roles == null) {
            this.roles = new ArrayList<>();
        } else {
            this.roles = user.getRoles().stream()
                    .map(RoleDto::new)
                    .collect(Collectors.toList());
        }
        if (this.courses == null) {
            this.courses = new ArrayList<>();
        } else {
            this.courses = user.getCourses().stream()
                    .map(CourseDto::new)
                    .collect(Collectors.toList());
        }
    }

    public User toUser() {
        var user = new User()
                .setId(this.getId())
                .setFirstName(this.getFirstName())
                .setLastName(this.getLastName())
                .setPassword(this.getPassword())
                .setEmail(this.getEmail())
                .setCreated(this.getCreated())
                .setUpdated(this.getUpdated())
                .setStatus(this.getStatus());
//        if (this.getRoles() == null) {
//            user.setRoles(new ArrayList<>());
//        } else {
//            user.setRoles(this.getRoles().stream()
//                    .map(RoleDto::toRole)
//                    .collect(Collectors.toList()));
//        }
        return user;
    }

}
