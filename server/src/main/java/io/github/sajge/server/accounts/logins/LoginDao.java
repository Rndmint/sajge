package io.github.sajge.server.accounts.logins;

import io.github.sajge.database.QueryExecutor;
import io.github.sajge.logger.Logger;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class LoginDao {
    private static final Logger logger = Logger.get(LoginDao.class);
    private final QueryExecutor queryExecutor;

    public LoginDao() throws Exception {
        this.queryExecutor = new QueryExecutor();
        logger.debug("LoginDao initialized");
    }

    public boolean validate(String username, String hash)
            throws SQLException, InterruptedException {
        String sql = "SELECT hash FROM users WHERE username = ?";
        logger.trace("Executing SQL: {}", sql);

        List<Map<String, Object>> rows = queryExecutor.executeQuery(sql, username);

        Object storedObj = rows.getFirst().get("hash");
        if (storedObj == null) {
            return false;
        }

        String stored = storedObj.toString();
        return stored.equals(hash);
    }

    public String getSalt(String username)
            throws SQLException, InterruptedException {
        String sql = "SELECT salt FROM users WHERE username = ?";
        logger.trace("Executing SQL: {}", sql);

        List<Map<String, Object>> rows = queryExecutor.executeQuery(sql, username);
        if (rows.isEmpty()) {
            return "";
        }

        Object storedObj = rows.getFirst().get("salt");
        if (storedObj == null) {
            return "";
        }

        return storedObj.toString();
    }

    public long getUserId(String username) throws SQLException, InterruptedException {
        String sql = "SELECT id FROM users WHERE username = ?";
        logger.trace("Executing SQL: {}", sql);

        List<Map<String,Object>> rows = queryExecutor.executeQuery(sql, username);
        if (rows.isEmpty()) {
            throw new SQLException("User not found: " + username);
        }

        Object idObj = rows.get(0).get("id");
        if (!(idObj instanceof Number)) {
            throw new SQLException("Invalid ID value for user: " + username);
        }
        return ((Number) idObj).longValue();
    }

}
