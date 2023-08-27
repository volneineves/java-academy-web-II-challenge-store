package com.ada.avanadestore.event;

import com.ada.avanadestore.dto.EmailFormDTO;
import com.ada.avanadestore.dto.UpdateProductQuantityDTO;
import com.ada.avanadestore.handler.CustomExceptionHandler;
import com.ada.avanadestore.service.EmailService;
import com.ada.avanadestore.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import static com.ada.avanadestore.constants.ErrorMessages.FAILURE_EVENT_CAPTURE;
import static com.ada.avanadestore.constants.ErrorMessages.SUCCESSFULLY_EVENT_CAPTURE;

@Component
public class EmailConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomExceptionHandler.class);
    private final EmailService emailService;

    public EmailConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @Async
    @EventListener
    public void handleSendEmailEvent(EmailFormDTO dto) {
        try {
            emailService.sendEmail(dto);
            LOGGER.info(SUCCESSFULLY_EVENT_CAPTURE + "handleSendEmailEvent");
        } catch (Exception exception) {
            LOGGER.error("{}: {}", exception.getClass().getName(), FAILURE_EVENT_CAPTURE + exception.getMessage());
        }
    }
}
