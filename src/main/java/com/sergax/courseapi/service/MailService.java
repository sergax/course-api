package com.sergax.courseapi.service;

public interface MailService {
    void send(String receiver, String subject, String message);
}
