package io.github.sajge.engine.ui;

import io.github.sajge.engine.renderer.Engine;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class SceneLoader {
    public static void loadScene(Engine engine) {
        try (InputStream in = SceneLoader.class
                .getClassLoader()
                .getResourceAsStream("scene.json")) {
            if (in == null) throw new RuntimeException("scene.json not found");
            String json = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            engine.loadSceneFromJson(json);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load scene.json", e);
        }
    }
}
