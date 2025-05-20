package io.github.sajge.server.projects.deletes;

public record DeleteProjectDto(
        String token,
        long projectId
) {}

