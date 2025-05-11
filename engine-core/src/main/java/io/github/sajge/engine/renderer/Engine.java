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
    private final int[] idBuffer;
    private final Pipeline pipeline;
    private Scene scene;

    public Engine(int width, int height) {
        log.info("Initializing Engine with width={} and height={}", width, height);
        this.width = width;
        this.height = height;
        this.fb = new FrameBuffer(width, height);
        this.db = new DepthBuffer(width, height);
        this.idBuffer = new int[width * height];
        this.pipeline = new Pipeline(fb, db, idBuffer);
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
        Arrays.fill(idBuffer, -1);

        pipeline.renderScene(scene);
        log.info("Render complete");
    }

    public BufferedImage getFrame() {
        log.debug("Retrieving frame image");
        return fb.getImage();
    }

    public IntBuffer getIdBuffer() {
        log.debug("Retrieving ID buffer");
        return IntBuffer.wrap(idBuffer);
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
