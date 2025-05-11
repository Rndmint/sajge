package io.github.sajge.engine.renderer;

import io.github.sajge.engine.renderer.core.Vec3;
import io.github.sajge.engine.renderer.scene.Scene;
import io.github.sajge.engine.renderer.scene.Model;
import io.github.sajge.engine.renderer.scene.Triangle;
import io.github.sajge.logger.Logger;

import java.awt.*;

public class Editor {
    private static final Logger log = Logger.get(Editor.class);

    private final Engine engine;
    private final Scene scene;
    private int selectedModelId = -1;
    private int selectedTriangleId = -1;

    public Editor(Engine engine) {
        log.info("Initializing Editor");
        this.engine = engine;
        this.scene = engine.getScene();
    }

    public void select(int triangleId) {
        log.debug("Selecting triangleId={}", triangleId);
        scene.findByTriangleId(triangleId).ifPresentOrElse(info -> {
            selectedModelId = info.getModelId();
            selectedTriangleId = info.getTriangleId();
            log.info("Selected modelId={}, triangleId={}", selectedModelId, selectedTriangleId);
        }, () -> log.warn("TriangleId {} not found in scene", triangleId));
    }

    public void translateSelected(float dx, float dy, float dz) {
        log.debug("Translating selected by dx={}, dy={}, dz={}", dx, dy, dz);
        if (selectedModelId < 0) {
            log.warn("No model selected to translate");
            return;
        }
        engine.getScene().findModel(selectedModelId).ifPresentOrElse(model -> {
            model.getTransform().translate(dx, dy, dz);
            log.info("Translated modelId={} to position {}", selectedModelId, model.getTransform().getPosition());
        }, () -> log.error("Selected modelId {} not found", selectedModelId));
    }

    public void rotateSelected(float rx, float ry, float rz) {
        log.debug("Rotating selected by rx={}, ry={}, rz={}", rx, ry, rz);
        if (selectedModelId < 0) {
            log.warn("No model selected to rotate");
            return;
        }
        engine.getScene().findModel(selectedModelId).ifPresentOrElse(model -> {
            model.getTransform().rotate(rx, ry, rz);
            log.info("Rotated modelId={} to rotation {}", selectedModelId, model.getTransform().getRotation());
        }, () -> log.error("Selected modelId {} not found", selectedModelId));
    }

    public void scaleSelected(float sx, float sy, float sz) {
        log.debug("Scaling selected by sx={}, sy={}, sz={}", sx, sy, sz);
        if (selectedModelId < 0) {
            log.warn("No model selected to scale");
            return;
        }
        engine.getScene().findModel(selectedModelId).ifPresentOrElse(model -> {
            model.getTransform().scale(sx, sy, sz);
            log.info("Scaled modelId={} to scale {}", selectedModelId, model.getTransform().getScale());
        }, () -> log.error("Selected modelId {} not found", selectedModelId));
    }

    public void setFaceColor(int triangleId, Color color) {
        log.debug("Setting faceColor for triangleId={} to {}", triangleId, color);
        scene.findByTriangleId(triangleId).ifPresentOrElse(info -> {
            scene.findModel(info.getModelId()).ifPresent(model -> {
                model.getMesh().getTriangles().stream()
                        .filter(t -> t.getId() == triangleId)
                        .findFirst()
                        .ifPresentOrElse(t -> {
                            t.setColor(color);
                            log.info("Set color of triangleId={} to {}", triangleId, color);
                        }, () -> log.error("Triangle {} not found in model {}", triangleId, info.getModelId()));
            });
        }, () -> log.warn("TriangleId {} not found for setting color", triangleId));
    }

    public void addObject(Model model) {
        log.debug("Adding object {} to scene", model);
        scene.addModel(model);
        log.info("Object {} added", model);
    }

    public void removeObject(int modelId) {
        log.debug("Removing object modelId={}", modelId);
        scene.findModel(modelId).ifPresentOrElse(model -> {
            scene.removeModel(model);
            log.info("Removed model {}", modelId);
        }, () -> log.warn("ModelId {} not found for removal", modelId));
    }

    public void setCameraTransform(Vec3 position, Vec3 rotation) {
        log.debug("Setting camera transform to position={}, rotation={}", position, rotation);
        var cam = scene.getCamera();
        cam.getTransform().setPosition(position);
        cam.getTransform().setRotation(rotation);
        log.info("Camera transform set to {}", cam.getTransform());
    }

    public void setCameraParams(float fovY, float aspect, float near, float far) {
        log.debug("Setting camera params fovY={}, aspect={}, near={}, far={}", fovY, aspect, near, far);
        var cam = scene.getCamera();
        cam.setFovY(fovY);
        cam.setAspect(aspect);
        cam.setNear(near);
        cam.setFar(far);
        log.info("Camera params set to fovY={}, aspect={}, near={}, far={}", fovY, aspect, near, far);
    }
}
