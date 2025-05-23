package io.proto.throwawaycode.exceptions.math;

import io.proto.throwawaycode.exceptions.ZeroVectorException;

public class Vector2f {
    public float[] data;

    public Vector2f() {
        this.data = new float[2];
    }

    public Vector2f(float x, float y) {
        this.data = new float[]{x, y};
    }

    public Vector2f(float[] array) {
        if (array == null || array.length != 2)
            throw new IllegalArgumentException("Input array must have exactly 2 elements");
        this.data = new float[]{array[0], array[1]};
    }

    public Vector2f(Vector2f other) {
        this.data = new float[]{other.data[0], other.data[1]};
    }

    public Vector2f add(Vector2f v) {
        return new Vector2f(this.data[0] + v.data[0], this.data[1] + v.data[1]);
    }

    public Vector2f sub(Vector2f v) {
        return new Vector2f(this.data[0] - v.data[0], this.data[1] - v.data[1]);
    }

    public Vector2f scale(float scalar) {
        return new Vector2f(this.data[0] * scalar, this.data[1] * scalar);
    }

    public float dot(Vector2f v) {
        return this.data[0] * v.data[0] + this.data[1] * v.data[1];
    }

    public float length() {
        return (float) Math.sqrt(dot(this));
    }

    public Vector2f normalize() {
        float len = length();
        if (len == 0f) throw new ZeroVectorException("Vector2f: " + this);
        return scale(1.0f / len);
    }

    public float distance(Vector2f v) {
        float dx = this.data[0] - v.data[0];
        float dy = this.data[1] - v.data[1];
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    public Vector2f negate() {
        return scale(-1f);
    }

    public Vector3f toHomogeneous() {
        return new Vector3f(data[0], data[1], 1f);
    }

    public Vector3f toHomogeneous(float z) {
        return new Vector3f(data[0], data[1], z);
    }

    @Override
    public String toString() {
        return "[" + data[0] + ", " + data[1] + "]";
    }
}
