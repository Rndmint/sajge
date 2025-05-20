package io.github.sajge.server.accounts.users;

import io.github.sajge.database.QueryExecutor;
import io.github.sajge.logger.Logger;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class UserDao {
    private static final Logger logger = Logger.get(UserDao.class);
    private final QueryExecutor queryExecutor;

    public UserDao() throws Exception {
        this.queryExecutor = new QueryExecutor();
        logger.debug("UserDao initialized");
    }

    public List<Map<String,Object>> listAllUsers()
            throws SQLException, InterruptedException {
        String sql = "SELECT id, username FROM users";
        logger.trace("SQL: {}", sql);
        return queryExecutor.executeQuery(sql);
    }
}
