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

    public Model getModel(int id) {
        log.trace("getModels() => {} models", models != null ? models.size() : 0);
        return models.get(id);
    }

    public void setModels(Model m, int id) {
        log.trace("setModels({} models)", models != null ? models.size() : 0);
        models.set(id, m);
    }

    public void addModel(Model m) {
        log.debug("Adding model {} to scene", m);
        models.add(m);
    }

    public void removeModel(Model m) {
        log.debug("Removing model {} from scene", m);
        models.remove(m);
    }

    public void reindexModels() {
        log.debug("Reindexing {} models", models.size());
        for (int i = 0; i < models.size(); i++) {
            models.get(i).setId(i);
            log.trace("Model at list index {} now has id={}", i, i);
        }
        log.info("Model reindexing complete");
    }

    public Triangle getTriangle(int idm, int idt) {
        log.trace("getModels() => {} models", models != null ? models.size() : 0);
        return models.get(idm).getMesh().getTriangle(idt);
    }
}
