package io.proto.spike.renderer.core;

public class Plane {
    public float nx, ny, nz, nw;

    public Plane(float nx, float ny, float nz, float nw) {
        this.nx = nx;
        this.ny = ny;
        this.nz = nz;
        this.nw = nw;
    }

    public float distance(Vec4 p) {
        return nx * p.x + ny * p.y + nz * p.z + nw * p.w;
    }

    public Vec4 intersect(Vec4 a, Vec4 b) {
        float da = distance(a);
        float db = distance(b);
        float t = da / (da - db);

        return new Vec4(
                a.x + (b.x - a.x) * t,
                a.y + (b.y - a.y) * t,
                a.z + (b.z - a.z) * t,
                a.w + (b.w - a.w) * t
        );
    }

    @Override
    public String toString() {
        return "Plane{" +
                "nx=" + nx +
                ", ny=" + ny +
                ", nz=" + nz +
                ", nw=" + nw +
                '}';
    }
}
