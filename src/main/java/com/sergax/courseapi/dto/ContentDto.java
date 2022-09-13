package com.sergax.courseapi.dto;

import com.sergax.courseapi.model.BaseEntity;
import com.sergax.courseapi.model.course.Content;
import com.sergax.courseapi.model.course.TypeContent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContentDto extends BaseEntity {
    private String name;
    private String text;
    private String movie_url;
    private TypeContent typeContent;

    public ContentDto(Content content) {
        this.id = content.getId();
        this.name = content.getName();
        this.text = content.getText();
        this.movie_url = content.getMovie_url();
        this.typeContent = content.getTypeContent();
    }
}