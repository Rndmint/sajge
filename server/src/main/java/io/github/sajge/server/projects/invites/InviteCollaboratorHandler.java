package io.github.sajge.server.projects.invites;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sajge.messages.Envelope;
import io.github.sajge.messages.requests.RequestType;
import io.github.sajge.messages.responses.ResponseType;
import io.github.sajge.server.patterns.Handler;
import io.github.sajge.logger.Logger;
import io.github.sajge.server.projects.ProjectService;

public class InviteCollaboratorHandler implements Handler<Envelope<RequestType, InviteCollaboratorDto>> {
    private static final Logger logger = Logger.get(InviteCollaboratorHandler.class);
    private final ObjectMapper mapper = new ObjectMapper();
    private final ProjectService service;

    public InviteCollaboratorHandler(ProjectService service) {
        logger.debug("InviteCollaboratorHandler initialized with service={}", service);
        this.service = service;
    }

    @Override
    public String handle(Envelope<RequestType, InviteCollaboratorDto> msg) {
        logger.debug("handle() called with envelope: {}", msg);
        InviteCollaboratorDto d = msg.getPayload();
        logger.debug("Payload: token={}, projectId={}, collaboratorUserId={}, canEdit={}",
                d.token(), d.projectId(), d.collaboratorUserId(), d.canEdit());

        boolean ok;
        String key = "";
        String text;

        try {
            logger.info("Calling service.inviteCollaborator for projectId={} and collaboratorUserId={}",
                    d.projectId(), d.collaboratorUserId());
            key = service.inviteCollaborator(
                    d.token(),
                    d.projectId(),
                    d.collaboratorUserId(),
                    d.canEdit()
            );
            ok = true;
            text = "Invitation sent";
            logger.info("inviteCollaborator succeeded: accessKey={}", key);
        } catch (IllegalArgumentException e) {
            logger.warn("Invite failed: {}", e.getMessage());
            ok = false;
            text = e.getMessage();
        } catch (Exception e) {
            logger.error("Error inviting collaborator for projectId={} to userId={}",
                    d.projectId(), d.collaboratorUserId(), e);
            ok = false;
            text = "Invite failed";
        }

        Envelope<ResponseType, InviteCollaboratorResponseDto> resp = new Envelope<>();
        ResponseType respType = ok ? ResponseType.INVITE_COLLABORATOR_RESULT : ResponseType.ERROR;
        resp.setType(respType);
        resp.setPayload(new InviteCollaboratorResponseDto(ok, key, text));
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
