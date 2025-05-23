package io.proto.spike.renderer.pipeline;

import io.proto.spike.renderer.core.Vec3;
import io.proto.spike.renderer.core.Vec4;

public class Projector {
    public Vec3 toNDC(Vec4 c) {
        return new Vec3(c.x / c.w, c.y / c.w, c.z / c.w);
    }
}
