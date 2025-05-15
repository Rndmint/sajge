package io.github.sajge.server.logins;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sajge.messages.Envelope;
import io.github.sajge.logger.Logger;
import io.github.sajge.messages.responses.ResponseType;
import io.github.sajge.messages.resquests.RequestType;
import io.github.sajge.server.patterns.Handler;

public class LoginHandler implements Handler<Envelope<RequestType,LoginDto>> {
    private static final Logger logger = Logger.get(LoginHandler.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final LoginService loginService;

    public LoginHandler(LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    public String handle(Envelope<RequestType,LoginDto> msg) {
        LoginDto dto = msg.getPayload();
        boolean success = loginService.authenticate(dto.username(), dto.password());
        LoginResponseDto body = new LoginResponseDto(success, success ? "Login successful" : "Login failed");
        Envelope<ResponseType,LoginResponseDto> response = new Envelope<>();
        response.setType(success ? ResponseType.LOGIN_RESULT : ResponseType.ERROR);
        response.setPayload(body);
        try {
            return objectMapper.writeValueAsString(response);
        } catch (Exception e) {
            logger.error("Error serializing login response", e);
            return "{\"type\":\"ERROR\",\"payload\":{\"success\":false,\"message\":\"Internal error\"}}";
        }
    }
}
