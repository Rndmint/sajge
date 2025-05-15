package io.github.sajge.server.patterns;

import io.github.sajge.messages.Envelope;

public interface Handler<T> {
    String handle(T msg);
}
