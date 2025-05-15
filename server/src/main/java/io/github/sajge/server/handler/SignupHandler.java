package io.github.sajge.server.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sajge.dto.SignupDto;
import io.github.sajge.message.Message;
import io.github.sajge.services.SignupService;
import io.github.sajge.logger.Logger;

public class SignupHandler implements Handler {
    private static final Logger logger = Logger.get(SignupHandler.class);

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final SignupService signupService;

    public SignupHandler(SignupService signupService) {
        this.signupService = signupService;
    }

    @Override
    public String handle(Message msg) {
        try {
            SignupDto signupDto = objectMapper.treeToValue(msg.getPayload(), SignupDto.class);
            signupService.create(signupDto.username(), signupDto.password());
            return "Signup successful";
        } catch (Exception e) {
            logger.error("Error handling signup", e);
            return "Signup failed";
        }
    }
}
