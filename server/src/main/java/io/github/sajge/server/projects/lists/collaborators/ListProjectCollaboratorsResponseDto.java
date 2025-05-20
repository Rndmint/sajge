package io.github.sajge.server.projects.lists.collaborators;

import java.util.List;

public record ListProjectCollaboratorsResponseDto(
        boolean success,
        List<CollaboratorDto> collaborators,
        String message
) {}
