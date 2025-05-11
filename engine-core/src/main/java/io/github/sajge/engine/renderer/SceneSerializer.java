package io.github.sajge.engine.renderer;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sajge.engine.renderer.scene.Scene;
import io.github.sajge.logger.Logger;

public class SceneSerializer {
    private static final Logger log = Logger.get(SceneSerializer.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    public static Scene fromJson(String json) throws Exception {
        log.debug("Deserializing Scene from JSON (length={})", json != null ? json.length() : 0);
        Scene scene = mapper.readValue(json, Scene.class);
        log.info("Deserialized Scene: {}", scene);
        return scene;
    }

    public static String toJson(Scene scene) throws Exception {
        log.debug("Serializing Scene to JSON");
        String json = mapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(scene);
        log.debug("Serialized JSON length={}", json != null ? json.length() : 0);
        return json;
    }
}
