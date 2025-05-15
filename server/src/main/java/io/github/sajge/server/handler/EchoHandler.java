package io.github.sajge.server.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sajge.dto.SignupDto;
import io.github.sajge.message.Message;
import io.github.sajge.services.SignupService;
import io.github.sajge.logger.Logger;

public class EchoHandler implements Handler {
    private static final Logger logger = Logger.get(EchoHandler.class);

    // private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String handle(Message msg) {
        return msg.getPayload().toString();
    }
}
