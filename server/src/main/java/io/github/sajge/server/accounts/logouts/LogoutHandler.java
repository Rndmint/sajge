package io.github.sajge.server.accounts.logouts;

import io.github.sajge.server.patterns.Handler;
import io.github.sajge.messages.Envelope;
import io.github.sajge.messages.requests.RequestType;
import io.github.sajge.messages.responses.ResponseType;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sajge.logger.Logger;
import io.github.sajge.server.security.SessionManager;

public class LogoutHandler implements Handler<Envelope<RequestType, LogoutDto>> {
    private static final Logger logger = Logger.get(LogoutHandler.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String handle(Envelope<RequestType, LogoutDto> msg) {
        String token = msg.getPayload().token();
        boolean wasValid = SessionManager.INSTANCE.isValid(token);
        if (wasValid) {
            SessionManager.INSTANCE.invalidate(token);
        }
        Envelope<ResponseType, LogoutResponseDto> resp = new Envelope<>();
        resp.setType(wasValid ? ResponseType.LOGOUT_RESULT : ResponseType.ERROR);
        resp.setPayload(new LogoutResponseDto(wasValid, wasValid ? "Logged out" : "Invalid token"));
        try {
            return objectMapper.writeValueAsString(resp);
        } catch (Exception e) {
            logger.error("Serialization error in LogoutHandler", e);
            return "{\"type\":\"ERROR\",\"payload\":{\"success\":false,\"message\":\"Internal error\"}}";
        }
    }
}

