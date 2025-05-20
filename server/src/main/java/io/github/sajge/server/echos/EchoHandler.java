package io.github.sajge.server.echos;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sajge.messages.Envelope;
import io.github.sajge.messages.responses.ResponseType;
import io.github.sajge.messages.requests.RequestType;
import io.github.sajge.server.patterns.Handler;

public class EchoHandler implements Handler<Envelope<RequestType,EchoDto>> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String handle(Envelope<RequestType,EchoDto> msg) {
        EchoDto dto = msg.getPayload();
        EchoResponseDto body = new EchoResponseDto(dto.message());
        Envelope<ResponseType,EchoResponseDto> resp = new Envelope<>();
        resp.setType(ResponseType.ECHO_REPLY);
        resp.setPayload(body);
        try {
            return objectMapper.writeValueAsString(resp);
        } catch (Exception e) {
            return "{\"type\":\"ERROR\",\"payload\":{\"message\":\"Internal error\"}}";
        }
    }
}
