package io.github.sajge.message;

import com.fasterxml.jackson.databind.JsonNode;

public class Message {
    private Request type;
    private JsonNode payload;

    public Request getType() {
        return type;
    }

    public void setType(Request type) {
        this.type = type;
    }

    public JsonNode getPayload() {
        return payload;
    }

    public void setPayload(JsonNode payload) {
        this.payload = payload;
    }

}
