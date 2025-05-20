package io.github.sajge.server.accounts.deletes;

import io.github.sajge.database.QueryExecutor;
import io.github.sajge.logger.Logger;

import java.sql.SQLException;

public class DeleteDao{
    private static final Logger logger = Logger.get(DeleteDao.class);
    private final QueryExecutor queryExecutor;

    public DeleteDao() throws Exception {
        this.queryExecutor = new QueryExecutor();
        logger.debug("DeleteAccountDao initialized");
    }

    public int deleteByUsername(String username) throws SQLException, InterruptedException {
        String sql = "DELETE FROM users WHERE username = ?";
        logger.trace("Executing SQL: {}", sql);
        return queryExecutor.executeUpdate(sql, username);
    }
}
