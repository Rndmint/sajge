package io.github.sajge.server.invites;

import io.github.sajge.database.QueryExecutor;
import io.github.sajge.logger.Logger;

import java.sql.SQLException;

public class InviteDao {
    private static final Logger logger = Logger.get(InviteDao.class);
    private final QueryExecutor queryExecutor;

    public InviteDao() throws Exception {
        this.queryExecutor = new QueryExecutor();
        logger.debug("InviteDao initialized");
    }

    public void inviteUser(String inviter, long projectId, String invitee)
            throws SQLException, InterruptedException {
        String sql = "INSERT INTO project_permissions (user_id, project_id, status, access_key)"
                + " VALUES ((SELECT id FROM users WHERE username=?), ?, 'pending', UUID())";
        queryExecutor.executeUpdate(sql, invitee, projectId);
        logger.info("User {} invited {} to project {}", inviter, invitee, projectId);
    }
}
