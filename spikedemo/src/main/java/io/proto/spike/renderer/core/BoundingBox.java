package io.proto.spike.renderer.core;

public class BoundingBox {
    public Vec3 min;
    public Vec3 max;

    public BoundingBox(Vec3 min, Vec3 max) {
    }

    public boolean contains(Vec3 point) {
        return false;
    }

    public boolean intersects(BoundingBox other) {
        return false;
    }
}
