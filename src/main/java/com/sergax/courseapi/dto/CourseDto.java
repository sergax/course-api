package com.sergax.courseapi.dto;

import com.sergax.courseapi.model.Status;
import com.sergax.courseapi.model.course.Course;
import com.sergax.courseapi.model.course.CourseStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.ArrayList;
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
    private String movieUrl;
    private LocalDate dateStart;
    private LocalDate dateEnd;
    private CourseStatus status;
    private List<UserDto> mentors;
    private List<ContentDto> contents;

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
            course.setMentors(new ArrayList<>());
        } else {
            this.mentors = course.getMentors().stream()
                    .map(UserDto::new)
                    .collect(Collectors.toList());
        }
        if (course.getContents() == null) {
            course.setContents(new ArrayList<>());
        } else {
            this.contents = course.getContents().stream()
                    .map(ContentDto::new)
                    .collect(Collectors.toList());
        }
    }

    public Course toCourse() {
        //        if (this.getMentors() == null) {
//            this.setMentors(new ArrayList<>());
//        } else {
//            course.setMentors(this.getMentors().stream()
//                    .map(UserDto::toUser)
//                    .collect(Collectors.toList()));
//        }
//        if (this.getContents() == null) {
//            this.setContents(new ArrayList<>());
//        } else {
//            course.setContents(this.getContents().stream()
//                    .map(ContentDto::toContent)
//                    .collect(Collectors.toList()));
//        }
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