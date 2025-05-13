package io.proto.throwawaycode.exceptions;

public class NonInvertibleHomogeneousCoordinateException extends MathException {
    public NonInvertibleHomogeneousCoordinateException(String context) {
        super("Cannot convert to Cartesian coordinates due to division by zero" +
                (context != null ? ": " + context : ""));
    }
}
