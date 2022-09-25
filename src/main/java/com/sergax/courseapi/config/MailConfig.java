package com.sergax.courseapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;

@Configuration
public class MailConfig {
    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.properties.mail.smtp.auth}")
    private String auth;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private String enable;

    @Value("${spring.mail.properties.mail.smtp.starttls.required}")
    private String required;

    @Value("${spring.mail.properties.mail.smtp.ssl.protocols}")
    private String protocol;

    @Value("${spring.mail.properties.mail.smtp.socketFactory.class}")
    private String socketFactory;

    @Bean
    public Session javaMailSession() {

        var properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", auth);
        properties.put("mail.smtp.starttls.enable", enable);
        properties.put("mail.smtp.starttls.required", required);
        properties.put("mail.smtp.ssl.protocols", protocol);
        properties.put("mail.smtp.socketFactory.class", socketFactory);

        return Session.getInstance(properties, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication(username, password);

            }
        });
    }

    @Bean
    public SimpleMailMessage templateSimpleMessage() {
        var message = new SimpleMailMessage();
        message.setText(
                "Hi from Course-client ✋ \n\n%s\n");
        return message;
    }
}
