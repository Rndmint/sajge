package io.proto.spike.math.exceptions;

public class ZeroVectorException extends RuntimeException {
    public ZeroVectorException(String context) {
        super("Cannot normalize zero vector" + (context != null ? ": " + context : ""));
    }
}
