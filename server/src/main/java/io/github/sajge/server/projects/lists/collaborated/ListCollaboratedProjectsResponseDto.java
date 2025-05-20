package io.github.sajge.server.projects.lists.collaborated;

import java.util.List;

public record ListCollaboratedProjectsResponseDto(
        boolean success,
        List<CollaboratedProjectDto> projects,
        String message
) {}
