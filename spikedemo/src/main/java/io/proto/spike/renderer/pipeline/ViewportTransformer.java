package io.proto.spike.renderer.pipeline;

import io.proto.spike.renderer.core.Vec3;
import io.proto.spike.renderer.support.ScreenVertex;

public class ViewportTransformer {
    public ScreenVertex toScreen(Vec3 ndc, int w, int h) {
        int sx = (int) ((ndc.x * 0.5f + 0.5f) * w);
        int sy = (int) ((-ndc.y * 0.5f + 0.5f) * h);
        float sz = ndc.z * 0.5f + 0.5f;
        return new ScreenVertex(sx, sy, sz);
    }
}
