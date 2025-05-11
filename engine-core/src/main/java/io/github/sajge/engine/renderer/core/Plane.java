package io.github.sajge.engine.renderer.core;

import io.github.sajge.logger.Logger;

public class Plane {
    private static final Logger log = Logger.get(Plane.class);

    private final float nx;
    private final float ny;
    private final float nz;
    private final float nw;

    public Plane(float nx, float ny, float nz, float nw) {
        log.debug("Constructing Plane with normal=({}, {}, {}) and w={}", nx, ny, nz, nw);
        this.nx = nx;
        this.ny = ny;
        this.nz = nz;
        this.nw = nw;
    }

    public float distance(Vec4 p) {
        float dist = nx * p.x + ny * p.y + nz * p.z + nw * p.w;
        log.trace("Computed distance of point {} to plane: {}", p, dist);
        return dist;
    }

    public Vec4 intersect(Vec4 a, Vec4 b) {
        log.trace("Intersecting line segment between {} and {} with plane", a, b);
        float da = distance(a);
        float db = distance(b);
        float t = da / (da - db);
        log.debug("Intersection factor t = {} (da={}, db={})", t, da, db);

        float ix = a.x + (b.x - a.x) * t;
        float iy = a.y + (b.y - a.y) * t;
        float iz = a.z + (b.z - a.z) * t;
        float iw = a.w + (b.w - a.w) * t;
        Vec4 result = new Vec4(ix, iy, iz, iw);
        log.trace("Intersection result: {}", result);
        return result;
    }

}
