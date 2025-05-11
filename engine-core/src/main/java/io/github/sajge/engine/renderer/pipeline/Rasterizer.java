package io.github.sajge.engine.renderer.pipeline;

import io.github.sajge.engine.renderer.buffer.DepthBuffer;
import io.github.sajge.engine.renderer.buffer.FrameBuffer;
import io.github.sajge.engine.renderer.util.MathUtils;
import io.github.sajge.logger.Logger;

import java.awt.Color;

public class Rasterizer {
    private static final Logger log = Logger.get(Rasterizer.class);

    private final FrameBuffer fb;
    private final DepthBuffer db;
    private final int[] triangleIdBuffer;
    private final int[] modelIdBuffer;

    public Rasterizer(
            FrameBuffer fb,
            DepthBuffer db,
            int[] triangleIdBuffer,
            int[] modelIdBuffer
    ) {
        log.info("Initializing Rasterizer");
        this.fb = fb;
        this.db = db;
        this.triangleIdBuffer = triangleIdBuffer;
        this.modelIdBuffer = modelIdBuffer;
    }

    public void rasterizeTriangle(
            ScreenVertex v0,
            ScreenVertex v1,
            ScreenVertex v2,
            Color color
    ) {
        log.trace("Rasterizing triangle with vertices {}, {}, {} and color {}",
                v0, v1, v2, color);

        int width = fb.getImage().getWidth();
        int height = fb.getImage().getHeight();

        int minX = Math.max(0, Math.min(v0.getX(), Math.min(v1.getX(), v2.getX())));
        int maxX = Math.min(width - 1, Math.max(v0.getX(), Math.max(v1.getX(), v2.getX())));
        int minY = Math.max(0, Math.min(v0.getY(), Math.min(v1.getY(), v2.getY())));
        int maxY = Math.min(height - 1, Math.max(v0.getY(), Math.max(v1.getY(), v2.getY())));

        log.debug("Triangle bounding box: x[{}..{}], y[{}..{}]",
                minX, maxX, minY, maxY);

        float area = MathUtils.edge(
                v0.getX(), v0.getY(),
                v1.getX(), v1.getY(),
                v2.getX(), v2.getY()
        );

        if (area == 0) {
            log.warn("Degenerate triangle with zero area: {}", area);
            return;
        }

        int pixelsDrawn = 0;
        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                float w0 = MathUtils.edge(
                        v1.getX(), v1.getY(),
                        v2.getX(), v2.getY(),
                        x, y
                ) / area;
                float w1 = MathUtils.edge(
                        v2.getX(), v2.getY(),
                        v0.getX(), v0.getY(),
                        x, y
                ) / area;
                float w2 = MathUtils.edge(
                        v0.getX(), v0.getY(),
                        v1.getX(), v1.getY(),
                        x, y
                ) / area;

                if (w0 >= 0 && w1 >= 0 && w2 >= 0) {
                    float iW = w0 * v0.getInvW()
                            + w1 * v1.getInvW()
                            + w2 * v2.getInvW();
                    float zOW = w0 * v0.getZOverW()
                            + w1 * v1.getZOverW()
                            + w2 * v2.getZOverW();
                    float trueZ = zOW / iW;

                    if (db.testAndSet(x, y, trueZ)) {
                        fb.setPixel(x, y, color);
                        int idx = y * width + x;
                        triangleIdBuffer[idx] = v0.getTriangleId();
                        modelIdBuffer[idx] = v0.getModelId();
                        pixelsDrawn++;
                    }
                }
            }
        }

        log.debug("Rasterization complete, pixels drawn={}", pixelsDrawn);
    }
}
