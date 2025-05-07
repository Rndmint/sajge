package io.proto.spike.renderer.pipeline;

import io.proto.spike.renderer.core.Mat4;
import io.proto.spike.renderer.core.Vec3;
import io.proto.spike.renderer.core.Vec4;

public class VertexProcessor {
    public Vec4 transform(Vec4 vertex, Mat4 mvp) {
        return null;
    }

    public boolean backfaceCull(Vec3 p0, Vec3 p1, Vec3 p2) {
        return false;
    }
}
