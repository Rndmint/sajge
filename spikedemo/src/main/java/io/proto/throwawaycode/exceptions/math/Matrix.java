package io.proto.throwawaycode.exceptions.math;

public interface Matrix<T extends Matrix<T>> {
    T add(T other);
    T sub(T other);
    T scale(float scalar);
    T multiply(T other);

    T transpose();
    float determinant();
    T inverse();

    void setIdentity();
}
