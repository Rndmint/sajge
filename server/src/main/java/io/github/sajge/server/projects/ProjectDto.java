package io.github.sajge.server.projects;

public record ProjectDto(
        long id,
        String name,
        String description,
        long ownerId,
        String sceneJson,
        String createdAt
) {}
