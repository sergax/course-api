package com.sergax.courseapi.model.course;

import com.sergax.courseapi.model.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Table(name = "courses_contents")
public class Content extends BaseEntity {
    @Column(name = "name")
    private String name;
    @Column(name = "text")
    private String text;
    @Column(name = "movie_url")
    private String movie_url;
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private TypeContent typeContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;
}
