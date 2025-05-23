package io.github.sajge.engine;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sajge.engine.renderer.SceneSerializer;
import io.github.sajge.engine.renderer.scene.Scene;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class SceneSerializationTest {
    public static void main(String[] args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        String path = "/scene.json";
        try (InputStream in = SceneSerializationTest.class.getResourceAsStream(path)) {
            if (in == null) {
                System.err.println("Resource not found: " + path);
                System.exit(1);
            }

            String originalJson = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))
                    .lines().collect(Collectors.joining("\n"));
            System.out.println("=== Original JSON ===");
            System.out.println(originalJson);

            Scene scene = SceneSerializer.fromJson(originalJson);

            String outputJson = SceneSerializer.toJson(scene);
            System.out.println("\n=== Re-serialized JSON ===");
            System.out.println(outputJson);

            JsonNode origNode = mapper.readTree(originalJson);
            JsonNode outNode  = mapper.readTree(outputJson);
            boolean equal = origNode.equals(outNode);
            System.out.println("\nStructural equality: " + equal);

            if (!equal) {
                System.out.println("  Original has " + origNode.size() + " top‐level fields");
                System.out.println("  Output   has " + outNode.size()  + " top‐level fields");
            }
        }
    }
}
