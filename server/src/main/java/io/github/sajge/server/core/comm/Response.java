package io.github.sajge.server.core.comm;

public class Response {
    private final String payload;

    public Response(String payload) {
        this.payload = payload;
    }

    public String getPayload() {
        return payload;
    }
}
