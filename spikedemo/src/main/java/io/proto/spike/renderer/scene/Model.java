package io.proto.spike.renderer.scene;

import io.proto.spike.renderer.mesh.Mesh;

public class Model {
    public Mesh mesh;
    public Transform transform;
    public Material material;

    public Model(Mesh mesh, Transform transform, Material material) {
    }
}
