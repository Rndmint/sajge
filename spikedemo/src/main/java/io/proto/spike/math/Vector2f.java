package io.proto.spike.math;

public class Vector2f {
    public final float[] data;

    public Vector2f() {
        this.data = new float[2];
    }

    public Vector2f(float x, float y) {
        this.data = new float[]{x, y};
    }

    public Vector2f(Vector2f other) {
        this.data = new float[]{other.data[0], other.data[1]};
    }

    public Vector2f add(Vector2f v) {
        float[] result = new float[2];
        for (int i = 0; i < 2; i++) {
            result[i] = this.data[i] + v.data[i];
        }
        return new Vector2f(result[0], result[1]);
    }

    public Vector2f sub(Vector2f v) {
        float[] result = new float[2];
        for (int i = 0; i < 2; i++) {
            result[i] = this.data[i] - v.data[i];
        }
        return new Vector2f(result[0], result[1]);
    }

    public Vector2f scale(float scalar) {
        float[] result = new float[2];
        for (int i = 0; i < 2; i++) {
            result[i] = this.data[i] * scalar;
        }
        return new Vector2f(result[0], result[1]);
    }

    public float dot(Vector2f v) {
        float sum = 0;
        for (int i = 0; i < 2; i++) {
            sum += this.data[i] * v.data[i];
        }
        return sum;
    }

    public float length() {
        return (float) Math.sqrt(dot(this));
    }

    public Vector2f normalize() {
        float len = length();
        return len != 0 ? scale(1.0f / len) : this;
    }

    public float distance(Vector2f v) {
        float sum = 0;
        for (int i = 0; i < 2; i++) {
            float d = this.data[i] - v.data[i];
            sum += d * d;
        }
        return (float) Math.sqrt(sum);
    }

    public Vector2f negate() {
        return scale(-1f);
    }

    @Override
    public String toString() {
        return "[" + data[0] + ", " + data[1] + "]";
    }
}
