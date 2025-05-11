package io.github.sajge.engine.renderer.scene;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.sajge.engine.renderer.core.Vec3;
import io.github.sajge.logger.Logger;

import java.util.ArrayList;
import java.util.List;

public class Mesh {
    private static final Logger log = Logger.get(Mesh.class);

    private List<Vec3> vertices;
    private List<Triangle> triangles;

    public Mesh() {
        log.debug("Initializing Mesh with empty vertex and triangle lists");
        this.vertices = new ArrayList<>();
        this.triangles = new ArrayList<>();
    }

    @JsonProperty("vertices")
    public List<Vec3> getVertices() {
        log.trace("getVertices() => {}", vertices);
        return vertices;
    }

    @JsonProperty("vertices")
    public void setVertices(List<Vec3> vertices) {
        log.trace("setVertices(size={})", vertices != null ? vertices.size() : 0);
        this.vertices = vertices;
    }

    @JsonProperty("triangles")
    public List<Triangle> getTriangles() {
        log.trace("getTriangles() => {} triangles", triangles != null ? triangles.size() : 0);
        return triangles;
    }

    @JsonProperty("triangles")
    public void setTriangles(List<Triangle> triangles) {
        log.trace("setTriangles(size={})", triangles != null ? triangles.size() : 0);
        this.triangles = triangles;
    }
}
