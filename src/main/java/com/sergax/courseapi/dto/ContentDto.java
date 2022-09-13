package com.sergax.courseapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sergax.courseapi.model.course.Content;
import com.sergax.courseapi.model.course.Course;
import com.sergax.courseapi.model.course.TypeContent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ContentDto extends BaseEntityDto {
    private String name;
    private String text;
    private String movieUrl;
    private TypeContent typeContent;

    public ContentDto(Content content) {
        this.id = content.getId();
        this.name = content.getName();
        this.text = content.getText();
        this.movieUrl = content.getMovieUrl();
        this.typeContent = content.getTypeContent();
    }

    public Content toContent() {
        return new Content()
                .setId(this.getId())
                .setName(this.getName())
                .setText(this.getText())
                .setMovieUrl(this.getMovieUrl())
                .setTypeContent(this.getTypeContent());
    }

}