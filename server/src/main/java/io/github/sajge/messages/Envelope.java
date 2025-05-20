package io.github.sajge.messages;

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
