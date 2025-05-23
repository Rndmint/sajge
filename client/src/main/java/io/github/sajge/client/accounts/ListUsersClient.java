package io.github.sajge.client.accounts;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.sajge.client.BaseClient;

import java.util.List;

public class ListUsersClient
        extends BaseClient<
                ListUsersClient.SuccessResponse,
                ListUsersClient.ErrorResponse> {

    private static final String REQUEST_TYPE = "LIST_USERS";
    private static final String SUCCESS_TYPE = "LIST_USERS_RESULT";

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

    public static record UserDto(
            @JsonProperty("id")       long id,
            @JsonProperty("username") String username
    ) {
        @Override
        public String toString() {
            return username;
        }
    }

    public static record SuccessResponse(
            @JsonProperty("success") boolean success,
            @JsonProperty("users")   List<UserDto> users,
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
