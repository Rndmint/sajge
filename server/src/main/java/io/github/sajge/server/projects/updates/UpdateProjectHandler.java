package io.github.sajge.server.projects.updates;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sajge.messages.Envelope;
import io.github.sajge.messages.requests.RequestType;
import io.github.sajge.messages.responses.ResponseType;
import io.github.sajge.server.patterns.Handler;
import io.github.sajge.logger.Logger;
import io.github.sajge.server.projects.ProjectService;

public class UpdateProjectHandler implements Handler<Envelope<RequestType, UpdateProjectDto>> {
    private static final Logger logger = Logger.get(UpdateProjectHandler.class);
    private final ObjectMapper mapper = new ObjectMapper();
    private final ProjectService service;

    public UpdateProjectHandler(ProjectService service) {
        logger.debug("UpdateProjectHandler initialized with service={}", service);
        this.service = service;
    }

    @Override
    public String handle(Envelope<RequestType, UpdateProjectDto> msg) {
        logger.debug("handle() called with envelope: {}", msg);
        UpdateProjectDto d = msg.getPayload();
        logger.debug("Payload: token={}, projectId={}, name={}, description length={}, sceneJson length={}",
                d.token(),
                d.projectId(),
                d.name(),
                d.description() != null ? d.description().length() : 0,
                d.sceneJson() != null ? d.sceneJson().length() : 0);

        boolean ok;
        String text;
        try {
            logger.info("Calling service.updateProject for projectId={} and user token={}",
                    d.projectId(), d.token());
            ok = service.updateProject(
                    d.token(),
                    d.projectId(),
                    d.name(),
                    d.description(),
                    d.sceneJson()
            );
            if (ok) {
                logger.info("Project {} updated successfully", d.projectId());
                text = "Project updated";
            } else {
                logger.warn("Project {} update not authorized or not found", d.projectId());
                text = "Not authorized or not found";
            }
        } catch (Exception e) {
            logger.error("Error updating project {}", d.projectId(), e);
            ok = false;
            text = "Update failed";
        }

        Envelope<ResponseType, UpdateProjectResponseDto> resp = new Envelope<>();
        ResponseType respType = ok ? ResponseType.UPDATE_PROJECT_RESULT : ResponseType.ERROR;
        resp.setType(respType);
        resp.setPayload(new UpdateProjectResponseDto(ok, text));
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
