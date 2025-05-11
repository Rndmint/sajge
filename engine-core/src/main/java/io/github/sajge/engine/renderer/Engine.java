package io.github.sajge.engine.renderer;

import io.github.sajge.engine.renderer.buffer.DepthBuffer;
import io.github.sajge.engine.renderer.buffer.FrameBuffer;
import io.github.sajge.engine.renderer.pipeline.Pipeline;
import io.github.sajge.engine.renderer.scene.Scene;
import io.github.sajge.logger.Logger;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.nio.IntBuffer;
import java.util.Arrays;

public class Engine {
    private static final Logger log = Logger.get(Engine.class);

    private final int width;
    private final int height;
    private final FrameBuffer fb;
    private final DepthBuffer db;
    private final int[] tmpTriangleIdBuffer;
    private final int[] tmpModelIdBuffer;
    private final Pipeline pipeline;
    private final int[] triangleIdBuffer;
    private final int[] modelIdBuffer;
    private Scene scene;

    public Engine(int width, int height) {
        log.info("Initializing Engine with width={} and height={}", width, height);
        this.width = width;
        this.height = height;
        this.fb = new FrameBuffer(width, height);
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

        System.arraycopy(tmpTriangleIdBuffer, 0, triangleIdBuffer, 0, triangleIdBuffer.length);
        System.arraycopy(tmpModelIdBuffer, 0, modelIdBuffer, 0, modelIdBuffer.length);

        fb.clear(Color.BLACK);
        db.clear();

        Arrays.fill(tmpTriangleIdBuffer, -1);
        Arrays.fill(tmpModelIdBuffer, -1);

        pipeline.renderScene(scene);
        log.info("Render complete");
    }

    public BufferedImage getFrame() {
        log.debug("Retrieving frame image");
        return fb.getImage();
    }

    public int getTriangleById() {
        log.debug("Retrieving Triangle by ID");
        return 0;
    }

    public int getModelById() {
        log.debug("Retrieving Triangle by ID");
        return 0;
    }

    public IntBuffer getTmpTriangleIdBuffer() {
        log.debug("Retrieving Model ID");
        return IntBuffer.wrap(tmpTriangleIdBuffer);
    }

    public IntBuffer getTmpModelIdBuffer() {
        log.debug("Retrieving Model ID");
        return IntBuffer.wrap(tmpModelIdBuffer);
    }

    public int getWidth() {
        log.trace("getWidth() => {}", width);
        return width;
    }

    public int getHeight() {
        log.trace("getHeight() => {}", height);
        return height;
    }

    public Scene getScene() {
        log.trace("getScene() => {}", scene);
        return scene;
    }
}
