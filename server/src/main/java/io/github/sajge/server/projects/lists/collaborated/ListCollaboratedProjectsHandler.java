package io.github.sajge.server.projects.lists.collaborated;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sajge.logger.Logger;
import io.github.sajge.messages.Envelope;
import io.github.sajge.messages.requests.RequestType;
import io.github.sajge.messages.responses.ResponseType;
import io.github.sajge.server.patterns.Handler;
import io.github.sajge.server.projects.ProjectService;

import java.util.Collections;
import java.util.List;

public class ListCollaboratedProjectsHandler
        implements Handler<Envelope<RequestType, ListCollaboratedProjectsDto>> {

    private static final Logger logger = Logger.get(ListCollaboratedProjectsHandler.class);
    private final ObjectMapper mapper = new ObjectMapper();
    private final ProjectService service;

    public ListCollaboratedProjectsHandler(ProjectService service) {
        this.service = service;
    }

    @Override
    public String handle(Envelope<RequestType, ListCollaboratedProjectsDto> msg) {
        String token = msg.getPayload().token();
        boolean ok;
        List<CollaboratedProjectDto> projects = Collections.emptyList();
        String text;

        try {
            projects = service.listCollaboratedProjects(token);
            ok = true;
            text = "Retrieved " + projects.size() + " collaborated project(s)";
        } catch (Exception e) {
            logger.error("Error listing collaborated projects", e);
            ok = false;
            text = "Failed to retrieve collaborated projects";
        }

        Envelope<ResponseType, ListCollaboratedProjectsResponseDto> resp = new Envelope<>();
        resp.setType(ok
                ? ResponseType.LIST_COLLABORATED_PROJECTS_RESULT
                : ResponseType.ERROR);
        resp.setPayload(new ListCollaboratedProjectsResponseDto(ok, projects, text));

        try {
            return mapper.writeValueAsString(resp);
        } catch (Exception e) {
            logger.error("Serialization error", e);
            return "{\"type\":\"ERROR\",\"payload\":{\"success\":false,\"message\":\"Internal error\"}}";
        }
    }
}

