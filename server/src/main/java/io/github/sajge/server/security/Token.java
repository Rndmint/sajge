package io.github.sajge.server.security;

import java.util.UUID;

public class Token {
    public static String generate() {
        return UUID.randomUUID().toString();
    }
}
