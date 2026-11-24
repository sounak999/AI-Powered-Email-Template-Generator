package com.indusnet.emailtemplate.advices;

import com.indusnet.emailtemplate.dto.ApiError;
import com.indusnet.emailtemplate.exceptions.AiProviderException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void mapsAiProviderFailureToBadGatewayResponse() {
        ResponseEntity<?> response = handler.handleProviderFailure(
                new AiProviderException("AI provider is unavailable."));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_GATEWAY);
        assertThat(response.getBody()).isInstanceOfSatisfying(
                ApiError.class,
                error -> {
                    assertThat(error.status()).isEqualTo(HttpStatus.BAD_GATEWAY.value());
                    assertThat(error.message()).isEqualTo("AI provider is unavailable.");
                    assertThat(error.details()).isEmpty();
                });
    }
}
