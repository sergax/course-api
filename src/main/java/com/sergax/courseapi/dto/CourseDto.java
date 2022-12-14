package com.sergax.courseapi.dto;

import com.sergax.courseapi.model.course.Course;
import com.sergax.courseapi.model.course.CourseInformation;
import com.sergax.courseapi.model.course.CourseStatus;
import com.sergax.courseapi.model.user.User;
import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper=false)
public class CourseDto extends BaseEntityDto {
    private String name;
    private String description;
    private String logoUrl;
    private String movieUrl;
    private LocalDate dateStart;
    private LocalDate dateEnd;
    @NotNull(message = "PUBLIC or PRIVATE")
    private CourseStatus status;
    private Set<Long> mentorsId;
    private Set<ContentDto> contents;
    private Set<CourseInformationDto> coursesInformation;

    public CourseDto(Course course) {
        this.id = course.getId();
        this.name = course.getName();
        this.description = course.getDescription();
        this.logoUrl = course.getLogoUrl();
        this.movieUrl = course.getMovieUrl();
        this.dateStart = course.getDateStart();
        this.dateEnd = course.getDateEnd();
        this.status = course.getCourseStatus();
        if (course.getMentors() == null) {
            this.mentorsId = null;
        } else {
            this.mentorsId = course.getMentors().stream()
                    .map(User::getId)
                    .collect(Collectors.toSet());
        }
        if (course.getContents() == null) {
            this.contents = null;
        } else {
            this.contents = course.getContents().stream()
                    .map(ContentDto::new)
                    .collect(Collectors.toSet());
        }
        if (course.getCoursesInformation() == null) {
            this.coursesInformation = null;
        } else {
            this.coursesInformation = course.getCoursesInformation().stream()
                    .map(CourseInformationDto::new)
                    .collect(Collectors.toSet());
        }
    }

    public Course toCourse() {
        return new Course()
                .setId(this.getId())
                .setName(this.getName())
                .setDescription(this.getDescription())
                .setLogoUrl(this.getLogoUrl())
                .setMovieUrl(this.getMovieUrl())
                .setDateStart(this.getDateStart())
                .setDateEnd(this.getDateEnd())
                .setCourseStatus(this.getStatus());
    }

}