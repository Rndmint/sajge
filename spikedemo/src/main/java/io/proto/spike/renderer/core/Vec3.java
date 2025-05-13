package io.proto.spike.renderer.core;

public class Vec3 {
    public float x, y, z;

    public Vec3() {
        this(0, 0, 0);
    }

    public Vec3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3 add(Vec3 o) {
        x += o.x;
        y += o.y;
        z += o.z;
        return this;
    }

    public Vec3 sub(Vec3 o) {
        x -= o.x;
        y -= o.y;
        z -= o.z;
        return this;
    }

    public Vec3 cross(Vec3 o) {
        float cx = y * o.z - z * o.y;
        float cy = z * o.x - x * o.z;
        float cz = x * o.y - y * o.x;
        x = cx;
        y = cy;
        z = cz;
        return this;
    }

    public float dot(Vec3 o) {
        return x * o.x + y * o.y + z * o.z;
    }

    public Vec3 normalize() {
        float len = (float) Math.sqrt(x * x + y * y + z * z);
        if (len != 0f) {
            x /= len;
            y /= len;
            z /= len;
        }
        return this;
    }

    public Vec3 lerp(Vec3 o, float t) {
        x = x * (1 - t) + o.x * t;
        y = y * (1 - t) + o.y * t;
        z = z * (1 - t) + o.z * t;
        return this;
    }

    public static Vec3 lerp(Vec3 a, Vec3 b, float t) {
        return new Vec3(
                a.x * (1 - t) + b.x * t,
                a.y * (1 - t) + b.y * t,
                a.z * (1 - t) + b.z * t
        );
    }

    @Override
    public String toString() {
        return "Vec3{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
