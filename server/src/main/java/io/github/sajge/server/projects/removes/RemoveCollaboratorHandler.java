package io.github.sajge.server.projects.removes;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sajge.messages.Envelope;
import io.github.sajge.messages.requests.RequestType;
import io.github.sajge.messages.responses.ResponseType;
import io.github.sajge.server.patterns.Handler;
import io.github.sajge.logger.Logger;
import io.github.sajge.server.projects.ProjectService;

public class RemoveCollaboratorHandler
        implements Handler<Envelope<RequestType,RemoveCollaboratorDto>> {
    private static final Logger logger = Logger.get(RemoveCollaboratorHandler.class);
    private final ObjectMapper mapper = new ObjectMapper();
    private final ProjectService service;

    public RemoveCollaboratorHandler(ProjectService service) {
        this.service = service;
    }

    @Override
    public String handle(Envelope<RequestType, RemoveCollaboratorDto> msg) {
        RemoveCollaboratorDto d = msg.getPayload();
        boolean ok;
        String text;
        try {
            ok = service.removeCollaborator(
                    d.token(), d.projectId(), d.collaboratorUserId());
            text = ok ? "Collaborator removed" : "Not found or not authorized";
        } catch (IllegalAccessException e) {
            logger.warn("Remove failed: {}", e.getMessage());
            ok = false;
            text = e.getMessage();
        } catch (Exception e) {
            logger.error("Error removing collaborator", e);
            ok = false;
            text = "Removal failed";
        }
        Envelope<ResponseType, RemoveCollaboratorResponseDto> resp = new Envelope<>();
        resp.setType(ok
                ? ResponseType.REMOVE_COLLABORATOR_RESULT
                : ResponseType.ERROR);
        resp.setPayload(new RemoveCollaboratorResponseDto(ok, text));
        try {
            return mapper.writeValueAsString(resp);
        } catch (Exception e) {
            logger.error("Serialization error", e);
            return "{\"type\":\"ERROR\",\"payload\":{\"success\":false,\"message\":\"Internal error\"}}";
        }
    }
}
