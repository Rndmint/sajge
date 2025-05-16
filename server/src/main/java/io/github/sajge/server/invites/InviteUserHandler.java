package io.github.sajge.server.invites;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sajge.messages.Envelope;
import io.github.sajge.messages.responses.ResponseType;
import io.github.sajge.messages.resquests.RequestType;
import io.github.sajge.server.patterns.Handler;
import io.github.sajge.logger.Logger;
import io.github.sajge.server.sessions.SessionManager;
import io.github.sajge.server.common.ErrorDto;
import io.github.sajge.server.common.SuccessDto;

public class InviteUserHandler implements Handler<Envelope<RequestType, InviteUserDto>> {
    private static final Logger logger = Logger.get(InviteUserHandler.class);
    private final InviteService inviteService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public InviteUserHandler(InviteService inviteService) {
        this.inviteService = inviteService;
    }

    @Override
    public String handle(Envelope<RequestType, InviteUserDto> env) {
        var dto = env.getPayload();
        String token = dto.token();

        if (!SessionManager.INSTANCE.isValid(token)) {
            Envelope<ResponseType, ErrorDto> err = new Envelope<>();
            err.setType(ResponseType.ERROR);
            err.setPayload(new ErrorDto("Invalid or expired session"));
            try {
                return objectMapper.writeValueAsString(err);
            } catch (Exception e) {
                logger.error("Serialization error", e);
                return "{\"type\":\"ERROR\",\"payload\":{\"message\":\"Internal error\"}}";
            }
        }
        String username = SessionManager.INSTANCE.getUsername(token);
        try {
            inviteService.inviteUser(username, dto.projectId(), dto.inviteeUsername());
            Envelope<ResponseType, SuccessDto> successEnv = new Envelope<>();
            successEnv.setType(ResponseType.USER_INVITED);
            successEnv.setPayload(new SuccessDto("User invited"));
            return objectMapper.writeValueAsString(successEnv);
        } catch (Exception e) {
            logger.error("Error inviting user for {}", username, e);
            Envelope<ResponseType, ErrorDto> err = new Envelope<>();
            err.setType(ResponseType.ERROR);
            err.setPayload(new ErrorDto("Failed to invite user"));
            try {
                return objectMapper.writeValueAsString(err);
            } catch (Exception ex) {
                logger.error("Serialization error", ex);
                return "{\"type\":\"ERROR\",\"payload\":{\"message\":\"Internal error\"}}";
            }
        }
    }
}

