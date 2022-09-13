package com.sergax.courseapi.dto;

import lombok.Data;

import javax.persistence.MappedSuperclass;

@Data
public class BaseEntityDto {
    protected Long id;
}
