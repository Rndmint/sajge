package io.github.sajge.engine.renderer.pipeline;

import io.github.sajge.engine.renderer.core.Vec4;
import io.github.sajge.logger.Logger;

public class ClipVertex {
    private static final Logger log = Logger.get(ClipVertex.class);

    private final Vec4 pos;
    private final int modelId;
    private final int triangleId;

    public ClipVertex(Vec4 pos, int modelId, int triangleId) {
        log.trace("Creating ClipVertex with pos={}, modelId={}, triangleId={}", pos, modelId, triangleId);
        this.pos = pos;
        this.modelId = modelId;
        this.triangleId = triangleId;
    }

    public Vec4 getPos() {
        log.trace("getPos() called: returning {}", pos);
        return pos;
    }

    public int getModelId() {
        log.trace("getModelId() called: returning {}", modelId);
        return modelId;
    }

    public int getTriangleId() {
        log.trace("getTriangleId() called: returning {}", triangleId);
        return triangleId;
    }

    @Override
    public String toString() {
        return "ClipVertex(pos=" + pos + ", modelId=" + modelId + ", triangleId=" + triangleId + ")";
    }
}
