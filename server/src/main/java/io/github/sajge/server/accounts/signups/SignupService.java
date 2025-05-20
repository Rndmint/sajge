package io.github.sajge.server.accounts.signups;

import io.github.sajge.logger.Logger;
import io.github.sajge.server.security.Hash;
import io.github.sajge.server.security.Token;

import java.sql.SQLException;

public class SignupService {
    private static final Logger logger = Logger.get(SignupService.class);
    private final SignupDao dao;

    public SignupService(SignupDao dao) {
        this.dao = dao;
    }

    public void create(String username, String password) throws Exception {
        try {
            if (dao.userExists(username)) {
                logger.warn("Signup failed: username '{}' already exists", username);
                throw new SQLException("Username already exists");
            }
            String salt = Token.generate();
            String hashed_salted_password = String.valueOf(Hash.of(password + salt));
            dao.create(username, hashed_salted_password, salt);
            logger.info("User created: {}", username);
        } catch (Exception e) {
            logger.error("Error creating user: {}", username, e);
            throw e;
        }
    }
}
