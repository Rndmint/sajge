package io.github.sajge.client.projects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.sajge.client.BaseClient;

import java.util.List;

public class ListOwnedProjectsWithCollaboratorsClient
        extends BaseClient<
                ListOwnedProjectsWithCollaboratorsClient.SuccessResponse,
                ListOwnedProjectsWithCollaboratorsClient.ErrorResponse> {

    private static final String REQUEST_TYPE = "LIST_OWNED_PROJECTS_WITH_COLLABORATORS";
    private static final String SUCCESS_TYPE = "LIST_OWNED_PROJECTS_WITH_COLLABORATORS_RESULT";

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

    public static record CollaboratorDto(
            @JsonProperty("id")       long id,
            @JsonProperty("username") String username
    ) {}

    public static record OwnedProjectDto(
            @JsonProperty("id")            long id,
            @JsonProperty("name")          String name,
            @JsonProperty("description")   String description,
            @JsonProperty("sceneJson")     String sceneJson,
            @JsonProperty("ownerId")       long ownerId,
            @JsonProperty("createdAt")     String createdAt,
            @JsonProperty("collaborators") List<CollaboratorDto> collaborators
    ) {}

    public static record SuccessResponse(
            @JsonProperty("success")  boolean success,
            @JsonProperty("projects") List<OwnedProjectDto> projects,
            @JsonProperty("message")  String message
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
