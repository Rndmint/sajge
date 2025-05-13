package io.proto.throwawaycode.exceptions.math;

import io.proto.throwawaycode.exceptions.NonInvertibleHomogeneousCoordinateException;
import io.proto.throwawaycode.exceptions.ZeroVectorException;

public class Vector4f {
    public float[] data;

    public Vector4f() {
        this.data = new float[4];
    }

    public Vector4f(float x, float y, float z, float w) {
        this.data = new float[]{x, y, z, w};
    }

    public Vector4f(float[] array) {
        if (array == null || array.length != 4)
            throw new IllegalArgumentException("Input array must have exactly 4 elements");
        this.data = new float[]{array[0], array[1], array[2], array[3]};
    }

    public Vector4f(Vector4f other) {
        this.data = new float[]{other.data[0], other.data[1], other.data[2], other.data[3]};
    }

    public Vector4f add(Vector4f v) {
        return new Vector4f(
                this.data[0] + v.data[0],
                this.data[1] + v.data[1],
                this.data[2] + v.data[2],
                this.data[3] + v.data[3]
        );
    }

    public Vector4f sub(Vector4f v) {
        return new Vector4f(
                this.data[0] - v.data[0],
                this.data[1] - v.data[1],
                this.data[2] - v.data[2],
                this.data[3] - v.data[3]
        );
    }

    public Vector4f scale(float scalar) {
        return new Vector4f(
                this.data[0] * scalar,
                this.data[1] * scalar,
                this.data[2] * scalar,
                this.data[3] * scalar
        );
    }

    public float dot(Vector4f v) {
        return this.data[0] * v.data[0]
                + this.data[1] * v.data[1]
                + this.data[2] * v.data[2]
                + this.data[3] * v.data[3];
    }

    public float length() {
        return (float) Math.sqrt(dot(this));
    }

    public Vector4f normalize() {
        float len = length();
        if (len == 0f) throw new ZeroVectorException("Vector4f: " + this);
        return scale(1.0f / len);
    }

    public Vector4f negate() {
        return scale(-1f);
    }

    public float distance(Vector4f v) {
        float dx = this.data[0] - v.data[0];
        float dy = this.data[1] - v.data[1];
        float dz = this.data[2] - v.data[2];
        float dw = this.data[3] - v.data[3];
        return (float) Math.sqrt(dx * dx + dy * dy + dz * dz + dw * dw);
    }

    public Vector3f toCartesian3f() {
        if (data[3] == 0f) {
            throw new NonInvertibleHomogeneousCoordinateException("Vector4f has w = 0: " + this);
        }
        float invW = 1f / data[3];
        return new Vector3f(
                data[0] * invW,
                data[1] * invW,
                data[2] * invW
        );
    }

    @Override
    public String toString() {
        return "[" + data[0] + ", " + data[1] + ", " + data[2] + ", " + data[3] + "]";
    }
}
