package com.ada.avanadestore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.security.web.SecurityFilterChain;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class AvanadeStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(AvanadeStoreApplication.class, args);
    }

}
