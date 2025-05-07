package io.proto.spike.renderer.scene;

import io.proto.spike.renderer.core.Mat4;

public class Camera {
    public Transform transform;
    public float fovY, aspect, near, far;

    public Camera(Transform t, float f, float a, float n, float fa) {
        transform = t;
        fovY = f;
        aspect = a;
        near = n;
        far = fa;
    }

    public Mat4 getViewMatrix() {
        Transform t = transform;
        Mat4 rot = Mat4.rotateZ(-t.rotation.z)
                .mul(Mat4.rotateY(-t.rotation.y))
                .mul(Mat4.rotateX(-t.rotation.x));
        Mat4 trans = Mat4.translate(-t.position.x, -t.position.y, -t.position.z);
        return rot.mul(trans);
    }

    public Mat4 getProjectionMatrix() {
        return Mat4.perspective(fovY, aspect, near, far);
    }

    @Override
    public String toString() {
        return "Camera{" +
                "transform=" + transform +
                ", fovY=" + fovY +
                ", aspect=" + aspect +
                ", near=" + near +
                ", far=" + far +
                '}';
    }
}
