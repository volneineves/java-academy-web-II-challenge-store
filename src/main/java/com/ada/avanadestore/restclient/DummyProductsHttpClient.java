package com.ada.avanadestore.restclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@FeignClient(name = "dummyProduct", url = "${dummyProduct.url}")
public interface DummyProductsHttpClient {

    @GetMapping
    Map<String, Object> getProducts();
}
