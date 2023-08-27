package com.ada.avanadestore.event;

import com.ada.avanadestore.dto.UpdateProductQuantityDTO;
import com.ada.avanadestore.handler.CustomExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import static com.ada.avanadestore.constants.ErrorMessages.FAILURE_EVENT_PUBLISHED;
import static com.ada.avanadestore.constants.ErrorMessages.SUCCESSFULLY_EVENT_PUBLISHED;

@Component
public class UpdateProductQuantityPublisher {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomExceptionHandler.class);
    private final ApplicationEventPublisher eventPublisher;

    public UpdateProductQuantityPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void handleUpdateProductQuantityEvent(UpdateProductQuantityDTO dto) {
        try {
            LOGGER.info(SUCCESSFULLY_EVENT_PUBLISHED + "handleUpdateProductQuantityEvent");
            eventPublisher.publishEvent(dto);
        } catch (Exception exception) {
            LOGGER.error("{}: {}", exception.getClass().getName(), FAILURE_EVENT_PUBLISHED + exception.getMessage());
        }
    }
}
