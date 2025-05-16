package io.github.sajge.server.projects;

import io.github.sajge.database.QueryExecutor;
import io.github.sajge.logger.Logger;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ProjectDao {
    private static final Logger logger = Logger.get(ProjectDao.class);
    private final QueryExecutor queryExecutor;

    public ProjectDao() throws Exception {
        this.queryExecutor = new QueryExecutor();
        logger.debug("ProjectDao initialized");
    }

    public long getUserId(String username) throws SQLException, InterruptedException {
        String sql = "SELECT id FROM users WHERE username = ?";
        List<Map<String, Object>> rows = queryExecutor.executeQuery(sql, username);
        if (rows.isEmpty()) {
            throw new SQLException("No such user: " + username);
        }
        Object idObj = rows.get(0).get("id");
        return ((Number)idObj).longValue();
    }

    public void createProject(String ownerUsername, String projectName)
            throws SQLException, InterruptedException {
        long ownerId = getUserId(ownerUsername);
        String sql = "INSERT INTO projects (name, owner_id, scene) VALUES (?, ?, ?)";
        queryExecutor.executeUpdate(sql, projectName, ownerId, "{}");
        logger.info("Project created: {} (id assigned by DB) by user ID {}", projectName, ownerId);
    }

    public List<Map<String,Object>> findProjectsByUser(long userId)
            throws SQLException, InterruptedException {
        String sql = "SELECT id,name,scene FROM projects WHERE owner_id=?";
        return queryExecutor.executeQuery(sql, userId);
    }

    public void updateName(long userId, long projectId, String newName)
            throws SQLException, InterruptedException {
        String sql = "UPDATE projects SET name=? WHERE id=? AND owner_id=?";
        queryExecutor.executeUpdate(sql, newName, projectId, userId);
    }

    public void updateScene(long userId, long projectId, String sceneJson)
            throws SQLException, InterruptedException {
        String sql = "UPDATE projects SET scene=? WHERE id=? AND owner_id=?";
        queryExecutor.executeUpdate(sql, sceneJson, projectId, userId);
    }

    public void deleteProject(long userId, long projectId)
            throws SQLException, InterruptedException {
        String sql = "DELETE FROM projects WHERE id=? AND owner_id=?";
        queryExecutor.executeUpdate(sql, projectId, userId);
    }


    public List<Map<String,Object>> findScenesByProject(long projectId)
            throws SQLException, InterruptedException {
        String sql = "SELECT id,name,created_by,created_at FROM scenes WHERE project_id=?";
        return queryExecutor.executeQuery(sql, projectId);
    }

    public void updateSceneJson(long userId, long sceneId, String json)
            throws SQLException, InterruptedException {
        String sql = "UPDATE scenes SET scene_data=? WHERE id=?";
        queryExecutor.executeUpdate(sql, json, sceneId);
    }

    public void deleteScene(long userId, long sceneId)
            throws SQLException, InterruptedException {
        String sql = "DELETE FROM scenes WHERE id=?";
        queryExecutor.executeUpdate(sql, sceneId);
    }

}
