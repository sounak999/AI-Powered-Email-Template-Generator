package com.indusnet.emailtemplate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonProperty;

public record GenerateEmailRequest(
        @NotBlank(message = "purpose is required")
        @Size(max = 500, message = "purpose must be at most 500 characters")
        String purpose,

        @JsonProperty("recipient_name")
        @NotBlank(message = "recipient_name is required")
        @Size(max = 100, message = "recipient_name must be at most 100 characters")
        String recipientName,

        @NotBlank(message = "tone is required")
        @Size(max = 100, message = "tone must be at most 100 characters")
        String tone) {

}
