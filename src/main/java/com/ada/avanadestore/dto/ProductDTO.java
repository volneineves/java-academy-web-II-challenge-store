package com.ada.avanadestore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

public record ProductDTO(

        @JsonProperty(access = JsonProperty.Access.READ_ONLY) UUID id,
        String title,
        String description,
        BigDecimal price,
        Double discountPercentage,
        Double rating,
        Integer stock,
        String brand,
        String category,
        String thumbnail,
        Set<String> images) {
}

