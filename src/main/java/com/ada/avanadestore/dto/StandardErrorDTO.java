package com.ada.avanadestore.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public record StandardErrorDTO(Integer status, String message, String path, String method, String timestamp) {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String toString() {
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "StandardErrorDTO{" +
                    "status=" + status +
                    ", message='" + message + '\'' +
                    ", path='" + path + '\'' +
                    ", method='" + method + '\'' +
                    ", timestamp='" + timestamp + '\'' +
                    '}';
        }
    }
}
