package io.proto.throwawaycode.exceptions;

public class ZeroVectorException extends MathException {
    public ZeroVectorException(String context) {
        super("Cannot normalize zero vector" + (context != null ? ": " + context : ""));
    }
}
