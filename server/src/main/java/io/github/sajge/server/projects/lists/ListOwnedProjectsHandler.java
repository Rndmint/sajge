package io.github.sajge.server.projects.lists;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sajge.messages.Envelope;
import io.github.sajge.messages.requests.RequestType;
import io.github.sajge.messages.responses.ResponseType;
import io.github.sajge.server.patterns.Handler;
import io.github.sajge.logger.Logger;
import io.github.sajge.server.projects.ProjectDto;
import io.github.sajge.server.projects.ProjectService;

import java.util.List;

public class ListOwnedProjectsHandler
        implements Handler<Envelope<RequestType,ListOwnedProjectsDto>> {
    private static final Logger logger = Logger.get(ListOwnedProjectsHandler.class);
    private final ObjectMapper mapper = new ObjectMapper();
    private final ProjectService service;

    public ListOwnedProjectsHandler(ProjectService service) {
        this.service = service;
    }

    @Override
    public String handle(Envelope<RequestType, ListOwnedProjectsDto> msg) {
        String token = msg.getPayload().token();
        boolean ok;
        List<ProjectDto> projects = List.of();
        String text;
        try {
            projects = service.listOwnedProjects(token);
            ok = true;
            text = "Retrieved " + projects.size() + " project(s)";
        } catch (Exception e) {
            logger.error("Error listing owned projects", e);
            ok = false;
            text = "Failed to retrieve projects";
        }

        Envelope<ResponseType, ListOwnedProjectsResponseDto> resp = new Envelope<>();
        resp.setType(ok
                ? ResponseType.LIST_OWNED_PROJECTS_RESULT
                : ResponseType.ERROR);
        resp.setPayload(new ListOwnedProjectsResponseDto(ok, projects, text));
        try {
            return mapper.writeValueAsString(resp);
        } catch (Exception e) {
            logger.error("Serialization error", e);
            return "{\"type\":\"ERROR\",\"payload\":{\"success\":false,\"message\":\"Internal error\"}}";
        }
    }
}
