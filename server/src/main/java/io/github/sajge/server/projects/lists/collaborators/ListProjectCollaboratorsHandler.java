package io.github.sajge.server.projects.lists.collaborators;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sajge.logger.Logger;
import io.github.sajge.messages.Envelope;
import io.github.sajge.messages.requests.RequestType;
import io.github.sajge.messages.responses.ResponseType;
import io.github.sajge.server.patterns.Handler;
import io.github.sajge.server.projects.ProjectService;

import java.util.Collections;
import java.util.List;

public class ListProjectCollaboratorsHandler
        implements Handler<Envelope<RequestType, ListProjectCollaboratorsDto>> {

    private static final Logger logger = Logger.get(ListProjectCollaboratorsHandler.class);
    private final ObjectMapper mapper = new ObjectMapper();
    private final ProjectService service;

    public ListProjectCollaboratorsHandler(ProjectService service) {
        this.service = service;
    }

    @Override
    public String handle(Envelope<RequestType, ListProjectCollaboratorsDto> msg) {
        String token = msg.getPayload().token();
        boolean ok;
        List<CollaboratorDto> collaborators = Collections.emptyList();
        String text;

        try {
            collaborators = service.listProjectCollaborators(token);
            ok = true;
            text = "Retrieved " + collaborators.size() + " collaborator(s)";
        } catch (Exception e) {
            logger.error("Error listing project collaborators", e);
            ok = false;
            text = "Failed to retrieve collaborators";
        }

        Envelope<ResponseType, ListProjectCollaboratorsResponseDto> resp = new Envelope<>();
        resp.setType(ok
                ? ResponseType.LIST_PROJECT_COLLABORATORS_RESULT
                : ResponseType.ERROR);
        resp.setPayload(new ListProjectCollaboratorsResponseDto(ok, collaborators, text));

        try {
            return mapper.writeValueAsString(resp);
        } catch (Exception e) {
            logger.error("Serialization error", e);
            return "{\"type\":\"ERROR\",\"payload\":{\"success\":false,\"message\":\"Internal error\"}}";
        }
    }
}
