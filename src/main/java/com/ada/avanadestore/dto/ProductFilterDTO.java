package com.ada.avanadestore.dto;

import java.math.BigDecimal;

public record ProductFilterDTO(
        Boolean isAvailable,
        String category,
        String brand,
        Double minRating,
        Double maxRating,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        Double minDiscount,
        Double maxDiscount
) {


}
