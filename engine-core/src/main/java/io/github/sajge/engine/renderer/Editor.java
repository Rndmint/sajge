package io.github.sajge.engine.renderer;

import io.github.sajge.engine.renderer.core.Vec3;
import io.github.sajge.engine.renderer.scene.Model;
import io.github.sajge.engine.renderer.scene.Scene;
import io.github.sajge.logger.Logger;

import java.awt.Color;

public class Editor {
    private static final Logger log = Logger.get(Editor.class);

    private final Engine engine;
    private int selectedModelId = -1;
    private int selectedTriangleId = -1;

    public Editor(Engine engine) {
        log.info("Initializing Editor");
        this.engine = engine;
    }

    public void selectAt(int x, int y) {
        log.debug("Selecting at pixel ({},{})", x, y);
        int modelId = engine.getModelIdAt(x, y);
        int triangleId = engine.getTriangleIdAt(x, y);
        if (modelId < 0 || triangleId < 0) {
            log.warn("No object found at ({},{}): modelId={}, triangleId={}", x, y, modelId, triangleId);
            selectedModelId = -1;
            selectedTriangleId = -1;
        } else {
            selectedModelId = modelId;
            selectedTriangleId = triangleId;
            log.info("Selected modelId={} and triangleId={} at ({},{})", modelId, triangleId, x, y);
        }
    }

    public void select(int modelId, int triangleId) {
        log.debug("Selecting modelId={} triangleId={}", modelId, triangleId);
        if (modelId < 0 || triangleId < 0) {
            log.warn("Invalid selection IDs: modelId={}, triangleId={}", modelId, triangleId);
            selectedModelId = -1;
            selectedTriangleId = -1;
            return;
        }
        selectedModelId = modelId;
        selectedTriangleId = triangleId;
        log.info("Selected modelId={} triangleId={}", modelId, triangleId);
    }

    public Model getSelectedModel() {
        if (selectedModelId < 0) {
            log.warn("No model selected");
            return null;
        }
        Model model = engine.getScene().getModels().stream()
                .filter(m -> m.getId() == selectedModelId)
                .findFirst()
                .orElse(null);
        if (model == null) {
            log.error("Selected modelId {} does not exist in scene", selectedModelId);
        }
        return model;
    }

    public void translateSelected(float dx, float dy, float dz) {
        log.debug("Translating selected by dx={}, dy={}, dz={}", dx, dy, dz);
        Model model = getSelectedModel();
        if (model != null) {
            model.getTransform().translate(dx, dy, dz);
            log.info("Translated modelId={} to position {}", selectedModelId, model.getTransform().getPosition());
            engine.render();
        }
    }

    public void rotateSelected(float rx, float ry, float rz) {
        log.debug("Rotating selected by rx={}, ry={}, rz={}", rx, ry, rz);
        Model model = getSelectedModel();
        if (model != null) {
            model.getTransform().rotate(rx, ry, rz);
            log.info("Rotated modelId={} to rotation {}", selectedModelId, model.getTransform().getRotation());
            engine.render();
        }
    }

    public void scaleSelected(float sx, float sy, float sz) {
        log.debug("Scaling selected by sx={}, sy={}, sz={}", sx, sy, sz);
        Model model = getSelectedModel();
        if (model != null) {
            model.getTransform().scale(sx, sy, sz);
            log.info("Scaled modelId={} to scale {}", selectedModelId, model.getTransform().getScale());
            engine.render();
        }
    }

    public void setFaceColor(Color color) {
        log.debug("Setting face color of selected triangleId={} to {}", selectedTriangleId, color);
        Model model = getSelectedModel();
        if (model != null && selectedTriangleId >= 0) {
            model.getMesh().getTriangles().stream()
                    .filter(t -> t.getId() == selectedTriangleId)
                    .findFirst()
                    .ifPresentOrElse(t -> {
                        t.setColor(color);
                        log.info("Set color of triangleId={} to {}", selectedTriangleId, color);
                        engine.render();
                    }, () -> log.error("TriangleId {} not found in model {}", selectedTriangleId, selectedModelId));
        } else {
            log.warn("No triangle selected for coloring");
        }
    }

