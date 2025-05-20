package io.github.sajge.server.projects.lists.pendings;

import java.util.List;

public record ListPendingInvitesResponseDto(
        boolean success,
        List<PendingInviteDto> invites,
        String message
) {}
