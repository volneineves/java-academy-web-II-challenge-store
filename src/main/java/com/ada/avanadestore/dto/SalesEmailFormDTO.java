package com.ada.avanadestore.dto;

import java.util.List;

public record SalesEmailFormDTO(String to, List<String> replyTo, String subject, String message) {
}
