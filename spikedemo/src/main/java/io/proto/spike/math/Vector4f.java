package io.proto.spike.math;

public class Vector4f {
    public float x;
    public float y;
    public float z;
    public float w;

    public Vector4f() {
        this(0, 0, 0, 0);
    }

    public Vector4f(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vector4f(Vector4f other) {
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
        this.w = other.w;
    }

    public Vector4f add(Vector4f v) {
        return new Vector4f(x + v.x, y + v.y, z + v.z, w + v.w);
    }

    public Vector4f sub(Vector4f v) {
        return new Vector4f(x - v.x, y - v.y, z - v.z, w - v.w);
    }

    public Vector4f scale(float scalar) {
        return new Vector4f(x * scalar, y * scalar, z * scalar, w * scalar);
    }

    public float dot(Vector4f v) {
        return x * v.x + y * v.y + z * v.z + w * v.w;
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z + w * w);
    }

    public Vector4f normalize() {
        float len = length();
        return len != 0 ? scale(1.0f / len) : new Vector4f(0, 0, 0, 0);
    }

    public Vector4f negate() {
        return new Vector4f(-x, -y, -z, -w);
    }

    public float distance(Vector4f v) {
        float dx = x - v.x;
        float dy = y - v.y;
        float dz = z - v.z;
        float dw = w - v.w;
        return (float) Math.sqrt(dx * dx + dy * dy + dz * dz + dw * dw);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Vector4f)) return false;
        Vector4f v = (Vector4f) obj;
        return Float.compare(x, v.x) == 0 &&
                Float.compare(y, v.y) == 0 &&
                Float.compare(z, v.z) == 0 &&
                Float.compare(w, v.w) == 0;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ", " + w + ")";
    }
}
