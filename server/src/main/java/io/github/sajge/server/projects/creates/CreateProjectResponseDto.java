package io.github.sajge.server.projects.creates;

public record CreateProjectResponseDto(
        boolean success,
        long projectId,
        String message
) {}
