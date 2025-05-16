package io.github.sajge.server.batch;

import java.util.List;

public record BatchDto(
        String token,
        List<OperationDto> ops
) {}

