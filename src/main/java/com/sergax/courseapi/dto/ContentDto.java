package com.sergax.courseapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sergax.courseapi.model.course.Content;
import com.sergax.courseapi.model.course.Course;
import com.sergax.courseapi.model.course.TypeContent;
import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper=false)
public class ContentDto extends BaseEntityDto {
    @NotNull
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