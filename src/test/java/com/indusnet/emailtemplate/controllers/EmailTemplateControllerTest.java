package com.indusnet.emailtemplate.controllers;

import com.indusnet.emailtemplate.dto.GenerateEmailRequest;
import com.indusnet.emailtemplate.dto.GenerateEmailResponse;
import com.indusnet.emailtemplate.advices.GlobalExceptionHandler;
import com.indusnet.emailtemplate.exceptions.AiProviderException;
import com.indusnet.emailtemplate.services.EmailTemplateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmailTemplateController.class)
@Import({GlobalExceptionHandler.class, EmailTemplateControllerTest.StubConfig.class})
class EmailTemplateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StubEmailTemplateService emailTemplateService;

    @BeforeEach
    void resetServiceStub() {
        emailTemplateService.setResponse(
                new GenerateEmailResponse("Avery", "Subject: Thanks\n\nHi Avery,", 120));
        emailTemplateService.setFailure(null);
    }

    @Test
    void generatesTemplateFromValidRequest() throws Exception {
        mockMvc.perform(post("/api/v1/email-templates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"purpose":"Thank the customer","recipient_name":"Avery","tone":"friendly"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recipientName").value("Avery"))
                .andExpect(jsonPath("$.emailTemplate").value("Subject: Thanks\n\nHi Avery,"))
                .andExpect(jsonPath("$.generationTimeMs").value(120));
    }

    @Test
    void rejectsMissingRequiredFields() throws Exception {
        mockMvc.perform(post("/api/v1/email-templates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"purpose\":\"Thank the customer\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details.recipientName").value("recipient_name is required"))
                .andExpect(jsonPath("$.details.tone").value("tone is required"));
    }

    @Test
    void returnsBadGatewayWhenAiProviderFails() throws Exception {
        emailTemplateService.setFailure(new AiProviderException("Gemini could not generate an email."));

        mockMvc.perform(post("/api/v1/email-templates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"purpose":"Thank the customer","recipient_name":"Avery","tone":"friendly"}
                                """))
                .andExpect(status().isBadGateway())
                .andExpect(jsonPath("$.status").value(502))
                .andExpect(jsonPath("$.error").value("Bad Gateway"))
                .andExpect(jsonPath("$.message").value("Gemini could not generate an email."));
    }

    @TestConfiguration(proxyBeanMethods = false)
    static class StubConfig {

        @Bean
        StubEmailTemplateService emailTemplateService() {
            return new StubEmailTemplateService();
        }
    }

    static class StubEmailTemplateService implements EmailTemplateService {

        private GenerateEmailResponse response;
        private RuntimeException failure;

        @Override
        public GenerateEmailResponse generate(GenerateEmailRequest request) {
            if (failure != null) {
                throw failure;
            }
            return response;
        }

        void setResponse(GenerateEmailResponse response) {
            this.response = response;
        }

        void setFailure(RuntimeException failure) {
            this.failure = failure;
        }
    }
}
