package io.proto.spike.renderer.debug;

import io.proto.spike.renderer.buffer.DepthBuffer;
import io.proto.spike.renderer.buffer.FrameBuffer;
import io.proto.spike.renderer.core.BoundingBox;
import io.proto.spike.renderer.core.Mat4;
import io.proto.spike.renderer.mesh.Mesh;

import java.awt.*;

public class DebugRenderer {
    private final FrameBuffer fb;

    public DebugRenderer(FrameBuffer fb) {
        this.fb = fb;
    }

    public void drawWireframe(Mesh mesh, Mat4 mvp, Color color) {
        // TODO
    }

    public void drawDepthMap(DepthBuffer depthBuffer) {
        // TODO
    }

    public void drawBoundingBox(BoundingBox box, Mat4 mvp, Color color) {
        // TODO
    }
}
