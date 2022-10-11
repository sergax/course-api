package com.sergax.courseapi.model.course;

import com.sergax.courseapi.model.user.User;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.List;
@Data
@Entity
@Table(name = "contents_students")
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ContentInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "passed", columnDefinition = "0")
    private boolean passed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id")
    private Content content;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;
}
