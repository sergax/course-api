package com.sergax.courseapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentProgressDto {
    private Long courseId;
    private Long studentId;
    private Double progress;
}
