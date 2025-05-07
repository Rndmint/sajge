package io.proto.spike.renderer.mesh;

import io.proto.spike.renderer.core.Vec3;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MeshLoader {
    public static Mesh loadOBJ(String path) throws IOException {
        Mesh mesh = new Mesh();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("v ")) {
                    String[] tokens = line.split("\\s+");
                    if (tokens.length >= 4) {
                        float x = Float.parseFloat(tokens[1]);
                        float y = Float.parseFloat(tokens[2]);
                        float z = Float.parseFloat(tokens[3]);
                        mesh.vertices.add(new Vec3(x, y, z));
                    }
                } else if (line.startsWith("f ")) {
                    String[] tokens = line.split("\\s+");
                    if (tokens.length >= 4) {
                        int i0 = parseFaceIndex(tokens[1]);
                        int i1 = parseFaceIndex(tokens[2]);
                        int i2 = parseFaceIndex(tokens[3]);

                        Vec3 p0 = mesh.vertices.get(i0);
                        Vec3 p1 = mesh.vertices.get(i1);
                        Vec3 p2 = mesh.vertices.get(i2);

                        Vec3 edge1 = new Vec3(p1.x - p0.x, p1.y - p0.y, p1.z - p0.z);
                        Vec3 edge2 = new Vec3(p2.x - p0.x, p2.y - p0.y, p2.z - p0.z);
                        Vec3 normal = edge1.cross(edge2).normalize();

                        mesh.triangles.add(new Triangle(i0, i1, i2, normal, Color.LIGHT_GRAY));
                    }
                }
            }
        }
        return mesh;
    }

    private static int parseFaceIndex(String faceToken) {
        String[] parts = faceToken.split("/");
        return Integer.parseInt(parts[0]) - 1;
    }
}
