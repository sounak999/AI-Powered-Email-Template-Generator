package com.indusnet.emailtemplate.controllers;

import com.indusnet.emailtemplate.dto.GenerateEmailRequest;
import com.indusnet.emailtemplate.dto.GenerateEmailResponse;
import com.indusnet.emailtemplate.services.EmailTemplateService;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

class EmailTemplateControllerTest {

    @Test
    void delegatesRequestToEmailTemplateServiceAndReturnsResponse() {
        GenerateEmailRequest request = new GenerateEmailRequest("Thank the customer", "Avery", "friendly");
        GenerateEmailResponse expected = new GenerateEmailResponse("Avery", "Subject: Thanks\n\nHi Avery,", 120);
        AtomicReference<GenerateEmailRequest> receivedRequest = new AtomicReference<>();
        EmailTemplateService service = received -> {
            receivedRequest.set(received);
            return expected;
        };
        EmailTemplateController controller = new EmailTemplateController(service);

        GenerateEmailResponse actual = controller.generate(request);

        assertThat(receivedRequest).hasValue(request);
        assertThat(actual).isSameAs(expected);
    }
}
