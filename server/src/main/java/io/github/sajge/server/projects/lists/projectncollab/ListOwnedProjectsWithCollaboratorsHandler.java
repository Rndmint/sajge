package io.github.sajge.server.projects.lists.projectncollab;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sajge.logger.Logger;
import io.github.sajge.messages.Envelope;
import io.github.sajge.messages.requests.RequestType;
import io.github.sajge.messages.responses.ResponseType;
import io.github.sajge.server.patterns.Handler;
import io.github.sajge.server.projects.ProjectService;

import java.util.Collections;
import java.util.List;

public class ListOwnedProjectsWithCollaboratorsHandler
        implements Handler<Envelope<RequestType, ListOwnedProjectsWithCollaboratorsDto>> {

    private static final Logger logger = Logger.get(ListOwnedProjectsWithCollaboratorsHandler.class);
    private final ObjectMapper mapper = new ObjectMapper();
    private final ProjectService service;

    public ListOwnedProjectsWithCollaboratorsHandler(ProjectService service) {
        this.service = service;
    }

    @Override
    public String handle(Envelope<RequestType, ListOwnedProjectsWithCollaboratorsDto> msg) {
        String token = msg.getPayload().token();
        boolean ok;
        List<OwnedProjectWithCollaboratorsDto> projects = Collections.emptyList();
        String text;

        try {
            projects = service.listOwnedProjectsWithCollaborators(token);
            ok = true;
            text = "Retrieved " + projects.size() + " project(s) with collaborators";
        } catch (Exception e) {
            logger.error("Error listing owned projects with collaborators", e);
            ok = false;
            text = "Failed to retrieve projects";
        }

        Envelope<ResponseType, ListOwnedProjectsWithCollaboratorsResponseDto> resp = new Envelope<>();
        resp.setType(ok
                ? ResponseType.LIST_OWNED_PROJECTS_WITH_COLLABORATORS_RESULT
                : ResponseType.ERROR);
        resp.setPayload(new ListOwnedProjectsWithCollaboratorsResponseDto(ok, projects, text));
        try {
            return mapper.writeValueAsString(resp);
        } catch (Exception e) {
            logger.error("Serialization error", e);
            return "{\"type\":\"ERROR\",\"payload\":{\"success\":false,\"message\":\"Internal error\"}}";
        }
    }
}
