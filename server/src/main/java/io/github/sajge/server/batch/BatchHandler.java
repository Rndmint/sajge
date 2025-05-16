package io.github.sajge.server.batch;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sajge.messages.Envelope;
import io.github.sajge.messages.responses.ResponseType;
import io.github.sajge.messages.resquests.RequestType;
import io.github.sajge.server.common.ErrorDto;
import io.github.sajge.server.invites.InviteDao;
import io.github.sajge.server.invites.InviteService;
import io.github.sajge.server.patterns.Handler;
import io.github.sajge.server.projects.ProjectDao;
import io.github.sajge.server.projects.ProjectDto;
import io.github.sajge.server.projects.ProjectService;
import io.github.sajge.server.scenes.SceneDao;
import io.github.sajge.server.scenes.SceneDto;
import io.github.sajge.server.scenes.SceneService;
import io.github.sajge.server.sessions.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BatchHandler implements Handler<Envelope<RequestType, BatchDto>> {
    private final ObjectMapper mapper = new ObjectMapper();

    private final ProjectService projectService = new ProjectService(new ProjectDao());
    private final SceneService sceneService     = new SceneService(new SceneDao());
    private final InviteService inviteService   = new InviteService(new InviteDao());

    public BatchHandler() throws Exception {
    }

    @Override
    public String handle(Envelope<RequestType, BatchDto> envelope) {
        BatchDto batch = envelope.getPayload();
        String token = batch.token();
        List<OperationResult> results = new ArrayList<>();

        if (!SessionManager.INSTANCE.isValid(token)) {
            return errorResponse("Invalid or expired session");
        }
        String username = SessionManager.INSTANCE.getUsername(token);

        for (OperationDto op : batch.ops()) {
            String key = op.op() + ":" + op.entity();
            try {
                switch (key) {
                    case "CREATE:PROJECT" -> {
                        String name = op.data().get("name").asText();
                        projectService.createProject(username, name);
                        results.add(new OperationResult(op, true, "Created project '" + name + "'"));
                    }
                    case "LIST:PROJECT" -> {
                        List<ProjectDto> list = projectService.listProjects(username);
                        JsonNode listNode = mapper.valueToTree(list);
                        results.add(new OperationResult(op, true, listNode));
                    }
                    case "UPDATE:PROJECT" -> {
                        long pid = op.data().get("projectId").asLong();
                        JsonNode nameNode = op.data().get("name");
                        JsonNode sceneNode = op.data().get("scene");
                        if (nameNode != null) {
                            projectService.renameProject(username, pid, nameNode.asText());
                        }
                        if (sceneNode != null) {
                            projectService.updateProjectScene(username, pid, sceneNode.toString());
                        }
                        results.add(new OperationResult(op, true, "Updated project " + pid));
                    }
                    case "DELETE:PROJECT" -> {
                        long pid = op.data().get("projectId").asLong();
                        projectService.deleteProject(username, pid);
                        SessionManager.INSTANCE.getOpenProjects(token).remove(pid);
                        results.add(new OperationResult(op, true, "Deleted project " + pid));
                    }

                    case "CREATE:SCENE" -> {
                        long projId = op.data().get("projectId").asLong();
                        String name = op.data().get("sceneName").asText();
                        sceneService.createScene(username, String.valueOf(projId), name);
                        results.add(new OperationResult(op, true, "Created scene '" + name + "'"));
                    }
                    case "LIST:SCENE" -> {
                        long projId = op.data().get("projectId").asLong();
                        List<SceneDto> scenes = sceneService.listScenes(username, projId);
                        JsonNode scenesNode = mapper.valueToTree(scenes);
                        results.add(new OperationResult(op, true, scenesNode));
                    }
                    case "UPDATE:SCENE" -> {
                        long sid = op.data().get("sceneId").asLong();
                        JsonNode nameNode = op.data().get("name");
                        JsonNode sceneJson = op.data().get("scene");
                        if (nameNode != null) {
                            sceneService.renameScene(username, sid, nameNode.asText());
                        }
                        if (sceneJson != null) {
                            sceneService.updateSceneJson(username, sid, sceneJson.toString());
                        }
                        results.add(new OperationResult(op, true, "Updated scene " + sid));
                    }
                    case "DELETE:SCENE" -> {
                        long sid = op.data().get("sceneId").asLong();
                        sceneService.deleteScene(username, sid);
                        results.add(new OperationResult(op, true, "Deleted scene " + sid));
                    }

                    case "INVITE:USER" -> {
                        long projId = op.data().get("projectId").asLong();
                        String invitee = op.data().get("inviteeUsername").asText();
                        inviteService.inviteUser(username, projId, invitee);
                        results.add(new OperationResult(op, true, "Invited '" + invitee + "' to project " + projId));
                    }
                    default -> results.add(new OperationResult(op, false, "Unknown operation " + key));
                }
            } catch (Exception e) {
                results.add(new OperationResult(op, false, "Error: " + e.getMessage()));
            }
        }

        try {
            Envelope<ResponseType, Map<String,Object>> resp = new Envelope<>();
            resp.setType(ResponseType.BATCH_RESULT);
            resp.setPayload(Map.of("results", results));
            return mapper.writeValueAsString(resp);
        } catch (Exception ex) {
            return errorResponse("Internal error");
        }
    }

    private String errorResponse(String msg) {
        try {
            Envelope<ResponseType, ErrorDto> err = new Envelope<>();
            err.setType(ResponseType.ERROR);
            err.setPayload(new ErrorDto(msg));
            return mapper.writeValueAsString(err);
        } catch (Exception e) {
            return "{\"type\":\"ERROR\",\"payload\":{\"message\":\"Internal error\"}}";
        }
    }
}
