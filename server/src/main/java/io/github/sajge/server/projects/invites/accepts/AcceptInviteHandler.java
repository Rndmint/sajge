package io.github.sajge.server.projects.invites.accepts;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sajge.messages.Envelope;
import io.github.sajge.messages.requests.RequestType;
import io.github.sajge.messages.responses.ResponseType;
import io.github.sajge.server.patterns.Handler;
import io.github.sajge.logger.Logger;
import io.github.sajge.server.projects.ProjectService;

public class AcceptInviteHandler implements Handler<Envelope<RequestType, AcceptInviteDto>> {
    private static final Logger logger = Logger.get(AcceptInviteHandler.class);
    private final ObjectMapper mapper = new ObjectMapper();
    private final ProjectService service;

    public AcceptInviteHandler(ProjectService service) {
        logger.debug("AcceptInviteHandler initialized with service={}", service);
        this.service = service;
    }

    @Override
    public String handle(Envelope<RequestType, AcceptInviteDto> msg) {
        logger.debug("handle() called with envelope: {}", msg);
        AcceptInviteDto d = msg.getPayload();
        logger.debug("Payload: token={}, accessKey={}", d.token(), d.accessKey());

        boolean ok;
        String text;

        try {
            logger.info("Calling service.acceptInvite with accessKey={} and token={}",
                    d.accessKey(), d.token());
            ok = service.acceptInvite(d.token(), d.accessKey());
            if (ok) {
                logger.info("Invite accepted for accessKey={}", d.accessKey());
                text = "Invite accepted";
            } else {
                logger.warn("Invite acceptance failed or invalid key for accessKey={}", d.accessKey());
                text = "Invalid or expired key";
            }
        } catch (Exception e) {
            logger.error("Error accepting invite for accessKey={}", d.accessKey(), e);
            ok = false;
            text = "Acceptance failed";
        }

        Envelope<ResponseType, AcceptInviteResponseDto> resp = new Envelope<>();
        ResponseType respType = ok ? ResponseType.ACCEPT_INVITE_RESULT : ResponseType.ERROR;
        resp.setType(respType);
        resp.setPayload(new AcceptInviteResponseDto(ok, text));
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
