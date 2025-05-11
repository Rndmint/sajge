package io.github.sajge.engine.renderer.pipeline;

import io.github.sajge.engine.renderer.core.Vec3;
import io.github.sajge.engine.renderer.core.Vec4;
import io.github.sajge.logger.Logger;

public class Projector {
    private static final Logger log = Logger.get(Projector.class);

    public Vec3 toNDC(Vec4 c) {
        log.trace("Projecting clip-space coordinate {} to NDC", c);
        Vec3 ndc = new Vec3(c.x / c.w, c.y / c.w, c.z / c.w);
        log.debug("Projected NDC coordinate {}", ndc);
        return ndc;
    }
}
