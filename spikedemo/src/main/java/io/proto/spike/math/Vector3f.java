package io.proto.spike.math;

public class Vector3f {
    public float x;
    public float y;
    public float z;

    public Vector3f() {
        this(0, 0, 0);
    }

    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3f(Vector3f other) {
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
    }

    public Vector3f add(Vector3f v) {
        return new Vector3f(x + v.x, y + v.y, z + v.z);
    }

    public Vector3f sub(Vector3f v) {
        return new Vector3f(x - v.x, y - v.y, z - v.z);
    }

    public Vector3f scale(float scalar) {
        return new Vector3f(x * scalar, y * scalar, z * scalar);
    }

    public float dot(Vector3f v) {
        return x * v.x + y * v.y + z * v.z;
    }

    public Vector3f cross(Vector3f v) {
        float cx = y * v.z - z * v.y;
        float cy = z * v.x - x * v.z;
        float cz = x * v.y - y * v.x;
        return new Vector3f(cx, cy, cz);
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    public Vector3f normalize() {
        float len = length();
        return len != 0 ? scale(1.0f / len) : new Vector3f(0, 0, 0);
    }

    public Vector3f negate() {
        return new Vector3f(-x, -y, -z);
    }

    public float distance(Vector3f v) {
        float dx = x - v.x;
        float dy = y - v.y;
        float dz = z - v.z;
        return (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Vector3f)) return false;
        Vector3f v = (Vector3f) obj;
        return Float.compare(x, v.x) == 0 &&
                Float.compare(y, v.y) == 0 &&
                Float.compare(z, v.z) == 0;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }
}
