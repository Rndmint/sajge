package io.github.sajge.server.accounts.signups;

import io.github.sajge.database.QueryExecutor;
import io.github.sajge.logger.Logger;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class SignupDao {
    private static final Logger logger = Logger.get(SignupDao.class);

    private final QueryExecutor queryExecutor;

    public SignupDao() throws Exception {
        try {
            this.queryExecutor = new QueryExecutor();
            logger.debug("SignupDao initialized successfully");
        } catch (Exception e) {
            logger.error("Failed to initialize SignupDao", e);
            throw e;
        }
    }

    public int create(String username, String hash, String salt)
            throws SQLException, InterruptedException {
        logger.debug("Attempting to create user: {}", username);

        try {
            String sql = "INSERT INTO users (username, hash, salt) VALUES (?, ?, ?)";
            logger.trace("Preparing SQL statement: {}", sql);

            int result = queryExecutor.executeUpdate(sql, username, hash, salt);
            logger.debug("User created = {}, username = {}", result > 0, username);

            return result;

        } catch (SQLException e) {
            logger.error("Database error while creating user: {}", username, e);
            throw e;
        } catch (InterruptedException e) {
            logger.error("Thread interrupted while creating user: {}", username, e);
            throw new RuntimeException(e);
        } catch (Exception e) {
            logger.error("Unexpected error while creating user: {}", username, e);
            throw e;
        }
    }

    public boolean userExists(String username) throws SQLException, InterruptedException {
        String sql = "SELECT COUNT(*) AS cnt FROM users WHERE username = ?";
        List<Map<String,Object>> rows = queryExecutor.executeQuery(sql, username);

        if (rows.isEmpty()) {
            return false;
        }

        Object cntObj = rows.get(0).get("cnt");
        if (!(cntObj instanceof Number)) {
            return false;
        }
        int count = ((Number) cntObj).intValue();
        return count > 0;
    }
}
