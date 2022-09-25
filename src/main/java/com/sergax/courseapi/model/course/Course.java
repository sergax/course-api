package com.sergax.courseapi.model.course;

import com.sergax.courseapi.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "courses_mentors",
            joinColumns = @JoinColumn(name = "course_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "mentor_id", referencedColumnName = "id")
    )
    private List<User> mentors;

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<Content> contents;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "course")
    private List<CourseInformation> coursesInformation;
}
