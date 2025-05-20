package io.github.sajge.server.projects.lists.projectncollab;

import java.util.List;

public record ListOwnedProjectsWithCollaboratorsResponseDto(
        boolean success,
        List<OwnedProjectWithCollaboratorsDto> projects,
        String message
) {}
