package io.proto.spike.renderer.core;

public class Mat4 {
    public float m00, m01, m02, m03;
    public float m10, m11, m12, m13;
    public float m20, m21, m22, m23;
    public float m30, m31, m32, m33;

    public Mat4() {
    }

    public static Mat4 identity() {
        return null;
    }

    public static Mat4 translate(float x, float y, float z) {
        return null;
    }

    public static Mat4 rotateX(float angle) {
        return null;
    }

    public static Mat4 rotateY(float angle) {
        return null;
    }

    public static Mat4 rotateZ(float angle) {
        return null;
    }

    public static Mat4 scale(float x, float y, float z) {
        return null;
    }

    public static Mat4 perspective(float fovY, float aspect, float near, float far) {
        return null;
    }

    public Mat4 mul(Mat4 other) {
        return null;
    }

    public Vec4 mul(Vec4 v) {
        return null;
    }

    public Mat4 transpose() {
        return null;
    }

    public Mat4 invert() {
        return null;
    }
}
