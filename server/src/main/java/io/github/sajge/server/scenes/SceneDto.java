package io.github.sajge.server.scenes;

public record SceneDto(
        long id,
        String name,
        String createdBy,
        String createdAt
) {}

