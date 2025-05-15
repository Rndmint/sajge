package io.github.sajge.messages;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.sajge.messages.resquests.RequestType;

public class Envelope<T, P> {

    private T type;
    private P payload;

    public T getType() {
        return type;
    }

    public void setType(T type) {
        this.type = type;
    }

    public P getPayload() {
        return payload;
    }

    public void setPayload(P payload) {
        this.payload = payload;
    }

}
