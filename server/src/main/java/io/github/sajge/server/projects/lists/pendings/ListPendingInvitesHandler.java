package io.github.sajge.server.projects.lists.pendings;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sajge.logger.Logger;
import io.github.sajge.messages.Envelope;
import io.github.sajge.messages.requests.RequestType;
import io.github.sajge.messages.responses.ResponseType;
import io.github.sajge.server.patterns.Handler;
import io.github.sajge.server.projects.ProjectService;

import java.util.Collections;
import java.util.List;

public class ListPendingInvitesHandler
        implements Handler<Envelope<RequestType, ListPendingInvitesDto>> {
    private static final Logger logger = Logger.get(ListPendingInvitesHandler.class);
    private final ObjectMapper mapper = new ObjectMapper();
    private final ProjectService service;

    public ListPendingInvitesHandler(ProjectService service) {
        this.service = service;
    }

    @Override
    public String handle(Envelope<RequestType, ListPendingInvitesDto> msg) {
        String token = msg.getPayload().token();
        boolean ok;
        List<PendingInviteDto> invites = Collections.emptyList();
        String text;

        try {
            invites = service.listPendingInvites(token);
            ok = true;
            text = "Retrieved " + invites.size() + " pending invite(s)";
        } catch (Exception e) {
            logger.error("Error listing pending invites", e);
            ok = false;
            text = "Failed to retrieve pending invites";
        }

        Envelope<ResponseType, ListPendingInvitesResponseDto> resp = new Envelope<>();
        resp.setType(ok
                ? ResponseType.LIST_PENDING_INVITES_RESULT
                : ResponseType.ERROR);
        resp.setPayload(new ListPendingInvitesResponseDto(ok, invites, text));

        try {
            return mapper.writeValueAsString(resp);
        } catch (Exception e) {
            logger.error("Serialization error", e);
            return "{\"type\":\"ERROR\",\"payload\":{\"success\":false,\"message\":\"Internal error\"}}";
        }
    }
}
