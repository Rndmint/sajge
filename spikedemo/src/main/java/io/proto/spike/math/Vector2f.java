package io.proto.spike.math;

public class Vector2f {
    public float x;
    public float y;

    public Vector2f() {
        this(0, 0);
    }

    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2f(Vector2f other) {
        this.x = other.x;
        this.y = other.y;
    }

    public Vector2f add(Vector2f v) {
        return new Vector2f(x + v.x, y + v.y);
    }

    public Vector2f sub(Vector2f v) {
        return new Vector2f(x - v.x, y - v.y);
    }

    public Vector2f scale(float scalar) {
        return new Vector2f(x * scalar, y * scalar);
    }

    public float dot(Vector2f v) {
        return x * v.x + y * v.y;
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y);
    }

    public Vector2f normalize() {
        float len = length();
        return len != 0 ? scale(1.0f / len) : this;
    }

    public float distance(Vector2f v) {
        float dx = x - v.x;
        float dy = y - v.y;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    public Vector2f negate() {
        return new Vector2f(-x, -y);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Vector2f)) return false;
        Vector2f v = (Vector2f) obj;
        return Float.compare(x + 0f, v.x + 0f) == 0 && Float.compare(y + 0f, v.y + 0f) == 0;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
