package io.github.sajge.server.projects.lists.invited;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sajge.messages.Envelope;
import io.github.sajge.messages.requests.RequestType;
import io.github.sajge.messages.responses.ResponseType;
import io.github.sajge.server.patterns.Handler;
import io.github.sajge.logger.Logger;
import io.github.sajge.server.projects.ProjectService;

import java.util.Collections;
import java.util.List;

public class ListSentInvitesHandler
        implements Handler<Envelope<RequestType, ListSentInvitesDto>> {
    private static final Logger logger = Logger.get(ListSentInvitesHandler.class);
    private final ObjectMapper mapper = new ObjectMapper();
    private final ProjectService service;

    public ListSentInvitesHandler(ProjectService service) {
        this.service = service;
    }

    @Override
    public String handle(Envelope<RequestType, ListSentInvitesDto> msg) {
        String token = msg.getPayload().token();
        boolean ok;
        List<SentInviteDto> invites = Collections.emptyList();
        String text;

        try {
            invites = service.listSentInvites(token);
            ok = true;
            text = "Retrieved " + invites.size() + " sent invite(s)";
        } catch (Exception e) {
            logger.error("Error listing sent invites", e);
            ok = false;
            text = "Failed to retrieve sent invites";
        }

        Envelope<ResponseType, ListSentInvitesResponseDto> resp = new Envelope<>();
        resp.setType(ok
                ? ResponseType.LIST_SENT_INVITES_RESULT
                : ResponseType.ERROR);
        resp.setPayload(new ListSentInvitesResponseDto(ok, invites, text));

        try {
            return mapper.writeValueAsString(resp);
        } catch (Exception e) {
            logger.error("Serialization error", e);
            return "{\"type\":\"ERROR\",\"payload\":{\"success\":false,\"message\":\"Internal error\"}}";
        }
    }
}
