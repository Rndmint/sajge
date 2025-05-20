package io.github.sajge.server.projects.invites;

public record InviteCollaboratorResponseDto(
        boolean success,
        String accessKey,
        String message
) {}
