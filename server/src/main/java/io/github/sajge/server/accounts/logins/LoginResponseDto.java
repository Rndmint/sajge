package io.github.sajge.server.accounts.logins;

public record LoginResponseDto(boolean success, String token, String message) {}
