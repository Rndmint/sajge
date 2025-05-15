package io.github.sajge.server.signups;

import io.github.sajge.logger.Logger;

public class SignupService {
    private static final Logger logger = Logger.get(SignupService.class);
    private final SignupDao dao;

    public SignupService(SignupDao dao) {
        this.dao = dao;
    }

    public void create(String username, String password) throws Exception {
        try {
            dao.create(username, password);
            logger.info("User created: {}", username);
        } catch (Exception e) {
            logger.error("Error creating user: {}", username, e);
            throw e;
        }
    }
}
