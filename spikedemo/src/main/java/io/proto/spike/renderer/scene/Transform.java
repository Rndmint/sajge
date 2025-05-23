package io.proto.spike.renderer.scene;

import io.proto.spike.renderer.core.Mat4;
import io.proto.spike.renderer.core.Vec3;

public class Transform {
    public Vec3 position = new Vec3();
    public Vec3 rotation = new Vec3(); // Euler angles
    public Vec3 scale = new Vec3(1, 1, 1);

    public Transform() {}

    public Mat4 toMatrix() {
        return Mat4.translate(position.x, position.y, position.z)
                .mul(Mat4.rotateX(rotation.x))
                .mul(Mat4.rotateY(rotation.y))
                .mul(Mat4.rotateZ(rotation.z))
                .mul(Mat4.scale(scale.x, scale.y, scale.z));
    }

    @Override
    public String toString() {
        return "Transform{" +
                "position=" + position +
                ", rotation=" + rotation +
                ", scale=" + scale +
                '}';
    }
}
