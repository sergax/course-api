package com.sergax.courseapi.dto;

import com.sergax.courseapi.model.course.ContentInformation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ContentInformationDto extends BaseEntityDto {
    private boolean passed;
    private Long contentId;
    private Long studentId;

    public ContentInformationDto(ContentInformation contentInformation) {
        this.id = contentInformation.getId();
        this.passed = contentInformation.isPassed();
        this.contentId = contentInformation.getContent().getId();
        this.studentId = contentInformation.getStudent().getId();
    }

    public ContentInformation toContentInformation() {
        return new ContentInformation()
                .setId(this.id)
                .setPassed(this.passed);
    }
}