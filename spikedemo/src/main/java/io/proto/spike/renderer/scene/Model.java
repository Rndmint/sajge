package io.proto.spike.renderer.scene;

import io.proto.spike.renderer.mesh.Mesh;

public class Model {
    public Mesh mesh;
    public Transform transform;
    public Material material;

    public Model(Mesh mesh, Transform t, Material m) {
        this.mesh = mesh;
        this.transform = t;
        this.material = m;
    }

    @Override
    public String toString() {
        return "Model{" +
                "mesh=" + mesh +
                ", transform=" + transform +
                ", material=" + material +
                '}';
    }
}
