package io.github.sajge.server.projects.deletes;

public record DeleteProjectResponseDto(
        boolean success,
        String message
) {}
