package io.proto.spike.renderer.scene;

import io.proto.spike.renderer.core.Mat4;

public class Camera {
    public Transform transform;
    public float fovY, aspect, near, far;

    public Camera(Transform transform, float fovY, float aspect, float near, float far) {
    }

    public Mat4 getViewMatrix() {
        return null;
    }

    public Mat4 getProjectionMatrix() {
        return null;
    }
}
