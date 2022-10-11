package com.sergax.courseapi.model.course;

import com.sergax.courseapi.model.user.User;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "courses")
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "logo_url")
    private String logoUrl;
    @Column(name = "movieUrl")
    private String movieUrl;
    @Column(name = "date_start")
    private LocalDate dateStart;
    @Column(name = "date_end")
    private LocalDate dateEnd;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CourseStatus courseStatus;

    @ManyToMany
    @JoinTable(
            name = "courses_mentors",
            joinColumns = @JoinColumn(name = "course_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "mentor_id", referencedColumnName = "id")
    )
    private Set<User> mentors = new HashSet<>();

    @OneToMany(mappedBy = "course",
            cascade = CascadeType.ALL)
    private Set<Content> contents = new HashSet<>();

    @OneToMany(mappedBy = "course")
    private Set<CourseInformation> coursesInformation = new HashSet<>();
}
