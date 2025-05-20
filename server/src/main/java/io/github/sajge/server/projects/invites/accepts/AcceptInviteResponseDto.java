package io.github.sajge.server.projects.invites.accepts;

public record AcceptInviteResponseDto(
        boolean success,
        String message
) {}
