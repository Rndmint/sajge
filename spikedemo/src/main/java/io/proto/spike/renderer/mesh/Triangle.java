package io.proto.spike.renderer.mesh;

import io.proto.spike.renderer.core.Vec3;

import java.awt.*;

public class Triangle {
    public int i0, i1, i2;
    public Vec3 normal;
    public Color color;

    public Triangle(int i0, int i1, int i2, Vec3 normal, Color color) {
        this.i0 = i0;
        this.i1 = i1;
        this.i2 = i2;
        this.normal = normal;
        this.color = color;
    }

    @Override
    public String toString() {
        return "Triangle{" +
                "i0=" + i0 +
                ", i1=" + i1 +
                ", i2=" + i2 +
                ", normal=" + normal +
                ", color=" + color +
                '}';
    }
}
