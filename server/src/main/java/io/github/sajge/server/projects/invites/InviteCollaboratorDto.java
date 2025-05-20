package io.github.sajge.server.projects.invites;

public record InviteCollaboratorDto(
        String token,
        long projectId,
        long collaboratorUserId,
        boolean canEdit
) {}
