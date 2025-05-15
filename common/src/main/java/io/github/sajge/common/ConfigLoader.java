package io.github.sajge.common;

import org.yaml.snakeyaml.Yaml;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ConfigLoader {
    private static final Yaml yaml = new Yaml();

    public static <T> T load(String path, Class<T> clazz) throws Exception {
        try (InputStream in = Files.newInputStream(Paths.get(path))) {
            return yaml.loadAs(in, clazz);
        }
    }
}
