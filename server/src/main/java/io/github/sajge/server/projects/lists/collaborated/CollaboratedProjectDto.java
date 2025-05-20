package io.github.sajge.server.projects.lists.collaborated;

public record CollaboratedProjectDto(
        long   id,
        String name,
        String description,
        String sceneJson,
        long   ownerId,
        String createdAt
) {}
