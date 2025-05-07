package io.proto.spike.renderer.scene;

import io.proto.spike.renderer.core.Mat4;
import io.proto.spike.renderer.core.Vec3;

public class Transform {
    public Vec3 position;
    public Vec3 rotation; // Euler angles
    public Vec3 scale;

    public Transform() {
    }

    public Mat4 toMatrix() {
        return null;
    }
}
