package io.github.sajge.server.config;

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ConfigLoader {
    public static <T> T load(Class<T> type, String resourceName) {
        InputStream inputStream = type.getClassLoader().getResourceAsStream(resourceName);
        if (inputStream == null) {
            throw new RuntimeException("Resource not found: " + resourceName);
        }
        try (InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            LoaderOptions options = new LoaderOptions();
            Constructor constructor = new Constructor(type, options);
            Yaml yaml = new Yaml(constructor);
            return yaml.load(reader);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load resource: " + resourceName, e);
        }
    }
}
