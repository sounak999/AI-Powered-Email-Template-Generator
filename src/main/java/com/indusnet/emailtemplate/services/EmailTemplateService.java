package com.indusnet.emailtemplate.services;

import com.indusnet.emailtemplate.dto.GenerateEmailRequest;
import com.indusnet.emailtemplate.dto.GenerateEmailResponse;

public interface EmailTemplateService {

    GenerateEmailResponse generate(GenerateEmailRequest request);
}
