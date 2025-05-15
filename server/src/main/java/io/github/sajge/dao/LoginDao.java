package io.github.sajge.dao;

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

    public boolean validate(String username, String password) throws SQLException, InterruptedException {
        String sql = "SELECT password FROM users WHERE username = ?";
        logger.trace("Executing SQL: {}", sql);

        List<Map<String, Object>> rows = queryExecutor.executeQuery(sql, username);
        if (rows.isEmpty()) {
            return false;
        }

        Object storedObj = rows.get(0).get("password");
        if (storedObj == null) {
            return false;
        }

        String stored = storedObj.toString();
        return stored.equals(password);
    }

}
