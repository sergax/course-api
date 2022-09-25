package com.sergax.courseapi.model.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "contents_students",
            joinColumns = @JoinColumn(name = "content_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "student_id", referencedColumnName = "id")
    )
    private List<Content> contents;
}
