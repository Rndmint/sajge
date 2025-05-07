package io.proto.spike.renderer.pipeline;

import io.proto.spike.renderer.buffer.DepthBuffer;
import io.proto.spike.renderer.buffer.FrameBuffer;
import io.proto.spike.renderer.support.ScreenVertex;
import io.proto.spike.renderer.util.MathUtils;

import java.awt.*;

public class Rasterizer {

    private FrameBuffer fb;
    private DepthBuffer db;

    public Rasterizer(FrameBuffer fb, DepthBuffer db) {
        this.fb = fb;
        this.db = db;
    }

    public void rasterizeTriangle(
            ScreenVertex v0,
            ScreenVertex v1,
            ScreenVertex v2,
            Color color){

        int minX = Math.max(0, Math.min(v0.x, Math.min(v1.x, v2.x)));
        int maxX = Math.min(fb.image.getWidth()-1, Math.max(v0.x, Math.max(v1.x, v2.x)));
        int minY = Math.max(0, Math.min(v0.y, Math.min(v1.y, v2.y)));
        int maxY = Math.min(fb.image.getHeight()-1, Math.max(v0.y, Math.max(v1.y, v2.y)));

        float area = MathUtils.edge(v0.x, v0.y, v1.x, v1.y, v2.x, v2.y);

        if ( area == 0 ) return;

        for ( int y = minY ; y <= maxY ; y++ ) {
            for ( int x = minX ; x <= maxX ; x++ ) {
                float w0 = MathUtils.edge(v1.x, v1.y, v2.x, v2.y, x, y) / area;
                float w1 = MathUtils.edge(v2.x, v2.y, v0.x, v0.y, x, y) / area;
                float w2 = MathUtils.edge(v0.x, v0.y, v1.x, v1.y, x, y) / area;
                if ( w0 >= 0 && w1 >= 0 && w2 >= 0 ) {
                    float z = w0 * v0.z + w1 * v1.z + w2 * v2.z;
                    if (db.testAndSet(x, y, z)) fb.setPixel(x, y, color);
                }
            }
        }
    }
}
