package io.github.sajge.server.batch;

public record OperationResult(
        OperationDto op,
        boolean success,
        Object result
) {}
