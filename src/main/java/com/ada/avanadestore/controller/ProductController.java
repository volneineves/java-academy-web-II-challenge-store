package com.ada.avanadestore.controller;

import com.ada.avanadestore.dto.ProductDTO;
import com.ada.avanadestore.dto.ProductFilterDTO;
import com.ada.avanadestore.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getByFilter(
            @RequestParam(required = false) Boolean isAvailable,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) Double maxRating,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Double minDiscount,
            @RequestParam(required = false) Double maxDiscount
    ) {
        ProductFilterDTO filter = new ProductFilterDTO(isAvailable, category, brand, minRating, maxRating, minPrice, maxPrice, minDiscount, maxDiscount);
        List<ProductDTO> productDTOList = service.findByFilter(filter);
        return ResponseEntity.ok(productDTOList);
    }

}
