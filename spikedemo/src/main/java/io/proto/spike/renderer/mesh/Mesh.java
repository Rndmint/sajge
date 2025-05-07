package io.proto.spike.renderer.mesh;

import io.proto.spike.renderer.core.Vec3;

import java.util.ArrayList;
import java.util.List;

public class Mesh {
    public List<Vec3> vertices = new ArrayList<>();
    public List<Triangle> triangles = new ArrayList<>();
}
