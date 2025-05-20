package io.github.sajge.server.projects.invites.refuses;

public record RefuseInviteResponseDto(
        boolean success,
        String message
) {}
