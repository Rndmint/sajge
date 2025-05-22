package io.github.sajge.client.projects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.sajge.client.BaseClient;

public class UpdateProjectClient
        extends BaseClient<UpdateProjectClient.SuccessResponse, UpdateProjectClient.ErrorResponse> {

    private static final String REQUEST_TYPE = "UPDATE_PROJECT";
    private static final String SUCCESS_TYPE = "UPDATE_PROJECT_RESULT";

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
            @JsonProperty("token")       String token,
            @JsonProperty("projectId")   long projectId,
            @JsonProperty("name")        String name,
            @JsonProperty("description") String description,
            @JsonProperty("sceneJson")   String sceneJson
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

    public void send(String token,
                     long projectId,
                     String name,
                     String description,
                     String sceneJson) throws Exception {
        Request req = new Request(new Payload(token, projectId, name, description, sceneJson));
        sendRequest(req, SUCCESS_TYPE, SuccessResponse.class, ErrorResponse.class);
    }
}

