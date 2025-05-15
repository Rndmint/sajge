package io.github.sajge.server.signups;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sajge.messages.responses.ResponseType;
import io.github.sajge.messages.resquests.RequestType;
import io.github.sajge.server.patterns.Handler;
import io.github.sajge.messages.Envelope;
import io.github.sajge.logger.Logger;

public class SignupHandler implements Handler<Envelope<RequestType,SignupDto>> {
    private static final Logger logger = Logger.get(SignupHandler.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final SignupService signupService;

    public SignupHandler(SignupService signupService) {
        this.signupService = signupService;
    }

    @Override
    public String handle(Envelope<RequestType,SignupDto> msg) {
        SignupDto dto = msg.getPayload();
        boolean success;
        String text;
        try {
            signupService.create(dto.username(), dto.password());
            success = true;
            text = "Signup successful";
        } catch (Exception e) {
            logger.error("Error handling signup", e);
            success = false;
            text = "Signup failed";
        }
        SignupResponseDto body = new SignupResponseDto(success, text);
        Envelope<ResponseType,SignupResponseDto> resp = new Envelope<>();
        resp.setType(success ? ResponseType.SIGNUP_RESULT : ResponseType.ERROR);
        resp.setPayload(body);
        try {
            return objectMapper.writeValueAsString(resp);
        } catch (Exception e) {
            logger.error("Error serializing signup response", e);
            return "{\"type\":\"ERROR\",\"payload\":{\"success\":false,\"message\":\"Internal error\"}}";
        }
    }
}
