package io.github.sajge.server.logins;

public record LoginResponseDto(boolean success, String token, String message) {}
