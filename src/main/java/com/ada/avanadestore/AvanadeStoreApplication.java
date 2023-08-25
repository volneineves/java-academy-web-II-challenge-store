package com.ada.avanadestore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AvanadeStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(AvanadeStoreApplication.class, args);
    }

}
