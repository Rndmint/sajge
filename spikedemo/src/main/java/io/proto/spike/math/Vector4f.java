package io.proto.spike.math;

public class Vector4f {
    public final float[] data;

    public Vector4f() {
        this.data = new float[4];
    }

    public Vector4f(float x, float y, float z, float w) {
        this.data = new float[]{x, y, z, w};
    }

    public Vector4f(Vector4f other) {
        this.data = new float[]{other.data[0], other.data[1], other.data[2], other.data[3]};
    }

    public Vector4f add(Vector4f v) {
        float[] result = new float[4];
        for (int i = 0; i < 4; i++) {
            result[i] = this.data[i] + v.data[i];
        }
        return new Vector4f(result[0], result[1], result[2], result[3]);
    }

    public Vector4f sub(Vector4f v) {
        float[] result = new float[4];
        for (int i = 0; i < 4; i++) {
            result[i] = this.data[i] - v.data[i];
        }
        return new Vector4f(result[0], result[1], result[2], result[3]);
    }

    public Vector4f scale(float scalar) {
        float[] result = new float[4];
        for (int i = 0; i < 4; i++) {
            result[i] = this.data[i] * scalar;
        }
        return new Vector4f(result[0], result[1], result[2], result[3]);
    }

    public float dot(Vector4f v) {
        float sum = 0;
        for (int i = 0; i < 4; i++) {
            sum += this.data[i] * v.data[i];
        }
        return sum;
    }

    public float length() {
        return (float) Math.sqrt(dot(this));
    }

    public Vector4f normalize() {
        float len = length();
        return len != 0 ? scale(1.0f / len) : this;
    }

    public Vector4f negate() {
        return scale(-1f);
    }

    public float distance(Vector4f v) {
        float sum = 0;
        for (int i = 0; i < 4; i++) {
            float d = this.data[i] - v.data[i];
            sum += d * d;
        }
        return (float) Math.sqrt(sum);
    }

    @Override
    public String toString() {
        return "[" + data[0] + ", " + data[1] + ", " + data[2] + ", " + data[3] + "]";
    }
}
