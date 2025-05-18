package io.github.sajge.server.util;

public class Hash {
    public static String of(String str) {
        long hash = 5381;
        for (char c: str.toCharArray()) {
            hash = ((hash << 5) + hash) + c;
        }
        return Long.toString(hash);
    }
}
