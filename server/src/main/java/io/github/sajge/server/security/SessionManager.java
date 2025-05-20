package io.github.sajge.server.security;

import io.github.sajge.logger.Logger;
import io.github.sajge.server.sessionchecks.CheckSessionHandler;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum SessionManager {
    INSTANCE;

    private static final Logger logger = Logger.get(SessionManager.class);

    private record SessionInfo(long userId, Instant createdAt) {}

    private final Map<String, SessionInfo> sessions = new ConcurrentHashMap<>();

    public void register(String token, long userId) {
        sessions.put(token, new SessionInfo(userId, Instant.now()));
    }

    public boolean isValid(String token) {
        return sessions.containsKey(token);
    }

    public long getUserId(String token) {
        SessionInfo info = sessions.get(token);
        if (info == null) {
            throw new IllegalArgumentException("Invalid session token");
        }
        return info.userId();
    }

    public void invalidate(String token) {
        sessions.remove(token);
    }

    public void prune(long maxAgeSeconds) {
        // If I didn't use it I just didn't have time
        Instant cutoff = Instant.now().minusSeconds(maxAgeSeconds);
        sessions.entrySet().removeIf(e -> e.getValue().createdAt().isBefore(cutoff));
    }
}