    public void addObject(Model model) {
        log.debug("Adding object {} to scene", model);
        engine.getScene().addModel(model);
        engine.getScene().reindexModels();
        log.info("Object {} added and models reindexed", model);
        engine.render();
    }

    public void removeSelectedObject() {
        log.debug("Removing selected object modelId={}", selectedModelId);
        Model model = getSelectedModel();
        if (model != null) {
            engine.getScene().removeModel(model);
            engine.getScene().reindexModels();
            selectedModelId = -1;
            selectedTriangleId = -1;
            log.info("Model removed and models reindexed");
            engine.render();
        }
    }

    public void setCameraTransform(Vec3 position, Vec3 rotation) {
        log.debug("Setting camera transform to position={}, rotation={}", position, rotation);
        var cam = engine.getScene().getCamera();
        cam.getTransform().setPosition(position);
        cam.getTransform().setRotation(rotation);
        log.info("Camera transform set to {}", cam.getTransform());
        engine.render();
    }

    public void setCameraParams(float fovY, float aspect, float near, float far) {
        log.debug("Setting camera params fovY={}, aspect={}, near={}, far={}", fovY, aspect, near, far);
        var cam = engine.getScene().getCamera();
        cam.setFovY(fovY);
        cam.setAspect(aspect);
        cam.setNear(near);
        cam.setFar(far);
        log.info("Camera params set to fovY={}, aspect={}, near={}, far={}", fovY, aspect, near, far);
        engine.render();
    }

    public void clearSelection() {
        log.debug("Clearing selection");
        selectedModelId = -1;
        selectedTriangleId = -1;
    }

    public void translateSelectedX(float dx) {
        translateSelected(dx, 0f, 0f);
    }

    public void translateSelectedY(float dy) {
        translateSelected(0f, dy, 0f);
    }

    public void translateSelectedZ(float dz) {
        translateSelected(0f, 0f, dz);
    }

    public void rotateSelectedX(float rx) {
        rotateSelected(rx, 0f, 0f);
    }

    public void rotateSelectedY(float ry) {
        rotateSelected(0f, ry, 0f);
    }

    public void rotateSelectedZ(float rz) {
        rotateSelected(0f, 0f, rz);
    }

    public void scaleSelectedX(float sx) {
        scaleSelected(sx, 1f, 1f);
    }

    public void scaleSelectedY(float sy) {
        scaleSelected(1f, sy, 1f);
    }

    public void scaleSelectedZ(float sz) {
        scaleSelected(1f, 1f, sz);
    }

    public void setCameraPositionX(float x) {
        var cam = engine.getScene().getCamera();
        Vec3 pos = cam.getTransform().getPosition();
        setCameraTransform(new Vec3(x, pos.y, pos.z), cam.getTransform().getRotation());
    }

    public void setCameraPositionY(float y) {
        var cam = engine.getScene().getCamera();
        Vec3 pos = cam.getTransform().getPosition();
        setCameraTransform(new Vec3(pos.x, y, pos.z), cam.getTransform().getRotation());
    }

    public void setCameraPositionZ(float z) {
        var cam = engine.getScene().getCamera();
        Vec3 pos = cam.getTransform().getPosition();
        setCameraTransform(new Vec3(pos.x, pos.y, z), cam.getTransform().getRotation());
    }

    public void setCameraRotationX(float rx) {
        var cam = engine.getScene().getCamera();
        Vec3 rot = cam.getTransform().getRotation();
        setCameraTransform(cam.getTransform().getPosition(), new Vec3(rx, rot.y, rot.z));
    }

    public void setCameraRotationY(float ry) {
        var cam = engine.getScene().getCamera();
        Vec3 rot = cam.getTransform().getRotation();
        setCameraTransform(cam.getTransform().getPosition(), new Vec3(rot.x, ry, rot.z));
    }

    public void setCameraRotationZ(float rz) {
        var cam = engine.getScene().getCamera();
        Vec3 rot = cam.getTransform().getRotation();
        setCameraTransform(cam.getTransform().getPosition(), new Vec3(rot.x, rot.y, rz));
    }

}
