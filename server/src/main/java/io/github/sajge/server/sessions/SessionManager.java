package io.github.sajge.server.sessions;

import java.util.*;
import java.util.concurrent.*;

public enum SessionManager {
    INSTANCE;
    private final ConcurrentMap<String, String> sessions = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Set<Long>> openProjects = new ConcurrentHashMap<>();

    public void createSession(String username, String token) {
        sessions.put(token, username);
    }

    public boolean isValid(String token) {
        return token != null && sessions.containsKey(token);
    }

    public String getUsername(String token) {
        return sessions.get(token);
    }

    public void invalidateSession(String token) {
        sessions.remove(token);
        openProjects.remove(token);
    }

    public void openProject(String token, long projectId) {
        openProjects.computeIfAbsent(token, t -> ConcurrentHashMap.newKeySet())
                .add(projectId);
    }

    public Set<Long> getOpenProjects(String token) {
        return openProjects.getOrDefault(token, Collections.emptySet());
    }
}

