package io.github.sajge.server.batch;

import java.util.*;
import java.util.concurrent.*;

public enum PingManager {
    INSTANCE;
    private final ConcurrentMap<String, Queue<String>> pings = new ConcurrentHashMap<>();

    public void ping(String username, String message) {
        pings.computeIfAbsent(username, u -> new ConcurrentLinkedQueue<>())
                .add(message);
    }

    public List<String> fetchPings(String username) {
        Queue<String> q = pings.getOrDefault(username, new ConcurrentLinkedQueue<>());
        List<String> list = new ArrayList<>(q);
        q.clear();
        return list;
    }
}
