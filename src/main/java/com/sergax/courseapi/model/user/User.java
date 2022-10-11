package com.sergax.courseapi.model.user;

import com.sergax.courseapi.model.Status;
import com.sergax.courseapi.model.course.ContentInformation;
import com.sergax.courseapi.model.course.Course;
import com.sergax.courseapi.model.course.CourseInformation;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.jpa.repository.EntityGraph;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
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

    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<Role> roles;

    @ManyToMany(mappedBy = "mentors")
    private List<Course> courses = new ArrayList<>();

    @OneToMany(mappedBy = "student")
    private List<CourseInformation> coursesInformation = new ArrayList<>();

    @OneToMany(mappedBy = "student")
    private List<ContentInformation> contentsInformation = new ArrayList<>();
}

