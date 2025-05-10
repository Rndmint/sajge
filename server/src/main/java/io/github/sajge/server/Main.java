package io.github.sajge.server;

import io.github.sajge.logger.Logger;

public class Main {
    private static final Logger log = Logger.get(Main.class);
    public static void main(String[] args) {
        for (int i = 1; i <= 5; i++) {
            log.info("Iter {}: I am server", i);
        }
    }
}
