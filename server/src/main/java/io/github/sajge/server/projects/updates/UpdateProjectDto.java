package io.github.sajge.server.projects.updates;

public record UpdateProjectDto(
        String token,
        long projectId,
        String name,
        String description,
        String sceneJson
) {}
