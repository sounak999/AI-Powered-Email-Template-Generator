package com.indusnet.emailtemplate.controllers;

import com.indusnet.emailtemplate.dto.GenerateEmailRequest;
import com.indusnet.emailtemplate.dto.GenerateEmailResponse;
import com.indusnet.emailtemplate.services.EmailTemplateService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/email-templates", produces = MediaType.APPLICATION_JSON_VALUE)
public class EmailTemplateController {

    private final EmailTemplateService emailTemplateService;

    public EmailTemplateController(EmailTemplateService emailTemplateService) {
        this.emailTemplateService = emailTemplateService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public GenerateEmailResponse generate(@Valid @RequestBody GenerateEmailRequest request) {
        return emailTemplateService.generate(request);
    }
}
