package io.github.sajge.server.projects.lists.invited;

import java.util.List;

public record ListSentInvitesResponseDto(
        boolean success,
        List<SentInviteDto> invites,
        String message
) {}
