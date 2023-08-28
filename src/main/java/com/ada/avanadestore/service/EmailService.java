package com.ada.avanadestore.service;

import com.ada.avanadestore.dto.EmailFormDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${spring.mail.username}")
    private String username;

    private final JavaMailSender emailSender;

    @SuppressWarnings("all")
    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendEmail(EmailFormDTO dto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(dto.to());
        message.setSubject(dto.subject());
        message.setText(dto.message());
        message.setFrom(username);
        emailSender.send(message);
    }
}
