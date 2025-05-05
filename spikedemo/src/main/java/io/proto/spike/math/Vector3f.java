package io.proto.spike.math;

public class Vector3f {
    public final float[] data;

    public Vector3f() {
        this.data = new float[3];
    }

    public Vector3f(float x, float y, float z) {
        this.data = new float[]{x, y, z};
    }

    public Vector3f(Vector3f other) {
        this.data = new float[]{other.data[0], other.data[1], other.data[2]};
    }

    public Vector3f add(Vector3f v) {
        float[] result = new float[3];
        for (int i = 0; i < 3; i++) {
            result[i] = this.data[i] + v.data[i];
        }
        return new Vector3f(result[0], result[1], result[2]);
    }

    public Vector3f sub(Vector3f v) {
        float[] result = new float[3];
        for (int i = 0; i < 3; i++) {
            result[i] = this.data[i] - v.data[i];
        }
        return new Vector3f(result[0], result[1], result[2]);
    }

    public Vector3f scale(float scalar) {
        float[] result = new float[3];
        for (int i = 0; i < 3; i++) {
            result[i] = this.data[i] * scalar;
        }
        return new Vector3f(result[0], result[1], result[2]);
    }

    public float dot(Vector3f v) {
        float sum = 0;
        for (int i = 0; i < 3; i++) {
            sum += this.data[i] * v.data[i];
        }
        return sum;
    }

    public Vector3f cross(Vector3f v) {
        float x = data[1] * v.data[2] - data[2] * v.data[1];
        float y = data[2] * v.data[0] - data[0] * v.data[2];
        float z = data[0] * v.data[1] - data[1] * v.data[0];
        return new Vector3f(x, y, z);
    }

    public float length() {
        return (float) Math.sqrt(dot(this));
    }

    public Vector3f normalize() {
        float len = length();
        return len != 0 ? scale(1.0f / len) : this;
    }

    public Vector3f negate() {
        return scale(-1f);
    }

    public float distance(Vector3f v) {
        float sum = 0;
        for (int i = 0; i < 3; i++) {
            float d = this.data[i] - v.data[i];
            sum += d * d;
        }
        return (float) Math.sqrt(sum);
    }

    @Override
    public String toString() {
        return "[" + data[0] + ", " + data[1] + ", " + data[2] + "]";
    }
}
