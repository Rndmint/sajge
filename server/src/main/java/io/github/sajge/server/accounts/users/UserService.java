package io.github.sajge.server.accounts.users;

import io.github.sajge.logger.Logger;
import io.github.sajge.server.security.SessionManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserService {
    private static final Logger logger = Logger.get(UserService.class);
    private final UserDao dao;

    public UserService(UserDao dao) {
        this.dao = dao;
    }

    public List<UserDto> listUsers(String token) throws Exception {
        SessionManager.INSTANCE.getUserId(token);

        List<Map<String,Object>> rows = dao.listAllUsers();
        List<UserDto> users = new ArrayList<>(rows.size());
        for (Map<String,Object> row : rows) {
            users.add(new UserDto(
                    ((Number)row.get("id")).longValue(),
                    (String)row.get("username")
            ));
        }
        return users;
    }
}
