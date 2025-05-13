package io.github.sajge.server.core.comm;

import java.net.Socket;

public class Request {
    private final Socket client;
    private final String payload;
    private boolean authenticated;

    public Request(Socket client, String payload) {
        this.client = client;
        this.payload = payload;
        this.authenticated = false;
    }

    public Socket getClient() {
        return client;
    }

    public String getPayload() {
        return payload;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }
}
