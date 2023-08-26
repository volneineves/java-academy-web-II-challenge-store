package com.ada.avanadestore.service;

import com.ada.avanadestore.dto.ProductDTO;
import com.ada.avanadestore.dto.ProductFilterDTO;
import com.ada.avanadestore.entitity.Product;
import com.ada.avanadestore.repository.ProductFilterRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductFilterRepository filterRepository;

    public ProductService(ProductFilterRepository filterRepository) {
        this.filterRepository = filterRepository;
    }

    public List<ProductDTO> findByFilter(ProductFilterDTO dto) {
        return filterRepository.findByFilter(dto).stream().map(Product::toDTO).toList();
    }
}
