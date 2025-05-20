package io.github.sajge.server.accounts.deletes;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sajge.logger.Logger;
import io.github.sajge.messages.Envelope;
import io.github.sajge.messages.responses.ResponseType;
import io.github.sajge.messages.requests.RequestType;
import io.github.sajge.server.patterns.Handler;

public class DeleteHandler implements Handler<Envelope<RequestType, DeleteDto>> {
    private static final Logger logger = Logger.get(DeleteHandler.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DeleteService service;

    public DeleteHandler(DeleteService service) {
        this.service = service;
    }

    @Override
    public String handle(Envelope<RequestType, DeleteDto> msg) {
        DeleteDto dto = msg.getPayload();
        boolean success;
        String text;

        try {
            success = service.deleteAccount(dto.username(), dto.token());
            text = success ? "Account deleted" : "Invalid token or user not found";
        } catch (Exception e) {
            logger.error("Error during account deletion for {}", dto.username(), e);
            success = false;
            text = "Deletion failed";
        }

        Envelope<ResponseType, DeleteResponseDto> resp = new Envelope<>();
        resp.setType(success ? ResponseType.DELETE_ACCOUNT_RESULT : ResponseType.ERROR);
        resp.setPayload(new DeleteResponseDto(success, text));

        try {
            return objectMapper.writeValueAsString(resp);
        } catch (Exception e) {
            logger.error("Serialization error in DeleteAccountHandler", e);
            return "{\"type\":\"ERROR\",\"payload\":{\"success\":false,\"message\":\"Internal error\"}}";
        }
    }
}
