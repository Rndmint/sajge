package io.proto.spike.renderer.core;

public class BoundingBox {
    public Vec3 min, max;

    public BoundingBox(Vec3 min, Vec3 max) {
        this.min = min;
        this.max = max;
    }

    public boolean contains(Vec3 p) {
        return p.x >= min.x && p.x <= max.x &&
                p.y >= min.y && p.y <= max.y &&
                p.z >= min.z && p.z <= max.z;
    }

    public boolean intersects(BoundingBox o) {
        return !(o.max.x < min.x || o.min.x > max.x ||
                o.max.y < min.y || o.min.y > max.y ||
                o.max.z < min.z || o.min.z > max.z);
    }

    @Override
    public String toString() {
        return "BoundingBox{" +
                "min=" + min +
                ", max=" + max +
                '}';
    }
}
