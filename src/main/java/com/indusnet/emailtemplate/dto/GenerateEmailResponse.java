package com.indusnet.emailtemplate.dto;

public record GenerateEmailResponse(String recipientName, String emailTemplate, long generationTimeMs) {
}
