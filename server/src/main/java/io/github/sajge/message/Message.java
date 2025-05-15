package io.github.sajge.message;

public class Message {
    private Request type;
    private String payload;

    public Request getType() {
        return type;
    }

    public void setType(Request type) {
        this.type = type;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

}
