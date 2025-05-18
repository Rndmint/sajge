package io.github.sajge.server.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sajge.logger.Logger;
import io.github.sajge.server.dto.Project;
import io.github.sajge.server.dto.User;
import io.github.sajge.server.net.Request;
import io.github.sajge.server.net.Response;
import io.github.sajge.server.service.AccountService;
import io.github.sajge.server.service.ProjectService;

import java.sql.Connection;

public class ServiceRouter {
    private static final Logger log = Logger.get(ServiceRouter.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    public static Response handle(Request request) {
        Connection connection = ServiceContext.acquireConnection();
        try {
            String action = request.getAction();
            String payload = request.getPayload();
            log.debug("Routing action: {}", action);

            switch (action) {
                case "createAccount":
                    return AccountService.createAccount(connection, payload);

                case "login":
                    return AccountService.login(connection, payload);

                case "getProjects": {
                    User user = AccountService.authenticate(connection, payload);
                    if (user == null) {
                        return error("Authentication failed");
                    }
                    return ProjectService.getProjects(connection, user);
                }

                case "updateProject": {
                    JsonNode node = mapper.readTree(payload);
                    String token = node.get("token").asText();
                    JsonNode projectNode = node.get("project");
                    User user = AccountService.authenticate(connection, token);
                    if (user == null) {
                        return error("Authentication failed");
                    }
                    Project project = mapper.treeToValue(projectNode, Project.class);
                    return ProjectService.updateProject(connection, user, project);
                }

                default:
                    return error("Unknown action: " + action);
            }

        } catch (Exception e) {
            log.error("Error routing request", e);
            return error(e.getMessage());
        }
    }

    private static Response error(String message) {
        Response response = new Response();
        response.setSuccess(false);
        response.setError(message);
        return response;
    }
}
