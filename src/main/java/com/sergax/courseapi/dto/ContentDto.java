package com.sergax.courseapi.dto;

import com.sergax.courseapi.model.course.Content;
import com.sergax.courseapi.model.course.TypeContent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ContentDto extends BaseEntityDto {
    private String name;
    private String text;
    private String movie_url;
    private TypeContent typeContent;
    private CourseDto courseDto;

    public ContentDto(Content content) {
        this.id = content.getId();
        this.name = content.getName();
        this.text = content.getText();
        this.movie_url = content.getMovie_url();
        this.typeContent = content.getTypeContent();
        this.courseDto = new CourseDto(content.getCourse());
    }

    public Content toContent () {
       return new Content()
               .setId(this.getId())
               .setName(this.getName())
               .setText(this.getText())
               .setMovie_url(this.getMovie_url())
               .setTypeContent(this.getTypeContent());
    }
}