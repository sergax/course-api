package com.sergax.courseapi.model.course;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Table(name = "courses_contents")
public class Content {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "text")
    private String text;
    @Column(name = "movieUrl")
    private String movieUrl;
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private TypeContent typeContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @OneToMany(mappedBy = "content", fetch = FetchType.LAZY)
    private List<ContentInformation> contentsInformation;
}
