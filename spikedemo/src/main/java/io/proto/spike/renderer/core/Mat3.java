package io.proto.spike.renderer.core;

public class Mat3 {
    // row-major fields m[row][col]
    public float m00, m01, m02;
    public float m10, m11, m12;
    public float m20, m21, m22;

    public Mat3() {
    }

    public static Mat3 identity() {
        return null;
    }

    public Mat3 mul(Mat3 other) {
        return null;
    }

    public Vec3 mul(Vec3 v) {
        return null;
    }

    public Mat3 transpose() {
        return null;
    }

    public Mat3 invert() {
        return null;
    }
}
