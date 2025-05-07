package io.proto.spike.renderer.pipeline;

import io.proto.spike.renderer.buffer.DepthBuffer;
import io.proto.spike.renderer.buffer.FrameBuffer;
import io.proto.spike.renderer.scene.Scene;

public class Pipeline {
    public Pipeline(
            VertexProcessor vp,
            Clipper clipper,
            Projector proj,
            ViewportTransformer vpTrans,
            Rasterizer rasterizer) {
    }

    public void renderScene(Scene scene, FrameBuffer fb, DepthBuffer db) {
    }
}
