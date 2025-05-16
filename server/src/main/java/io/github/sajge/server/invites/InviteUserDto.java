package io.github.sajge.server.invites;

public record InviteUserDto(
        String token,
        String projectId,
        String inviteeUsername
) {}
