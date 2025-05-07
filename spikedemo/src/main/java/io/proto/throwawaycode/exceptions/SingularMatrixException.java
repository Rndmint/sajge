package io.proto.throwawaycode.exceptions;

public class SingularMatrixException extends MathException {
    public SingularMatrixException(String context) {
        super("Matrix is singular and cannot be inverted" + (context != null ? ": " + context : ""));
    }
}
