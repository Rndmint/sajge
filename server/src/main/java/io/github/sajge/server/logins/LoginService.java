package io.github.sajge.server.logins;

import io.github.sajge.logger.Logger;

public class LoginService {

    private static final Logger logger = Logger.get(LoginService.class);
    private final LoginDao dao;

    public LoginService(LoginDao dao) {
        this.dao = dao;
    }

    public boolean authenticate(String username, String password) {
        try {
            boolean ok = dao.validate(username, password);
            logger.info("Login attempt for {}: {}", username, ok);
            return ok;
        } catch (Exception e) {
            logger.error("Error during authentication for {}", username, e);
            return false;
        }
    }

}
