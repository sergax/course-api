package com.sergax.courseapi.service;


import com.sergax.courseapi.dto.ContentInformationDto;

public interface ContentInformationService {
    ContentInformationDto passedContentByStudent(Long contentId, ContentInformationDto contentInformationDto, String studentEmail);
}
