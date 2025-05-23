package io.proto.spike.renderer.pipeline;

import io.proto.spike.renderer.core.Mat4;
import io.proto.spike.renderer.core.Vec3;
import io.proto.spike.renderer.core.Vec4;

public class VertexProcessor {
    public Vec4 transform(Vec4 v, Mat4 mvp) {
        return mvp.mul(v);
    }

    public boolean backfaceCull(Vec3 p0, Vec3 p1, Vec3 p2) {
        // CCW in screen space means normal.z < 0 is front
        Vec3 u = new Vec3(p1.x - p0.x, p1.y - p0.y, p1.z - p0.z);
        Vec3 v = new Vec3(p2.x - p0.x, p2.y - p0.y, p2.z - p0.z);
        u.cross(v);
        return u.z >= 0;
    }
}
