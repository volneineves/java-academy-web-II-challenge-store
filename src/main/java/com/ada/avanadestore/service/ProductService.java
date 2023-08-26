package com.ada.avanadestore.service;

import com.ada.avanadestore.dto.ProductDTO;
import com.ada.avanadestore.dto.ProductFilterDTO;
import com.ada.avanadestore.entitity.Product;
import com.ada.avanadestore.exception.ResourceNotFound;
import com.ada.avanadestore.repository.ProductFilterRepository;
import com.ada.avanadestore.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.ada.avanadestore.constants.ErrorMessages.PRODUCT_NOT_FOUND;

@Service
public class ProductService {

    private final ProductFilterRepository filterRepository;
    private final ProductRepository repository;

    public ProductService(ProductFilterRepository filterRepository, ProductRepository repository) {
        this.filterRepository = filterRepository;
        this.repository = repository;
    }

    public Product getById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFound(PRODUCT_NOT_FOUND));
    }

    public List<ProductDTO> findByFilter(ProductFilterDTO dto) {
        return filterRepository.findByFilter(dto).stream().map(Product::toDTO).toList();
    }
}
