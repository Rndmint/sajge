package io.github.sajge.server.sessionchecks;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sajge.messages.Envelope;
import io.github.sajge.messages.requests.RequestType;
import io.github.sajge.messages.responses.ResponseType;
import io.github.sajge.server.patterns.Handler;
import io.github.sajge.server.security.SessionManager;
import io.github.sajge.logger.Logger;

public class CheckSessionHandler implements Handler<Envelope<RequestType, CheckSessionDto>> {
    private static final Logger logger = Logger.get(CheckSessionHandler.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String handle(Envelope<RequestType, CheckSessionDto> msg) {
        String token = msg.getPayload().token();
        boolean valid = SessionManager.INSTANCE.isValid(token);
        String text = valid ? "Session is active" : "Invalid or expired token"; // Not adding expiration yet...

        Envelope<ResponseType, CheckSessionResponseDto> resp = new Envelope<>();
        resp.setType(ResponseType.CHECK_SESSION_RESULT);
        resp.setPayload(new CheckSessionResponseDto(valid, text));

        try {
            return objectMapper.writeValueAsString(resp);
        } catch (Exception e) {
            logger.error("Error serializing session-check response", e);
            return "{\"type\":\"ERROR\",\"payload\":{\"valid\":false,\"message\":\"Internal error\"}}";
        }
    }
}
