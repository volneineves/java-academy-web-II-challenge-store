package com.ada.avanadestore.event;

import com.ada.avanadestore.dto.SalesEmailFormDTO;
import com.ada.avanadestore.dto.UserEmailFormDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import static com.ada.avanadestore.constants.Messages.FAILURE_EVENT_PUBLISHED;
import static com.ada.avanadestore.constants.Messages.SUCCESSFULLY_EVENT_PUBLISHED;

@Component
public class EmailPublisher {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailPublisher.class);
    private final ApplicationEventPublisher eventPublisher;

    public EmailPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Async
    public void handleSendEmailEventUser(UserEmailFormDTO dto) {
        try {
            LOGGER.info(SUCCESSFULLY_EVENT_PUBLISHED + "handleSendEmailEvent");
            eventPublisher.publishEvent(dto);
        } catch (Exception exception) {
            LOGGER.error("{}: {}", exception.getClass().getName(), FAILURE_EVENT_PUBLISHED + exception.getMessage());
        }
    }

    @Async
    public void handleSendEmailEventSales(SalesEmailFormDTO dto) {
        try {
            LOGGER.info(SUCCESSFULLY_EVENT_PUBLISHED + "handleSendEmailEventSales");
            eventPublisher.publishEvent(dto);
        } catch (Exception exception) {
            LOGGER.error("{}: {}", exception.getClass().getName(), FAILURE_EVENT_PUBLISHED + exception.getMessage());
        }
    }
}
