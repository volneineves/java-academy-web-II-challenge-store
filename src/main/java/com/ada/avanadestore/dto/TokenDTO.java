package com.ada.avanadestore.dto;

import java.util.UUID;

public record TokenDTO(UUID user, String userType, String type, String token) {
}
