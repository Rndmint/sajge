package io.github.sajge.engine.renderer.pipeline;

import io.github.sajge.logger.Logger;

public class ScreenVertex {
    private static final Logger log = Logger.get(ScreenVertex.class);

    private final int x;
    private final int y;
    private final float invW;
    private final float zOverW;
    private final int modelId;
    private final int triangleId;

    public ScreenVertex(
            int x,
            int y,
            float invW,
            float zOverW,
            int modelId,
            int triangleId
    ) {
        log.trace("Creating ScreenVertex x={}, y={}, invW={}, zOverW={}, modelId={}, triangleId={}",
                x, y, invW, zOverW, modelId, triangleId);
        this.x = x;
        this.y = y;
        this.invW = invW;
        this.zOverW = zOverW;
        this.modelId = modelId;
        this.triangleId = triangleId;
    }

    public int getX() {
        log.trace("getX() => {}", x);
        return x;
    }

    public int getY() {
        log.trace("getY() => {}", y);
        return y;
    }

    public float getInvW() {
        log.trace("getInvW() => {}", invW);
        return invW;
    }

    public float getZOverW() {
        log.trace("getZOverW() => {}", zOverW);
        return zOverW;
    }

    public int getModelId() {
        log.trace("getModelId() => {}", modelId);
        return modelId;
    }

    public int getTriangleId() {
        log.trace("getTriangleId() => {}", triangleId);
        return triangleId;
    }

    @Override
    public String toString() {
        return "ScreenVertex(x=" + x
                + ", y=" + y
                + ", invW=" + invW
                + ", zOverW=" + zOverW
                + ", modelId=" + modelId
                + ", triangleId=" + triangleId + ")";
    }
}
