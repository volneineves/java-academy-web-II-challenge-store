package com.ada.avanadestore.event;

import com.ada.avanadestore.dto.SalesEmailFormDTO;
import com.ada.avanadestore.dto.UserEmailFormDTO;
import com.ada.avanadestore.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import static com.ada.avanadestore.constants.Messages.FAILURE_EVENT_CAPTURE;
import static com.ada.avanadestore.constants.Messages.SUCCESSFULLY_EVENT_CAPTURE;

@Component
public class EmailConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailConsumer.class);
    private final EmailService emailService;

    public EmailConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @Async
    @EventListener
    public void handleSendEmailEventUser(UserEmailFormDTO dto) {
        try {
            emailService.sendUserEmail(dto);
            LOGGER.info(SUCCESSFULLY_EVENT_CAPTURE + "handleSendEmailEvent");
        } catch (Exception exception) {
            LOGGER.error("{}: {}", exception.getClass().getName(), FAILURE_EVENT_CAPTURE + exception.getMessage());
        }
    }

    @Async
    @EventListener
    public void handleSendEmailEventSales(SalesEmailFormDTO dto) {
        try {
            emailService.sendSalesManagerEmail(dto);
            LOGGER.info(SUCCESSFULLY_EVENT_CAPTURE + "handleSendEmailEventSales");
        } catch (Exception exception) {
            LOGGER.error("{}: {}", exception.getClass().getName(), FAILURE_EVENT_CAPTURE + exception.getMessage());
        }
    }
}
