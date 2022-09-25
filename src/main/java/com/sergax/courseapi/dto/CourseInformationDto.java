package com.sergax.courseapi.dto;

import com.sergax.courseapi.model.course.CourseInformation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class CourseInformationDto extends BaseEntityDto {
    private LocalDate dateRegistered;
    private String comments;
    private boolean likes;
    private Long courseId;
    private Long studentId;

    public CourseInformationDto(CourseInformation courseInformation) {
        this.id = courseInformation.getId();
        this.dateRegistered = courseInformation.getDateRegistered();
        this.comments = courseInformation.getComments();
        this.likes = courseInformation.isLikes();
        this.courseId = courseInformation.getCourse().getId();
        this.studentId = courseInformation.getStudent().getId();
    }

    public CourseInformation toCourseInformation() {
        return new CourseInformation().setId(this.id)
                .setDateRegistered(this.dateRegistered)
                .setLikes(this.likes)
                .setComments(this.comments);
    }
}