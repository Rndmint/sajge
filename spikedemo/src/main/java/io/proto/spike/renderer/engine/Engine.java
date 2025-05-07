package io.proto.spike.renderer.engine;

import io.proto.spike.renderer.buffer.DepthBuffer;
import io.proto.spike.renderer.buffer.FrameBuffer;
import io.proto.spike.renderer.scene.Scene;

import java.util.List;

public class Engine {
    public List<Scene> scenes;
    public FrameBuffer frameBuffer;
    public DepthBuffer depthBuffer;

    public Engine(int width, int height) {
    }

    public void start() {
    }

    public void renderFrame() {
    }
}
