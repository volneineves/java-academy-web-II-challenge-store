package com.ada.avanadestore.service;

import com.ada.avanadestore.dto.ProductDTO;
import com.ada.avanadestore.dto.ProductFilterDTO;
import com.ada.avanadestore.dto.UpdateProductQuantityDTO;
import com.ada.avanadestore.entitity.Product;
import com.ada.avanadestore.exception.BadRequestException;
import com.ada.avanadestore.exception.DatabaseException;
import com.ada.avanadestore.exception.InternalServerException;
import com.ada.avanadestore.exception.ResourceNotFoundException;
import com.ada.avanadestore.repository.ProductFilterRepository;
import com.ada.avanadestore.repository.ProductRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.ada.avanadestore.constants.Messages.*;

@Service
public class ProductService {

    private final ProductFilterRepository filterRepository;
    private final ProductRepository repository;

    public ProductService(ProductFilterRepository filterRepository, ProductRepository repository) {
        this.filterRepository = filterRepository;
        this.repository = repository;
    }

    public Product getById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(PRODUCT_NOT_FOUND));
    }

    public List<ProductDTO> findByFilter(ProductFilterDTO dto) {
        return filterRepository.findByFilter(dto).stream().map(Product::toDTO).toList();
    }

    public void updateQuantity(UpdateProductQuantityDTO dto) {
        Product product = getById(dto.id());
        Integer stockTotal = product.getStock() - dto.quantityDecrease();
        product.setStock(stockTotal);
        trySaverOrThrowError(product);
    }

    private void trySaverOrThrowError(Product product) {
        try {
            repository.save(product);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage(), e.getRootCause());
        } catch (Exception e) {
            throw new InternalServerException(INTERNAL_SERVER_ERROR);
        }
    }
}
