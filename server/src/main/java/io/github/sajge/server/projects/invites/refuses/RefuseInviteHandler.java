package io.github.sajge.server.projects.invites.refuses;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sajge.messages.Envelope;
import io.github.sajge.messages.requests.RequestType;
import io.github.sajge.messages.responses.ResponseType;
import io.github.sajge.server.patterns.Handler;
import io.github.sajge.logger.Logger;
import io.github.sajge.server.projects.ProjectService;

public class RefuseInviteHandler
        implements Handler<Envelope<RequestType,RefuseInviteDto>> {
    private static final Logger logger = Logger.get(RefuseInviteHandler.class);
    private final ObjectMapper mapper = new ObjectMapper();
    private final ProjectService service;

    public RefuseInviteHandler(ProjectService service) {
        this.service = service;
    }

    @Override
    public String handle(Envelope<RequestType, RefuseInviteDto> msg) {
        RefuseInviteDto d = msg.getPayload();
        boolean ok;
        String text;
        try {
            ok = service.refuseInvite(d.token(), d.accessKey());
            text = ok ? "Invite refused" : "No such invite or not authorized";
        } catch (Exception e) {
            logger.error("Error refusing invite", e);
            ok = false;
            text = "Refusal failed";
        }
        Envelope<ResponseType, RefuseInviteResponseDto> resp = new Envelope<>();
        resp.setType(ok
                ? ResponseType.REFUSE_INVITE_RESULT
                : ResponseType.ERROR);
        resp.setPayload(new RefuseInviteResponseDto(ok, text));
        try {
            return mapper.writeValueAsString(resp);
        } catch (Exception e) {
            logger.error("Serialization error", e);
            return "{\"type\":\"ERROR\",\"payload\":{\"success\":false,\"message\":\"Internal error\"}}";
        }
    }
}
