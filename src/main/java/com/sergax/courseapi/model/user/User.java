package com.sergax.courseapi.model.user;

import com.sergax.courseapi.model.Status;
import com.sergax.courseapi.model.course.Course;
import com.sergax.courseapi.model.course.CourseInformation;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "created")
    private LocalDate created;
    @Column(name = "updated")
    private LocalDate updated;
    @Enumerated(EnumType.STRING)
    @Column(name = "user_status")
    private Status status;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private List<Role> roles;

    @ManyToMany(mappedBy = "mentors", fetch = FetchType.LAZY)
    private List<Course> courses;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "student")
    private List<CourseInformation> coursesInformation;
}

