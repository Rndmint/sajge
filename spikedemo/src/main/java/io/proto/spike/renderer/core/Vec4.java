package io.proto.spike.renderer.core;

public class Vec4 {
    public float x, y, z, w;

    public Vec4() {
        this(0, 0, 0, 0);
    }

    public Vec4(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vec4 add(Vec4 o) {
        x += o.x;
        y += o.y;
        z += o.z;
        w += o.w;
        return this;
    }

    public Vec4 sub(Vec4 o) {
        x -= o.x;
        y -= o.y;
        z -= o.z;
        w -= o.w;
        return this;
    }

    public float dot(Vec4 o) {
        return x * o.x + y * o.y + z * o.z + w * o.w;
    }

    @Override
    public String toString() {
        return "Vec4{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", w=" + w +
                '}';
    }
}
