package io.github.sajge.server.signups;

import io.github.sajge.database.QueryExecutor;
import io.github.sajge.logger.Logger;
import java.sql.SQLException;

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

    public int create(String username, String password)
            throws SQLException, InterruptedException {
        logger.debug("Attempting to create user: {}", username);

        try {
            String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
            logger.trace("Preparing SQL statement: {}", sql);

            int result = queryExecutor.executeUpdate(sql, username, password);
            logger.debug("User created = {}, username = {}", result > 0, username);

            return result;







        } catch (SQLException e) {
            logger.error("Database error while creating user: {}", username, e);
            throw e;
        } catch (InterruptedException e) {
            logger.error("Thread interrupted while creating user: {}", username, e);
            Thread.currentThread().interrupt();
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error while creating user: {}", username, e);
            throw e;
        }
    }
}
