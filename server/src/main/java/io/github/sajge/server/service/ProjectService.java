package io.github.sajge.server.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sajge.logger.Logger;
import io.github.sajge.server.dto.Project;
import io.github.sajge.server.dto.User;
import io.github.sajge.server.net.Response;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProjectService {
    private static final Logger log = Logger.get(ProjectService.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    public static Response getProjects(Connection connection, User user) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT id, name, data FROM projects WHERE owner_id = (SELECT id FROM users WHERE username = ?)"
            );
            stmt.setString(1, user.getUsername());
            ResultSet rs = stmt.executeQuery();

            List<Project> projects = new ArrayList<>();
            while (rs.next()) {
                Project project = new Project();
                project.setId(rs.getInt("id"));
                project.setName(rs.getString("name"));
                project.setData(rs.getString("data"));
                projects.add(project);
            }

            log.info("Retrieved {} projects for user {}", projects.size(), user.getUsername());
            return success(projects);

        } catch (Exception e) {
            log.error("Error retrieving projects for user {}", user.getUsername(), e);
            return error(e.getMessage());
        }
    }

    public static Response updateProject(Connection connection, User user, Project project) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "UPDATE projects p JOIN users u ON p.owner_id = u.id SET p.name = ?, p.data = ? WHERE p.id = ? AND u.username = ?"
            );
            stmt.setString(1, project.getName());
            stmt.setString(2, project.getData());
            stmt.setInt(3, project.getId());
            stmt.setString(4, user.getUsername());

            int updatedRows = stmt.executeUpdate();
            if (updatedRows == 0) {
                return error("Project not found or unauthorized");
            }

            log.info("Updated project {} for user {}", project.getId(), user.getUsername());
            return success(null);

        } catch (Exception e) {
            log.error("Error updating project {} for user {}", project.getId(), user.getUsername(), e);
            return error(e.getMessage());
        }
    }

    private static Response success(Object data) {
        try {
            Response response = new Response();
            response.setSuccess(true);
            if (data != null) {
                response.setBody(mapper.writeValueAsString(data));
            }
            return response;
        } catch (Exception e) {
            log.error("Error serializing success response", e);
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
