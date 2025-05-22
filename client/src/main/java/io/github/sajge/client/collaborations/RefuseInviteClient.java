package io.github.sajge.client.collaborations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.sajge.client.BaseClient;

public class RefuseInviteClient
        extends BaseClient<RefuseInviteClient.SuccessResponse, RefuseInviteClient.ErrorResponse> {

    private static final String REQUEST_TYPE = "REFUSE_INVITE";
    private static final String SUCCESS_TYPE = "REFUSE_INVITE_RESULT";

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
            @JsonProperty("token")     String token,
            @JsonProperty("accessKey") String accessKey
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

    public void send(String token, String accessKey) throws Exception {
        Request req = new Request(new Payload(token, accessKey));
        sendRequest(req, SUCCESS_TYPE, SuccessResponse.class, ErrorResponse.class);
    }
}
