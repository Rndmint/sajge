package io.github.sajge.server.handler;

import io.github.sajge.message.Message;
import io.github.sajge.message.Request;

public class EchoHandler implements Handler {
    @Override
    public String handle(Message msg) {
        return msg.getPayload();
    }
}
