package io.github.sajge.client.accounts;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.sajge.client.BaseClient;

public class SignupClient
        extends BaseClient<SignupClient.SuccessResponse, SignupClient.ErrorResponse> {

    private static final String REQUEST_TYPE  = "SIGNUP";
    private static final String SUCCESS_TYPE  = "SIGNUP_RESULT";

    public static record Request(
            @JsonProperty("type")    String type,
            @JsonProperty("payload") Payload payload
    ) {
        @JsonCreator
        public Request(@JsonProperty("payload") Payload payload) {
            this(REQUEST_TYPE, payload);
        }
    }

    public static record Payload(
            @JsonProperty("username") String username,
            @JsonProperty("password") String password
    ) {}

    public static record SuccessResponse(
            @JsonProperty("success") boolean success,
            @JsonProperty("message") String message
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static record ErrorResponse(
            @JsonProperty("success") boolean success,
            @JsonProperty("message") String message
    ) {}

    public void send(String username, String password) throws Exception {
        Request req = new Request(new Payload(username, password));
        sendRequest(req, SUCCESS_TYPE, SuccessResponse.class, ErrorResponse.class);
    }
}
