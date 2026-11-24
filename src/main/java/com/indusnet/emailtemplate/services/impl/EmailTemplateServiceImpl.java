package com.indusnet.emailtemplate.services.impl;

import com.indusnet.emailtemplate.dto.GenerateEmailRequest;
import com.indusnet.emailtemplate.dto.GenerateEmailResponse;
import com.indusnet.emailtemplate.services.EmailGenerationProvider;
import com.indusnet.emailtemplate.services.EmailTemplateService;
import org.springframework.stereotype.Service;

@Service
public class EmailTemplateServiceImpl implements EmailTemplateService {

    private final EmailGenerationProvider emailGenerationProvider;

    public EmailTemplateServiceImpl(EmailGenerationProvider emailGenerationProvider) {
        this.emailGenerationProvider = emailGenerationProvider;
    }

    public GenerateEmailResponse generate(GenerateEmailRequest request) {
        long startedAt = System.nanoTime();
        String template = emailGenerationProvider.generate(request);
        long durationMs = (System.nanoTime() - startedAt) / 1_000_000;
        return new GenerateEmailResponse(request.recipientName().trim(), template, durationMs);
    }
}
