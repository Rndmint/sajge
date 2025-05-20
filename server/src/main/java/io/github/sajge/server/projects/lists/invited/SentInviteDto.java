package io.github.sajge.server.projects.lists.invited;

public record SentInviteDto(
        long   invitedUserId,
        String invitedUsername,
        long   projectId,
        String accessKey,
        String status,
        String createdAt
) {}
