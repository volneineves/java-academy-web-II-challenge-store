package com.ada.avanadestore.event;

import com.ada.avanadestore.dto.UpdateProductQuantityDTO;
import com.ada.avanadestore.handler.CustomExceptionHandler;
import com.ada.avanadestore.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import static com.ada.avanadestore.constants.Messages.FAILURE_EVENT_CAPTURE;
import static com.ada.avanadestore.constants.Messages.SUCCESSFULLY_EVENT_CAPTURE;

@Component
public class UpdateProductQuantityConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomExceptionHandler.class);
    private final ProductService productService;

    public UpdateProductQuantityConsumer(ProductService productService) {
        this.productService = productService;
    }

    @Async
    @EventListener
    public void handleUpdateProductQuantityEvent(UpdateProductQuantityDTO dto) {
        try {
            productService.updateQuantity(dto);
            LOGGER.info(SUCCESSFULLY_EVENT_CAPTURE + "handleUpdateProductQuantityEvent");
        } catch (Exception exception) {
            LOGGER.error("{}: {}", exception.getClass().getName(), FAILURE_EVENT_CAPTURE + exception.getMessage());
        }
    }
}
