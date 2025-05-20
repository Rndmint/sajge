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

    public long createProject(String name,
                              String description,
                              String sceneJson,
                              long ownerId)
            throws SQLException, InterruptedException {
        String sql = "INSERT INTO projects (name, description, scene, owner_id) VALUES (?, ?, ?, ?)";
        logger.trace("SQL: {}", sql);

        QueryExecutor.UpdateResult result =
                queryExecutor.executeUpdateWithKeys(sql, name, description, sceneJson, ownerId);

        if (result.getGeneratedKeys().isEmpty()) {
            throw new SQLException("Creating project failed, no ID obtained.");
        }
        return result.getGeneratedKeys().get(0);
    }

    public int updateProject(long projectId,
                             String name,
                             String description,
                             String sceneJson)
            throws SQLException, InterruptedException {
        String sql = "UPDATE projects SET name = ?, description = ?, scene = ? WHERE id = ?";
        logger.trace("SQL: {}", sql);
        return queryExecutor.executeUpdate(sql, name, description, sceneJson, projectId);
    }

    public int deleteProject(long projectId)
            throws SQLException, InterruptedException {
        String sql = "DELETE FROM projects WHERE id = ?";
        logger.trace("SQL: {}", sql);
        return queryExecutor.executeUpdate(sql, projectId);
    }

    public Map<String, Object> findProjectById(long projectId)
            throws SQLException, InterruptedException {
        String sql = "SELECT * FROM projects WHERE id = ?";
        logger.trace("SQL: {}", sql);
        List<Map<String, Object>> rows = queryExecutor.executeQuery(sql, projectId);
        return rows.isEmpty() ? null : rows.get(0);
    }

    public int addPermission(long projectId,
                             long userId,
                             boolean canEdit,
                             String accessKey)
            throws SQLException, InterruptedException {
        String sql = "INSERT INTO project_permissions (user_id, project_id, can_edit, access_key) VALUES (?, ?, ?, ?)";
        logger.trace("SQL: {}", sql);
        return queryExecutor.executeUpdate(sql, userId, projectId, canEdit, accessKey);
    }

    public int deletePermission(long projectId, long userId)
            throws SQLException, InterruptedException {
        String sql = "DELETE FROM project_permissions WHERE project_id = ? AND user_id = ?";
        logger.trace("SQL: {}", sql);
        return queryExecutor.executeUpdate(sql, projectId, userId);
    }

    public Map<String, Object> findPermission(long projectId, long userId)
            throws SQLException, InterruptedException {
        String sql = "SELECT * FROM project_permissions WHERE project_id = ? AND user_id = ?";
        logger.trace("SQL: {}", sql);
        List<Map<String, Object>> rows = queryExecutor.executeQuery(sql, projectId, userId);
        return rows.isEmpty() ? null : rows.get(0);
    }

    public Map<String, Object> findPermissionByAccessKey(String accessKey)
            throws SQLException, InterruptedException {
        String sql = "SELECT * FROM project_permissions WHERE access_key = ?";
        logger.trace("SQL: {}", sql);
        List<Map<String, Object>> rows = queryExecutor.executeQuery(sql, accessKey);
        return rows.isEmpty() ? null : rows.get(0);
    }

    public int updatePermissionStatus(long projectId,
                                      String accessKey,
                                      String status)
            throws SQLException, InterruptedException {
        String sql = "UPDATE project_permissions SET status = ? WHERE project_id = ? AND access_key = ?";
        logger.trace("SQL: {}", sql);
        return queryExecutor.executeUpdate(sql, status, projectId, accessKey);
    }

    public List<Map<String, Object>> listProjectsByOwner(long ownerId)
            throws SQLException, InterruptedException {
        String sql = "SELECT * FROM projects WHERE owner_id = ?";
        logger.trace("SQL: {}", sql);
        return queryExecutor.executeQuery(sql, ownerId);
    }

    public List<Map<String,Object>> listPendingInvitesForUser(long userId)
            throws SQLException, InterruptedException {
        String sql =
                "SELECT u.id   AS inviter_id,  " +
                        "       u.username AS inviter_username,  " +
                        "       pp.project_id,               " +
                        "       pp.access_key,               " +
                        "       pp.created_at                " +
                        "  FROM project_permissions pp     " +
                        "  JOIN projects p  ON pp.project_id = p.id  " +
                        "  JOIN users u     ON p.owner_id     = u.id " +
                        " WHERE pp.user_id    = ?           " +
                        "   AND pp.status     = 'pending'";
        logger.trace("SQL: {}", sql);
        return queryExecutor.executeQuery(sql, userId);
    }
}
