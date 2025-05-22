package io.github.sajge.server.echos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record EchoDto(@JsonProperty("message") String message) {
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public EchoDto {
    }
}

