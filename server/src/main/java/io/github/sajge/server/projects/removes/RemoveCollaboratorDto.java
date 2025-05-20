package io.github.sajge.server.projects.removes;

public record RemoveCollaboratorDto(
        String token,
        long projectId,
        long collaboratorUserId
) {}
