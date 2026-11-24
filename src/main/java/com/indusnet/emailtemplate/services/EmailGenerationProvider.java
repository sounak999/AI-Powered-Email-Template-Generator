package com.indusnet.emailtemplate.services;

import com.indusnet.emailtemplate.dto.GenerateEmailRequest;

public interface EmailGenerationProvider {

    String generate(GenerateEmailRequest request);
}
