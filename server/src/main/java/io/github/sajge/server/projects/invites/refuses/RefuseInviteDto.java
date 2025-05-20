package io.github.sajge.server.projects.invites.refuses;

public record RefuseInviteDto(
        String token,
        String accessKey
) {}
