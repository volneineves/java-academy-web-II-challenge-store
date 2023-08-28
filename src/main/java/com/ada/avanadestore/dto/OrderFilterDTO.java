package com.ada.avanadestore.dto;

import com.ada.avanadestore.enums.OrderStatus;
import com.ada.avanadestore.util.JavaUtil;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

public record OrderFilterDTO(UUID customerId, LocalDate minDate, LocalDate maxDate, OrderStatus status) {

    public Date minDateConvertedToDate() {
        return JavaUtil.convertToDate(minDate);
    }

    public Date maxDateConvertedToDate() {
        return JavaUtil.convertToDate(maxDate);
    }
}
