package io.github.sajge.server.projects.lists;

import io.github.sajge.server.projects.ProjectDto;
import java.util.List;

public record ListOwnedProjectsResponseDto(
        boolean success,
        List<ProjectDto> projects,
        String message
) {}
