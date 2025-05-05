package io.proto.spike.math.exceptions;

public class SingularMatrixException extends RuntimeException {
    public SingularMatrixException(String context) {
        super("Matrix is singular and cannot be inverted" + (context != null ? ": " + context : ""));
    }
}
