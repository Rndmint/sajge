package io.github.sajge.server.projects.creates;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sajge.messages.Envelope;
import io.github.sajge.messages.requests.RequestType;
import io.github.sajge.messages.responses.ResponseType;
import io.github.sajge.server.patterns.Handler;
import io.github.sajge.logger.Logger;
import io.github.sajge.server.projects.ProjectService;

public class CreateProjectHandler implements Handler<Envelope<RequestType, CreateProjectDto>> {
    private static final Logger logger = Logger.get(CreateProjectHandler.class);
    private final ObjectMapper mapper = new ObjectMapper();
    private final ProjectService service;

    public CreateProjectHandler(ProjectService service) {
        logger.debug("CreateProjectHandler initialized with service={}", service);
        this.service = service;
    }

    @Override
    public String handle(Envelope<RequestType, CreateProjectDto> msg) {
        logger.debug("handle() called with envelope: {}", msg);
        CreateProjectDto d = msg.getPayload();
        logger.debug("Payload: token={}, name={}, description length={}, sceneJson length={}",
                d.token(),
                d.name(),
                d.description() != null ? d.description().length() : 0,
                d.sceneJson() != null ? d.sceneJson().length() : 0);

        boolean ok;
        long id = -1;
        String text;

        try {
            logger.info("Calling service.createProject for name={} with token={}",
                    d.name(), d.token());
            id = service.createProject(d.token(), d.name(), d.description(), d.sceneJson());
            ok = true;
            text = "Project created";
            logger.info("createProject succeeded: id={} for token={}", id, d.token());
        } catch (Exception e) {
            logger.error("Error creating project for name={} token={}", d.name(), d.token(), e);
            ok = false;
            text = "Creation failed";
        }

        Envelope<ResponseType, CreateProjectResponseDto> resp = new Envelope<>();
        ResponseType respType = ok ? ResponseType.CREATE_PROJECT_RESULT : ResponseType.ERROR;
        resp.setType(respType);
        resp.setPayload(new CreateProjectResponseDto(ok, id, text));
        logger.debug("Response envelope created: type={}, payload={}",
                respType, resp.getPayload());

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
