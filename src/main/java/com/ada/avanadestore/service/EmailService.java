package com.ada.avanadestore.service;

import com.ada.avanadestore.dto.SalesEmailFormDTO;
import com.ada.avanadestore.dto.UserEmailFormDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${spring.mail.username}")
    private String username;

    private final JavaMailSender emailSender;

    @SuppressWarnings({"all"})
    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendUserEmail(UserEmailFormDTO emailForm) {
        SimpleMailMessage message = createEmailMessage(emailForm.to(), emailForm.subject(), emailForm.message());
        emailSender.send(message);
    }

    public void sendSalesManagerEmail(SalesEmailFormDTO emailForm) {
        SimpleMailMessage userMessage = createEmailMessage(emailForm.to(), emailForm.subject(), emailForm.message());
        SimpleMailMessage managerMessage = createEmailMessage(emailForm);
        emailSender.send(userMessage);
        emailSender.send(managerMessage);
    }

    private SimpleMailMessage createEmailMessage(SalesEmailFormDTO emailForm) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailForm.to());
        message.setSubject("Sales");
        message.setText("Customer message sended to " + emailForm.to() + ": "  + emailForm.message());
        message.setFrom(username);
        return message;
    }

    private SimpleMailMessage createEmailMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom(username);
        return message;
    }


}
