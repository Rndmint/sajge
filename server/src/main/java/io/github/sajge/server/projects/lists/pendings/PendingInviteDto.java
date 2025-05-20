package io.github.sajge.server.projects.lists.pendings;

public record PendingInviteDto(
        long   inviterId,
        String inviterUsername,
        long   projectId,
        String accessKey,
        String createdAt
) {}
