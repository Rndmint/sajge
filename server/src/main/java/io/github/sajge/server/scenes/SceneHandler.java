package io.github.sajge.server.scenes;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sajge.messages.Envelope;
import io.github.sajge.messages.responses.ResponseType;
import io.github.sajge.messages.resquests.RequestType;
import io.github.sajge.server.patterns.Handler;
import io.github.sajge.logger.Logger;
import io.github.sajge.server.sessions.SessionManager;
import io.github.sajge.server.common.ErrorDto;
import io.github.sajge.server.common.SuccessDto;

public class SceneHandler  {
//    private static final Logger logger = Logger.get(SceneHandler.class);
//    private final SceneService sceneService;
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    public SceneHandler(SceneService sceneService) {
//        this.sceneService = sceneService;
//    }
//
//    @Override
//    public String handle(Envelope<RequestType, SceneDto> env) {
//        var dto = env.getPayload();
//        String token = dto.token();
//
//        if (!SessionManager.INSTANCE.isValid(token)) {
//            Envelope<ResponseType, ErrorDto> err = new Envelope<>();
//            err.setType(ResponseType.ERROR);
//            err.setPayload(new ErrorDto("Invalid or expired session"));
//            try {
//                return objectMapper.writeValueAsString(err);
//            } catch (Exception e) {
//                logger.error("Serialization error", e);
//                return "{\"type\":\"ERROR\",\"payload\":{\"message\":\"Internal error\"}}";
//            }
//        }
//        String username = SessionManager.INSTANCE.getUsername(token);
//        try {
//            sceneService.createScene(username, dto.projectId(), dto.sceneName());
//            Envelope<ResponseType, SuccessDto> successEnv = new Envelope<>();
//            successEnv.setType(ResponseType.SCENE_CREATED);
//            successEnv.setPayload(new SuccessDto("Scene created"));
//            return objectMapper.writeValueAsString(successEnv);
//        } catch (Exception e) {
//            logger.error("Error creating scene for {}", username, e);
//            Envelope<ResponseType, ErrorDto> err = new Envelope<>();
//            err.setType(ResponseType.ERROR);
//            err.setPayload(new ErrorDto("Failed to create scene"));
//            try {
//                return objectMapper.writeValueAsString(err);
//            } catch (Exception ex) {
//                logger.error("Serialization error", ex);
//                return "{\"type\":\"ERROR\",\"payload\":{\"message\":\"Internal error\"}}";
//            }
//        }
//    }
}

