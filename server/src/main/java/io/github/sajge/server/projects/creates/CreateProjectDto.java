package io.github.sajge.server.projects.creates;

public record CreateProjectDto(
        String token,
        String name,
        String description,
        String sceneJson
) {}
