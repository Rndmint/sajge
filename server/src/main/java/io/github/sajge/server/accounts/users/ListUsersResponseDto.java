package io.github.sajge.server.accounts.users;

import java.util.List;

public record ListUsersResponseDto(
        boolean success,
        List<UserDto> users,
        String message
) {}
