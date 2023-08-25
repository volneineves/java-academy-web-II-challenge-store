package com.ada.avanadestore.runner;

import com.ada.avanadestore.dto.ProductDTO;
import com.ada.avanadestore.entitity.Product;
import com.ada.avanadestore.repository.ProductRepository;
import com.ada.avanadestore.restclient.DummyProductsHttpClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ProductsRunner implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductsRunner.class);
    private final ProductRepository repository;
    private final DummyProductsHttpClient dummyProductsClient;

    private final ObjectMapper objectMapper;

    public ProductsRunner(ProductRepository repository, DummyProductsHttpClient dummyProductsClient, ObjectMapper objectMapper) {
        this.repository = repository;
        this.dummyProductsClient = dummyProductsClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(String... args) throws Exception {
        if (isRepositoryEmpty()) {
            try {
                List<Product> products = fetchAndTransformProducts();
                repository.saveAll(products);
            } catch (Exception exception) {
                LOGGER.warn("Exception: {}", exception.getMessage(), exception);
            }
        }
    }

    private boolean isRepositoryEmpty() {
        return repository.count() <= 0;
    }

    private List<Product> fetchAndTransformProducts() {
        Map<String, Object> responseMap = dummyProductsClient.getProducts();
        List<Map<String, ProductDTO>> productMaps = (List<Map<String, ProductDTO>>) responseMap.get("products");

        return productMaps.stream()
                .map(map -> objectMapper.convertValue(map, ProductDTO.class))
                .map(Product::new)
                .toList();
    }
}

