package io.github.sajge.client.echos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.github.sajge.client.BaseClient;

public class EchoClient
        extends BaseClient<EchoClient.SuccessResponse, EchoClient.ErrorResponse> {

    private static final String REQUEST_TYPE = "ECHO";
    private static final String SUCCESS_TYPE = "ECHO_REPLY";

    public static record Request(
            @JsonProperty("type") String type,
            @JsonProperty("payload") Payload payload
    ) {
        @JsonCreator
        public Request(Payload payload) {
            this(REQUEST_TYPE, payload);
        }
    }

    public static record Payload(
            @JsonValue String message
    ) {}

    public static record SuccessResponse(
            @JsonProperty("message") String message
    ) {}

    public static record ErrorResponse(
            @JsonProperty("success") boolean success,
            @JsonProperty("message") String message
    ) {}

    public void send(String message) throws Exception {
        Request req = new Request(new Payload(message));
        sendRequest(req, SUCCESS_TYPE, SuccessResponse.class, ErrorResponse.class);
    }
}
