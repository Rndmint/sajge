package io.github.sajge.client.collaborations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.sajge.client.BaseClient;

import java.util.List;

public class ListSentInvitesClient
        extends BaseClient<
                ListSentInvitesClient.SuccessResponse,
                ListSentInvitesClient.ErrorResponse> {

    private static final String REQUEST_TYPE = "LIST_SENT_INVITES";
    private static final String SUCCESS_TYPE = "LIST_SENT_INVITES_RESULT";

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
            @JsonProperty("token") String token
    ) {}

    public static record SentInviteDto(
            @JsonProperty("invitedUserId")   long invitedUserId,
            @JsonProperty("invitedUsername") String invitedUsername,
            @JsonProperty("projectId")       long projectId,
            @JsonProperty("accessKey")       String accessKey,
            @JsonProperty("status")          String status,
            @JsonProperty("createdAt")       String createdAt
    ) {}

    public static record SuccessResponse(
            @JsonProperty("success") boolean success,
            @JsonProperty("invites") List<SentInviteDto> invites,
            @JsonProperty("message") String message
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static record ErrorResponse(
            @JsonProperty("success") boolean success,
            @JsonProperty("message") String message
    ) {}

    public void send(String token) throws Exception {
        Request req = new Request(new Payload(token));
        sendRequest(req, SUCCESS_TYPE, SuccessResponse.class, ErrorResponse.class);
    }
}
