package io.github.sajge.engine.renderer.pipeline;

import io.github.sajge.engine.renderer.core.Mat4;
import io.github.sajge.engine.renderer.core.Vec3;
import io.github.sajge.engine.renderer.core.Vec4;
import io.github.sajge.logger.Logger;

public class VertexProcessor {
    private static final Logger log = Logger.get(VertexProcessor.class);

    public Vec4 transform(Vec4 v, Mat4 mvp) {
        log.trace("Transforming Vec4 {} with MVP matrix", v);
        Vec4 result = mvp.mul(v);
        log.debug("Result of transform: {}", result);
        return result;
    }

    public boolean backfaceCull(Vec3 p0, Vec3 p1, Vec3 p2) {
        log.trace("Performing back-face culling on triangle with points {}, {}, {}", p0, p1, p2);
        Vec3 u = new Vec3(p1.x - p0.x, p1.y - p0.y, p1.z - p0.z);
        Vec3 v = new Vec3(p2.x - p0.x, p2.y - p0.y, p2.z - p0.z);
        Vec3 normal = u.cross(v);
        boolean culled = normal.z >= 0;
        log.debug("Back-face cull test: normal.z={} => {}", normal.z, culled ? "cull" : "keep");
        return culled;
    }
}
