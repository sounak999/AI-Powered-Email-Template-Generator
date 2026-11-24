package com.indusnet.emailtemplate.services.impl;

import com.indusnet.emailtemplate.dto.GenerateEmailRequest;
import com.indusnet.emailtemplate.dto.GenerateEmailResponse;
import com.indusnet.emailtemplate.services.EmailGenerationProvider;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

class EmailTemplateServiceImplTest {

    @Test
    void returnsGeneratedTemplateAndElapsedTime() {
        GenerateEmailRequest request = new GenerateEmailRequest("Confirm resolution", " Avery ", "warm");
        AtomicReference<GenerateEmailRequest> receivedRequest = new AtomicReference<>();
        EmailGenerationProvider provider = received -> {
            receivedRequest.set(received);
            return "Subject: Resolved\n\nHi Avery,";
        };
        EmailTemplateServiceImpl service = new EmailTemplateServiceImpl(provider);

        GenerateEmailResponse response = service.generate(request);

        assertThat(receivedRequest).hasValue(request);
        assertThat(response.recipientName()).isEqualTo("Avery");
        assertThat(response.emailTemplate()).isEqualTo("Subject: Resolved\n\nHi Avery,");
        assertThat(response.generationTimeMs()).isGreaterThanOrEqualTo(0);
    }
}
