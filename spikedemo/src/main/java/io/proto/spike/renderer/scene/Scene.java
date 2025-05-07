package io.proto.spike.renderer.scene;

import io.proto.spike.renderer.buffer.DepthBuffer;
import io.proto.spike.renderer.buffer.FrameBuffer;
import io.proto.spike.renderer.pipeline.Pipeline;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Scene {
    public List<Model> models = new ArrayList<>();
    public Camera camera;

    public Scene(Camera camera) {
        this.camera = camera;
    }

    public void addModel(Model model) {
        models.add(model);
    }

    public void removeModel(Model model) {
        models.remove(model);
    }

    public List<Model> getModels() {
        return Collections.unmodifiableList(models);
    }

    public Camera getCamera() {
        return camera;
    }

    public void render(Pipeline pipeline, FrameBuffer frameBuffer, DepthBuffer depthBuffer) {
        pipeline.renderScene(this, frameBuffer, depthBuffer);
    }
}
