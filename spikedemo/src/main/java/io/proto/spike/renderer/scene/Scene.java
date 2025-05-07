package io.proto.spike.renderer.scene;

import io.proto.spike.renderer.buffer.DepthBuffer;
import io.proto.spike.renderer.buffer.FrameBuffer;
import io.proto.spike.renderer.pipeline.Pipeline;

import java.util.List;

public class Scene {
    public List<Model> models;
    public Camera camera;

    public void render(Pipeline pipeline, FrameBuffer fb, DepthBuffer db) {
    }
}
