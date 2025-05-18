package io.github.sajge.engine.renderer;

import io.github.sajge.engine.renderer.buffer.DepthBuffer;
import io.github.sajge.engine.renderer.buffer.FrameBuffer;
import io.github.sajge.engine.renderer.pipeline.Pipeline;
import io.github.sajge.engine.renderer.scene.Model;
import io.github.sajge.engine.renderer.scene.Scene;
import io.github.sajge.logger.Logger;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class Engine {
    private static final Logger log = Logger.get(Engine.class);

    private final int width;
    private final int height;

    private final FrameBuffer fb;

    private final FrameBuffer stableFb;
    private final DepthBuffer db;


    private final int[] tmpTriangleIdBuffer;
    private final int[] tmpModelIdBuffer;
    private final int[] triangleIdBuffer;
    private final int[] modelIdBuffer;

    private final Pipeline pipeline;
    private Scene scene;

    public Engine(int width, int height) {
        log.info("Initializing Engine with width={} and height={}", width, height);
        this.width = width;
        this.height = height;
        this.fb = new FrameBuffer(width, height);
        this.stableFb = new FrameBuffer(width, height);
        this.db = new DepthBuffer(width, height);

        this.tmpTriangleIdBuffer = new int[width * height];
        this.tmpModelIdBuffer = new int[width * height];
        this.triangleIdBuffer = new int[width * height];
        this.modelIdBuffer = new int[width * height];

        this.pipeline = new Pipeline(fb, db, tmpTriangleIdBuffer, tmpModelIdBuffer);
    }

    public void loadSceneFromJson(String json) throws Exception {
        log.debug("Loading scene from JSON (length={})", json != null ? json.length() : 0);
        this.scene = SceneSerializer.fromJson(json);
        log.info("Scene loaded: {}", scene);
    }

    public String saveSceneToJson() throws Exception {
        log.debug("Serializing scene to JSON");
        String json = SceneSerializer.toJson(scene);
        log.debug("Serialized JSON length={}", json != null ? json.length() : 0);
        return json;
    }

    public void render() {
        log.info("Starting render");
        if (scene == null) {
            log.error("Cannot render: no scene loaded");
            throw new IllegalStateException("No scene loaded");
        }

        fb.clear(Color.BLACK);
        db.clear();
        Arrays.fill(tmpTriangleIdBuffer, -1);
        Arrays.fill(tmpModelIdBuffer, -1);

        pipeline.renderScene(scene);

        System.arraycopy(tmpTriangleIdBuffer, 0, triangleIdBuffer, 0, triangleIdBuffer.length);
        System.arraycopy(tmpModelIdBuffer, 0, modelIdBuffer, 0, modelIdBuffer.length);

        Graphics2D g = stableFb.getImage().createGraphics();
        try {
            g.drawImage(fb.getImage(), 0, 0, null);
        } finally {
            g.dispose();
        }

        log.info("Render complete");
    }

    public BufferedImage getFrame() {
        log.debug("Retrieving frame image");
        return stableFb.getImage();
    }

    public int getModelIdAt(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new IllegalArgumentException(
                    String.format("Coordinates (%d,%d) out of bounds", x, y)
            );
        }
        return modelIdBuffer[y * width + x];
    }

    public int getTriangleIdAt(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new IllegalArgumentException(
                    String.format("Coordinates (%d,%d) out of bounds", x, y)
            );
        }
        return triangleIdBuffer[y * width + x];
    }

    public void loadModelFromJson(String json) throws Exception {
        log.info("Loading model into scene from JSON (length={})", json != null ? json.length() : 0);
        Model model = ModelSerializer.fromJson(json);
        scene.addModel(model);
        scene.reindexModels();
        log.info("Model id={} added and scene reindexed", model.getId());
    }

    public void removeModelById(int id) {
        log.info("Removing model id={} from scene", id);
        scene.getModels().removeIf(m -> m.getId() == id);
        scene.reindexModels();
        log.info("Model id={} removed and scene reindexed", id);
    }

    public String saveModelToJson(int modelId) throws Exception {
        log.info("Serializing model id={} to JSON", modelId);
        Model model = scene.getModels().stream()
                .filter(m -> m.getId() == modelId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No model with id=" + modelId));
        return ModelSerializer.toJson(model);
    }

    public Scene getScene() {
        log.trace("getScene() => {}", scene);
        return scene;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
