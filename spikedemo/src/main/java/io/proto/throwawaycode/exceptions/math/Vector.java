package io.proto.throwawaycode.exceptions.math;

public interface Vector<T extends Vector<T>> {
    T add(T other);
    T sub(T other);
    T scale(float scalar);
    float dot(T other);

    float length();
    T normalize();
    T negate();
    float distance(T other);

    default boolean floatEquals(float a, float b) {
        return Math.abs(a - b) < 1e-6f;
    }

    default int hashFloat(float value) {
        return Float.hashCode(Math.round(value * 1e4f) / 1e4f);
    }

}
