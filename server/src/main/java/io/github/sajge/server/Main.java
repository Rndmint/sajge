package io.github.sajge.server;

import io.github.sajge.server.core.CoreServer;

public class Main {
    public static void main(String[] args) throws Exception {
        CoreServer srv = new CoreServer();
        new Thread(srv).start();
    }
}
