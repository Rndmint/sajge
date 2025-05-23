package io.proto.throwawaycode.exceptions.math;

import io.proto.throwawaycode.exceptions.NonInvertibleHomogeneousCoordinateException;
import io.proto.throwawaycode.exceptions.ZeroVectorException;

public class Vector3f {
    public float[] data;

    public Vector3f() {
        this.data = new float[3];
    }

    public Vector3f(float x, float y, float z) {
        this.data = new float[]{x, y, z};
    }

    public Vector3f(float[] array) {
        if (array == null || array.length != 3)
            throw new IllegalArgumentException("Input array must have exactly 3 elements");
        this.data = new float[]{array[0], array[1], array[2]};
    }

    public Vector3f(Vector3f other) {
        this.data = new float[]{other.data[0], other.data[1], other.data[2]};
    }

    public Vector3f add(Vector3f v) {
        return new Vector3f(
                this.data[0] + v.data[0],
                this.data[1] + v.data[1],
                this.data[2] + v.data[2]
        );
    }

    public Vector3f sub(Vector3f v) {
        return new Vector3f(
                this.data[0] - v.data[0],
                this.data[1] - v.data[1],
                this.data[2] - v.data[2]
        );
    }

    public Vector3f scale(float scalar) {
        return new Vector3f(
                this.data[0] * scalar,
                this.data[1] * scalar,
                this.data[2] * scalar
        );
    }

    public float dot(Vector3f v) {
        return this.data[0] * v.data[0]
                + this.data[1] * v.data[1]
                + this.data[2] * v.data[2];
    }

    public Vector3f cross(Vector3f v) {
        float x = this.data[1] * v.data[2] - this.data[2] * v.data[1];
        float y = this.data[2] * v.data[0] - this.data[0] * v.data[2];
        float z = this.data[0] * v.data[1] - this.data[1] * v.data[0];
        return new Vector3f(x, y, z);
    }

    public float length() {
        return (float) Math.sqrt(dot(this));
    }

    public Vector3f normalize() {
        float len = length();
        if (len == 0f) throw new ZeroVectorException("Vector3f: " + this);
        return scale(1.0f / len);
    }

    public Vector3f negate() {
        return scale(-1f);
    }

    public float distance(Vector3f v) {
        float dx = this.data[0] - v.data[0];
        float dy = this.data[1] - v.data[1];
        float dz = this.data[2] - v.data[2];
        return (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    public Vector4f toHomogeneous() {
        return new Vector4f(data[0], data[1], data[2], 1f);
    }

    public Vector4f toHomogeneous(float w) {
        return new Vector4f(data[0], data[1], data[2], w);
    }

    public Vector2f toCartesian2f() {
        if (data[2] == 0f) {
            throw new NonInvertibleHomogeneousCoordinateException("Vector3f has z = 0: " + this);
        }
        float invZ = 1f / data[2];
        return new Vector2f(
                data[0] * invZ,
                data[1] * invZ
        );
    }

    @Override
    public String toString() {
        return "[" + data[0] + ", " + data[1] + ", " + data[2] + "]";
    }
}
