package com.sergax.courseapi.model.course;

import com.sergax.courseapi.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "courses_students")
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CourseInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "date_registered")
    private LocalDate dateRegistered;
    @Column(name = "comments")
    private String comments;
    @Column(name = "likes", columnDefinition = "0")
    private boolean likes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private User student;
}
