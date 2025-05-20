package io.github.sajge.server.projects.deletes;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sajge.messages.Envelope;
import io.github.sajge.messages.requests.RequestType;
import io.github.sajge.messages.responses.ResponseType;
import io.github.sajge.server.patterns.Handler;
import io.github.sajge.logger.Logger;
import io.github.sajge.server.projects.ProjectService;

public class DeleteProjectHandler implements Handler<Envelope<RequestType, DeleteProjectDto>> {
    private static final Logger logger = Logger.get(DeleteProjectHandler.class);
    private final ObjectMapper mapper = new ObjectMapper();
    private final ProjectService service;

    public DeleteProjectHandler(ProjectService service) {
        logger.debug("DeleteProjectHandler initialized with service={}", service);
        this.service = service;
    }

    @Override
    public String handle(Envelope<RequestType, DeleteProjectDto> msg) {
        logger.debug("handle() called with envelope: {}", msg);
        DeleteProjectDto d = msg.getPayload();
        logger.debug("Payload: token={}, projectId={}", d.token(), d.projectId());

        boolean ok;
        String text;

        try {
            logger.info("Calling service.deleteProject for projectId={} with token={}",
                    d.projectId(), d.token());
            ok = service.deleteProject(d.token(), d.projectId());
            if (ok) {
                logger.info("deleteProject succeeded for projectId={}", d.projectId());
                text = "Project deleted";
            } else {
                logger.warn("deleteProject not authorized or not found for projectId={}", d.projectId());
                text = "Not authorized or not found";
            }
        } catch (Exception e) {
            logger.error("Error deleting projectId={}", d.projectId(), e);
            ok = false;
            text = "Deletion failed";
        }

        Envelope<ResponseType, DeleteProjectResponseDto> resp = new Envelope<>();
        ResponseType respType = ok ? ResponseType.DELETE_PROJECT_RESULT : ResponseType.ERROR;
        resp.setType(respType);
        resp.setPayload(new DeleteProjectResponseDto(ok, text));
        logger.debug("Response envelope created: type={}, payload={}", respType, resp.getPayload());

        try {
            String json = mapper.writeValueAsString(resp);
            logger.debug("Serialized response JSON: {}", json);
            return json;
        } catch (Exception e) {
            logger.error("Serialize error for response envelope", e);
            return "{\"type\":\"ERROR\",\"payload\":{\"success\":false}}";
        }
    }
}
