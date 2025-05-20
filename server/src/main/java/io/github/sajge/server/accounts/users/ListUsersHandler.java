package io.github.sajge.server.accounts.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sajge.logger.Logger;
import io.github.sajge.messages.Envelope;
import io.github.sajge.messages.requests.RequestType;
import io.github.sajge.messages.responses.ResponseType;
import io.github.sajge.server.patterns.Handler;

import java.util.Collections;
import java.util.List;

public class ListUsersHandler
        implements Handler<Envelope<RequestType,ListUsersDto>> {
    private static final Logger logger = Logger.get(ListUsersHandler.class);
    private final ObjectMapper mapper = new ObjectMapper();
    private final UserService service;

    public ListUsersHandler(UserService service) {
        this.service = service;
    }

    @Override
    public String handle(Envelope<RequestType, ListUsersDto> msg) {
        String token = msg.getPayload().token();
        boolean ok;
        List<UserDto> users = Collections.emptyList();
        String text;

        try {
            users = service.listUsers(token);
            ok = true;
            text = "Retrieved " + users.size() + " user(s)";
        } catch (Exception e) {
            logger.error("Error listing users", e);
            ok = false;
            text = "Failed to retrieve users";
        }

        Envelope<ResponseType, ListUsersResponseDto> resp = new Envelope<>();
        resp.setType(ok
                ? ResponseType.LIST_USERS_RESULT
                : ResponseType.ERROR);
        resp.setPayload(new ListUsersResponseDto(ok, users, text));

        try {
            return mapper.writeValueAsString(resp);
        } catch (Exception e) {
            logger.error("Serialization error", e);
            return "{\"type\":\"ERROR\",\"payload\":{\"success\":false,\"message\":\"Internal error\"}}";
        }
    }
}
