package io.github.sajge.server.accounts.deletes;

import io.github.sajge.logger.Logger;
import io.github.sajge.server.security.SessionManager;

public class DeleteService {
    private static final Logger logger = Logger.get(DeleteService.class);
    private final DeleteDao dao;

    public DeleteService(DeleteDao dao) {
        this.dao = dao;
    }

    public boolean deleteAccount(String username, String token) throws Exception {
        if (!SessionManager.INSTANCE.isValid(token)) {
            logger.warn("Invalid token for delete request: {}", token);
            return false;
        }

        int rows = dao.deleteByUsername(username);
        boolean ok = rows > 0;
        logger.info("Account deletion for '{}': {}", username, ok);
        if (ok) {
            SessionManager.INSTANCE.invalidate(token);
        }
        return ok;
    }
}
