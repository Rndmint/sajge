package io.github.sajge.server.batch;

import com.fasterxml.jackson.databind.JsonNode;

public record OperationDto(
        String op,
        String entity,
        JsonNode data
) {}
