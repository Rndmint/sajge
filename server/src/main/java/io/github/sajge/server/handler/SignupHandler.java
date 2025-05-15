package io.github.sajge.server.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sajge.dto.SignupDto;
import io.github.sajge.message.Message;
import io.github.sajge.core.services.SignupService;
import io.github.sajge.message.Request;

public class SignupHandler implements Handler {
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final SignupService signupService;

    private final SignupDto signupDto;

    public SignupHandler(SignupService signupService) {
        this.signupService = signupService;
        this.signupDto = new SignupDto();
    }

    @Override
    public String handle(Message msg) {
        return "";
    }

}
