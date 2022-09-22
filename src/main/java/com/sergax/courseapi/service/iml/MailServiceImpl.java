package com.sergax.courseapi.service.iml;

import com.sergax.courseapi.service.MailService;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
    private final Session mailSession;
    @Value("${spring.mail.username}")
    private String username;

    @SneakyThrows
    @Override
    public void send(String receiver, String subject, String message) {
        var mailMessage = new MimeMessage(mailSession);
        mailMessage.setFrom(new InternetAddress(username));
        mailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        Transport.send(mailMessage);
    }
}
