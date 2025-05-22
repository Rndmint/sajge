package io.github.sajge.client.collaborations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.sajge.client.BaseClient;

public class InviteCollaboratorClient
        extends BaseClient<
                InviteCollaboratorClient.SuccessResponse,
                InviteCollaboratorClient.ErrorResponse> {

    private static final String REQUEST_TYPE = "INVITE_COLLABORATOR";
    private static final String SUCCESS_TYPE = "INVITE_COLLABORATOR_RESULT";

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
            @JsonProperty("token")              String token,
            @JsonProperty("projectId")          long projectId,
            @JsonProperty("collaboratorUserId") long collaboratorUserId,
            @JsonProperty("canEdit")            boolean canEdit
    ) {}

    public static record SuccessResponse(
            @JsonProperty("success")   boolean success,
            @JsonProperty("accessKey") String accessKey,
            @JsonProperty("message")   String message
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static record ErrorResponse(
            @JsonProperty("success") boolean success,
            @JsonProperty("message") String message
    ) {}

    public void send(String token,
                     long projectId,
                     long collaboratorUserId,
                     boolean canEdit) throws Exception {
        Request req = new Request(
                new Payload(token, projectId, collaboratorUserId, canEdit)
        );
        sendRequest(req, SUCCESS_TYPE, SuccessResponse.class, ErrorResponse.class);
    }
}
