package io.github.sajge.client;

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;

public class ConfigLoader {
    public static <T> T loadConfig(Class<T> type, String resourceName) {
        LoaderOptions opts = new LoaderOptions();
        Constructor ctor = new Constructor(type, opts);
        Yaml yaml = new Yaml(ctor);
        InputStream in = type.getClassLoader().getResourceAsStream(resourceName);
        return yaml.load(in);
    }
}
