package io.github.sajge.server.accounts.signups;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sajge.messages.responses.ResponseType;
import io.github.sajge.messages.requests.RequestType;
import io.github.sajge.server.patterns.Handler;
import io.github.sajge.messages.Envelope;
import io.github.sajge.logger.Logger;

import java.sql.SQLException;
import java.util.regex.Pattern;

public class SignupHandler implements Handler<Envelope<RequestType,SignupDto>> {
    private static final Logger logger = Logger.get(SignupHandler.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final SignupService signupService;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*\\W).{8,}$");

    public SignupHandler(SignupService signupService) {
        this.signupService = signupService;
    }

    @Override
    public String handle(Envelope<RequestType,SignupDto> msg) {
        SignupDto dto = msg.getPayload();

        String pw = dto.password();
        if (!PASSWORD_PATTERN.matcher(pw).matches()) {
            SignupResponseDto body = new SignupResponseDto(
                    false,
                    "Password must be at least 8 characters,\n" +
                            "include uppercase, lowercase, digit, and symbol"
            );
            Envelope<ResponseType,SignupResponseDto> err = new Envelope<>();
            err.setType(ResponseType.ERROR);
            err.setPayload(body);
            try {
                return objectMapper.writeValueAsString(err);
            } catch (JsonProcessingException e) {
                logger.error("Error serializing password‐error response", e);
                return "{\"type\":\"ERROR\",\"payload\":{\"success\":false,\"message\":\"Password format invalid\"}}";
            }
        }

        boolean success;
        String text;
        try {
            signupService.create(dto.username(), dto.password());
            success = true;
            text = "Signup successful";
        } catch (SQLException e) {
            logger.error("Error handling signup", e);
            success = false;
            text = "Username already taken";
        } catch (Exception e) {
            logger.error("Error handling signup", e);
            success = false;
            text = "Signup failed";
        }
        SignupResponseDto body = new SignupResponseDto(success, text);
        Envelope<ResponseType,SignupResponseDto> response = new Envelope<>();
        response.setType(success ? ResponseType.SIGNUP_RESULT : ResponseType.ERROR);
        response.setPayload(body);
        try {
            return objectMapper.writeValueAsString(response);
        } catch (Exception e) {
            logger.error("Error serializing signup response", e);
            return "{\"type\":\"ERROR\",\"payload\":{\"success\":false,\"message\":\"Internal error\"}}";
        }
    }
}
