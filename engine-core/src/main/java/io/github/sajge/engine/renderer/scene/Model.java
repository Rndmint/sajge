package io.github.sajge.engine.renderer.scene;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.sajge.logger.Logger;

public class Model {
    private static final Logger log = Logger.get(Model.class);

    private int id;
    private Mesh mesh;
    private Transform transform;
    private Material material;

    public Model() {
        log.debug("Created empty Model instance");
    }

    @JsonCreator
    public Model(
            @JsonProperty("id") int id,
            @JsonProperty("mesh") Mesh mesh,
            @JsonProperty("transform") Transform transform,
            @JsonProperty("material") Material material
    ) {
        log.debug("Creating Model with id={}, mesh={}, transform={}, material={}", id, mesh, transform, material);
        this.id = id;
        this.mesh = mesh;
        this.transform = transform;
        this.material = material;
    }

    @JsonProperty("id")
    public int getId() {
        log.trace("getId() => {}", id);
        return id;
    }

    @JsonProperty("id")
    public void setId(int id) {
        log.trace("setId({})", id);
        this.id = id;
    }

    @JsonProperty("mesh")
    public Mesh getMesh() {
        log.trace("getMesh() => {}", mesh);
        return mesh;
    }

    @JsonProperty("mesh")
    public void setMesh(Mesh mesh) {
        log.trace("setMesh({})", mesh);
        this.mesh = mesh;
    }

    @JsonProperty("transform")
    public Transform getTransform() {
        log.trace("getTransform() => {}", transform);
        return transform;
    }

    @JsonProperty("transform")
    public void setTransform(Transform transform) {
        log.trace("setTransform({})", transform);
        this.transform = transform;
    }

    @JsonProperty("material")
    public Material getMaterial() {
        log.trace("getMaterial() => {}", material);
        return material;
    }

    @JsonProperty("material")
    public void setMaterial(Material material) {
        log.trace("setMaterial({})", material);
        this.material = material;
    }

    public void applyMaterialColor() {
        java.awt.Color color = material.getColor();
        log.debug("Applying material color {} to {} triangles", color, mesh.getTriangles().size());
        for (Triangle t : mesh.getTriangles()) {
            t.setColor(color);
            log.trace("Triangle id={} colored {}", t.getId(), color);
        }
        log.info("applyMaterialColor complete");
    }

}
