package io.github.sajge.client.projects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.sajge.client.BaseClient;

public class DeleteProjectClient
        extends BaseClient<DeleteProjectClient.SuccessResponse, DeleteProjectClient.ErrorResponse> {

    private static final String REQUEST_TYPE = "DELETE_PROJECT";
    private static final String SUCCESS_TYPE = "DELETE_PROJECT_RESULT";

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
            @JsonProperty("projectId") long projectId
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

    public void send(String token, long projectId) throws Exception {
        Request req = new Request(new Payload(token, projectId));
        sendRequest(req, SUCCESS_TYPE, SuccessResponse.class, ErrorResponse.class);
    }
}

