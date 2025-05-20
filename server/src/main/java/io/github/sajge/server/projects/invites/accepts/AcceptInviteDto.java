package io.github.sajge.server.projects.invites.accepts;

public record AcceptInviteDto(
        String token,
        String accessKey
) {}

