package com.ada.avanadestore.dto;

public record StandardErrorDTO(Integer status, String message, String path, String method, String timestamp) {
}
