package io.github.sajge.engine.renderer.pipeline;

import io.github.sajge.engine.renderer.core.Vec4;
import io.github.sajge.logger.Logger;

public class ViewportTransformer {
    private static final Logger log = Logger.get(ViewportTransformer.class);

    public ScreenVertex toScreen(
            ClipVertex cv, int width, int height
    ) {
        Vec4 c = cv.getPos();
        float invW = 1f / c.w;

        float ndcX = c.x * invW;
        float ndcY = c.y * invW;
        float ndcZ = c.z * invW;

        int sx = (int) ((ndcX * 0.5f + 0.5f) * width);
        int sy = (int) ((-ndcY * 0.5f + 0.5f) * height);

        float zOvW = ndcZ * invW;

        log.trace("Transforming ClipVertex {} to ScreenVertex with screen coords ({}, {}) and zOvW={}",
                cv, sx, sy, zOvW);

        ScreenVertex sv = new ScreenVertex(
                sx, sy,
                invW,
                zOvW,
                cv.getModelId(),
                cv.getTriangleId()
        );

        log.debug("Created ScreenVertex {}", sv);
        return sv;
    }
}
