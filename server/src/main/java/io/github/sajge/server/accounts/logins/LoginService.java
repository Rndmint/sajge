package io.github.sajge.server.accounts.logins;

import io.github.sajge.logger.Logger;
import io.github.sajge.server.security.Hash;

import java.sql.SQLException;

public class LoginService {

    private static final Logger logger = Logger.get(LoginService.class);
    private final LoginDao dao;

    public LoginService(LoginDao dao) {
        this.dao = dao;
    }

    public boolean authenticate(String username, String password) {
        try {
            String salt = dao.getSalt(username);
            if (salt.isEmpty()) {
                throw new Exception();
            }
            String hash = String.valueOf(Hash.of(password + salt));
            boolean ok = dao.validate(username, hash);
            logger.info("Login attempt for {}: {}", username, ok);
            return ok;
        } catch (Exception e) {
            logger.error("Error during authentication for {}", username, e);
            return false;
        }
    }

    public long getUserId(String username) throws SQLException, InterruptedException {
        return dao.getUserId(username);
    }

}
