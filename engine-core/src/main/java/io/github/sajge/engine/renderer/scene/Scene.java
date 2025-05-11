package io.github.sajge.engine.renderer.scene;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.sajge.logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Scene {
    private static final Logger log = Logger.get(Scene.class);

    private Camera camera;
    private List<Model> models;

    public Scene() {
        log.debug("Initializing Scene with empty model list");
        this.models = new ArrayList<>();
    }

    @JsonCreator
    public Scene(
            @JsonProperty("camera") Camera camera,
            @JsonProperty("objects") List<Model> models
    ) {
        log.debug("Creating Scene with camera={} and {} models", camera, models != null ? models.size() : 0);
        this.camera = camera;
        this.models = models != null ? models : new ArrayList<>();
    }

    @JsonProperty("camera")
    public Camera getCamera() {
        log.trace("getCamera() => {}", camera);
        return camera;
    }

    @JsonProperty("camera")
    public void setCamera(Camera camera) {
        log.trace("setCamera({})", camera);
        this.camera = camera;
    }

    @JsonProperty("objects")
    public List<Model> getModels() {
        log.trace("getModels() => {} models", models != null ? models.size() : 0);
        return models;
    }

    @JsonProperty("objects")
    public void setModels(List<Model> models) {
        log.trace("setModels({} models)", models != null ? models.size() : 0);
        this.models = models;
    }

    public void addModel(Model m) {
        log.debug("Adding model {} to scene", m);
        models.add(m);
    }

    public void removeModel(Model m) {
        log.debug("Removing model {} from scene", m);
        models.remove(m);
    }

    public Optional<TriangleInfo> findByTriangleId(int tid) {
        log.trace("Finding TriangleInfo for triangleId={}", tid);
        for (Model m : models) {
            for (Triangle t : m.getMesh().getTriangles()) {
                if (t.getId() == tid) {
                    TriangleInfo info = new TriangleInfo(m.getId(), tid);
                    log.debug("Found TriangleInfo {}", info);
                    return Optional.of(info);
                }
            }
        }
        log.info("No TriangleInfo found for triangleId={}", tid);
        return Optional.empty();
    }

    public Optional<Model> findModel(int mid) {
        log.trace("Finding model with id={}", mid);
        Optional<Model> model = models.stream()
                .filter(m -> m.getId() == mid)
                .findFirst();
        log.debug("findModel result => {}", model);
        return model;
    }

    public static class TriangleInfo {
        private final int modelId;
        private final int triangleId;

        @JsonCreator
        public TriangleInfo(
                @JsonProperty("modelId") int modelId,
                @JsonProperty("triangleId") int triangleId
        ) {
            this.modelId = modelId;
            this.triangleId = triangleId;
            log.debug("Created TriangleInfo modelId={}, triangleId={}", modelId, triangleId);
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
            return "TriangleInfo(modelId=" + modelId + ", triangleId=" + triangleId + ")";
        }
    }
}
