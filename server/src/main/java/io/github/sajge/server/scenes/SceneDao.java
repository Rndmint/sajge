package io.github.sajge.server.scenes;

import io.github.sajge.database.QueryExecutor;
import io.github.sajge.logger.Logger;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class SceneDao {
    private static final Logger logger = Logger.get(SceneDao.class);
    private final QueryExecutor queryExecutor;

    public SceneDao() throws Exception {
        this.queryExecutor = new QueryExecutor();
        logger.debug("SceneDao initialized");
    }

    public void createScene(String creator, long projectId, String sceneName)
            throws SQLException, InterruptedException {
        String sql = "INSERT INTO scenes (project_id, name, created_by) VALUES (?, ?, ?)";
        queryExecutor.executeUpdate(sql, projectId, sceneName, creator);
        logger.info("Scene created: {} in project {} by {}", sceneName, projectId, creator);
    }

    public List<Map<String,Object>> findScenesByProject(long projectId)
            throws SQLException, InterruptedException {
        String sql = "SELECT id, name, created_by, created_at FROM scenes WHERE project_id = ?";
        return queryExecutor.executeQuery(sql, projectId);
    }

    public void renameScene(long sceneId, String newName)
            throws SQLException, InterruptedException {
        String sql = "UPDATE scenes SET name = ? WHERE id = ?";
        queryExecutor.executeUpdate(sql, newName, sceneId);
        logger.info("Scene {} renamed to {}", sceneId, newName);
    }

    public void updateSceneJson(long sceneId, String json)
            throws SQLException, InterruptedException {
        String sql = "UPDATE scenes SET scene_data = ? WHERE id = ?";
        queryExecutor.executeUpdate(sql, json, sceneId);
        logger.info("Scene {} JSON updated", sceneId);
    }

    public void deleteScene(long sceneId)
            throws SQLException, InterruptedException {
        String sql = "DELETE FROM scenes WHERE id = ?";
        queryExecutor.executeUpdate(sql, sceneId);
        logger.info("Scene {} deleted", sceneId);
    }
}

