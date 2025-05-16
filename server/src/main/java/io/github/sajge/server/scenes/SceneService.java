package io.github.sajge.server.scenes;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sajge.logger.Logger;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.github.sajge.logger.Logger;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SceneService {
    private static final Logger logger = Logger.get(SceneService.class);
    private final SceneDao dao;

    public SceneService(SceneDao dao) {
        this.dao = dao;
    }

    public void createScene(String creator, long projectId, String sceneName) throws Exception {
        dao.createScene(creator, projectId, sceneName);
    }

    public List<SceneDto> listScenes(String username, long projectId) throws Exception {
        var rows = dao.findScenesByProject(projectId);
        return rows.stream().map(r -> new SceneDto(
                ((Number) r.get("id")).longValue(),
                (String)    r.get("name"),
                (String)    r.get("created_by"),
                r.get("created_at").toString()
        )).collect(Collectors.toList());
    }

    public void renameScene(String username, long sceneId, String newName) throws Exception {
        dao.renameScene(sceneId, newName);
    }

    public void updateSceneJson(String username, long sceneId, String json) throws Exception {
        dao.updateSceneJson(sceneId, json);
    }

    public void deleteScene(String username, long sceneId) throws Exception {
        dao.deleteScene(sceneId);
    }
}
