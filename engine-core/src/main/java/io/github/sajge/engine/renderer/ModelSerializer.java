package io.github.sajge.engine.renderer;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sajge.engine.renderer.scene.Model;
import io.github.sajge.logger.Logger;

public class ModelSerializer {
    private static final Logger log = Logger.get(ModelSerializer.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    public static Model fromJson(String json) throws Exception {
        log.debug("Deserializing Model from JSON (length={})", json != null ? json.length() : 0);
        Model model = mapper.readValue(json, Model.class);
        log.info("Deserialized Model: id={} ", model.getId());
        return model;
    }

    public static String toJson(Model model) throws Exception {
        log.debug("Serializing Model to JSON (id={})", model.getId());
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(model);
        log.debug("Serialized Model JSON length={}", json.length());
        return json;
    }
}
