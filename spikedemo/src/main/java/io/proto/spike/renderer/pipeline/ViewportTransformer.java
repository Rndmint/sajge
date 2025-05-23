package io.proto.spike.renderer.pipeline;

import io.proto.spike.renderer.core.Vec3;
import io.proto.spike.renderer.core.Vec4;
import io.proto.spike.renderer.support.ClipVertex;
import io.proto.spike.renderer.support.ScreenVertex;

public class ViewportTransformer {
    public ScreenVertex toScreen(ClipVertex cv, int width, int height) {
        Vec4 c    = cv.pos;
        float invW = 1.0f / c.w;
        float ndcX = c.x * invW;
        float ndcY = c.y * invW;
        float ndcZ = c.z * invW;

        int sx      = (int)((ndcX * 0.5f + 0.5f) * width);
        int sy      = (int)((-ndcY * 0.5f + 0.5f) * height);
        float zOverW = ndcZ * invW;

        return new ScreenVertex(sx, sy, invW, zOverW);
    }
}
