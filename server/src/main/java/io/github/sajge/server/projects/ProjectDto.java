package io.github.sajge.server.projects;

import com.fasterxml.jackson.databind.JsonNode;

public record ProjectDto(
        long id,
        String name,
        JsonNode scene
) {}
