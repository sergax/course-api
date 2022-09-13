package com.sergax.courseapi.dto;

import com.sergax.courseapi.model.Status;
import com.sergax.courseapi.model.course.Course;
import com.sergax.courseapi.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CourseDto extends BaseEntityDto {
    private String name;
    private String description;
    private String logoUrl;
    private String movie_url;
    private LocalDate dateStart;
    private LocalDate dateEnd;
    private Status status;
    private List<UserDto> mentors;
    private List<ContentDto> contents;

    public CourseDto(Course course) {
        this.id = course.getId();
        this.name = course.getName();
        this.description = course.getDescription();
        this.logoUrl = course.getLogoUrl();
        this.movie_url = course.getMovie_url();
        this.dateStart = course.getDateStart();
        this.dateEnd = course.getDateEnd();
        this.status = course.getStatus();
        this.mentors = course.getMentors().stream()
                .map(UserDto::new)
                .collect(Collectors.toList());
        this.contents = course.getContents().stream()
                .map(ContentDto::new)
                .collect(Collectors.toList());
    }

}