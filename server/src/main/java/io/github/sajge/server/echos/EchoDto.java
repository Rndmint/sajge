package io.github.sajge.server.echos;

import com.fasterxml.jackson.annotation.JsonCreator;

public record EchoDto(String message) {
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public EchoDto {
    }
}

