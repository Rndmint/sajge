package io.github.sajge.server.handler;

import io.github.sajge.message.Message;
import io.github.sajge.message.Request;

public interface Handler {
    String handle(Message msg);
}
