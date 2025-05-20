package io.github.sajge.server.accounts.logins;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sajge.messages.Envelope;
import io.github.sajge.messages.responses.ResponseType;
import io.github.sajge.messages.requests.RequestType;
import io.github.sajge.server.patterns.Handler;
import io.github.sajge.server.security.SessionManager;
import io.github.sajge.server.security.Token;
import io.github.sajge.logger.Logger;

import java.sql.SQLException;

public class LoginHandler implements Handler<Envelope<RequestType, LoginDto>> {
    private static final Logger logger = Logger.get(LoginHandler.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final LoginService loginService;

    public LoginHandler(LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    public String handle(Envelope<RequestType, LoginDto> msg) {
        LoginDto dto = msg.getPayload();
        boolean success = loginService.authenticate(dto.username(), dto.password());
        String token = success ? Token.generate() : "";

        if (success) {
            long userId = 0;
            try {
                userId = loginService.getUserId(dto.username());
            } catch (SQLException | InterruptedException e) {
                throw new RuntimeException(e);
            }
            SessionManager.INSTANCE.register(token, userId);
        }

        LoginResponseDto body = new LoginResponseDto(
                success,
                token,
                success ? "Login successful" : "Login failed"
        );
        Envelope<ResponseType, LoginResponseDto> response = new Envelope<>();
        response.setType(success ? ResponseType.LOGIN_RESULT : ResponseType.ERROR);
        response.setPayload(body);
        try {
            return objectMapper.writeValueAsString(response);
        } catch (Exception e) {
            logger.error("Error serializing login response", e);
            return "{\"type\":\"ERROR\",\"payload\":{\"message\":\"Internal error\"}}";
        }
    }
}
