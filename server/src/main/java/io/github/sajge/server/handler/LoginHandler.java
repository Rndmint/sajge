package io.github.sajge.server.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sajge.dto.LoginDto;
import io.github.sajge.message.Message;
import io.github.sajge.services.LoginService;
import io.github.sajge.logger.Logger;

public class LoginHandler implements Handler {
    private static final Logger logger = Logger.get(LoginHandler.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final LoginService loginService;

    public LoginHandler(LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    public String handle(Message msg) {
        try {
            LoginDto dto = objectMapper.treeToValue(msg.getPayload(), LoginDto.class);
            boolean success = loginService.authenticate(dto.username(), dto.password());
            return success ? "Login successful" : "Login failed";
        } catch (Exception e) {
            logger.error("Error handling login", e);
            return "Login failed";
        }
    }
}
