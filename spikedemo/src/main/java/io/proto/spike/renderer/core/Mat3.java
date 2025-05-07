package io.proto.spike.renderer.core;

public class Mat3 {
    public float m00, m01, m02;
    public float m10, m11, m12;
    public float m20, m21, m22;

    public Mat3() {}

    public static Mat3 identity() {
        Mat3 I = new Mat3();
        I.m00 = 1;
        I.m11 = 1;
        I.m22 = 1;
        return I;
    }

    public Mat3 mul(Mat3 o) {
        Mat3 r = new Mat3();
        r.m00 = m00 * o.m00 + m01 * o.m10 + m02 * o.m20;
        r.m01 = m00 * o.m01 + m01 * o.m11 + m02 * o.m21;
        r.m02 = m00 * o.m02 + m01 * o.m12 + m02 * o.m22;

        r.m10 = m10 * o.m00 + m11 * o.m10 + m12 * o.m20;
        r.m11 = m10 * o.m01 + m11 * o.m11 + m12 * o.m21;
        r.m12 = m10 * o.m02 + m11 * o.m12 + m12 * o.m22;

        r.m20 = m20 * o.m00 + m21 * o.m10 + m22 * o.m20;
        r.m21 = m20 * o.m01 + m21 * o.m11 + m22 * o.m21;
        r.m22 = m20 * o.m02 + m21 * o.m12 + m22 * o.m22;
        return r;
    }

    public Vec3 mul(Vec3 v) {
        return new Vec3(
                m00 * v.x + m01 * v.y + m02 * v.z,
                m10 * v.x + m11 * v.y + m12 * v.z,
                m20 * v.x + m21 * v.y + m22 * v.z
        );
    }

    public Mat3 transpose() {
        Mat3 t = new Mat3();
        t.m00 = m00; t.m01 = m10; t.m02 = m20;
        t.m10 = m01; t.m11 = m11; t.m12 = m21;
        t.m20 = m02; t.m21 = m12; t.m22 = m22;
        return t;
    }

    public Mat3 invert() {
        float det = m00 * (m11 * m22 - m12 * m21)
                - m01 * (m10 * m22 - m12 * m20)
                + m02 * (m10 * m21 - m11 * m20);

        if (det == 0) throw new ArithmeticException("Mat3 is singular");

        float invDet = 1.0f / det;
        Mat3 r = new Mat3();

        r.m00 =  (m11 * m22 - m12 * m21) * invDet;
        r.m01 = -(m01 * m22 - m02 * m21) * invDet;
        r.m02 =  (m01 * m12 - m02 * m11) * invDet;

        r.m10 = -(m10 * m22 - m12 * m20) * invDet;
        r.m11 =  (m00 * m22 - m02 * m20) * invDet;
        r.m12 = -(m00 * m12 - m02 * m10) * invDet;

        r.m20 =  (m10 * m21 - m11 * m20) * invDet;
        r.m21 = -(m00 * m21 - m01 * m20) * invDet;
        r.m22 =  (m00 * m11 - m01 * m10) * invDet;

        return r;
    }

    @Override
    public String toString() {
        return "Mat3{" +
                "m00=" + m00 +
                ", m01=" + m01 +
                ", m02=" + m02 +
                ", m10=" + m10 +
                ", m11=" + m11 +
                ", m12=" + m12 +
                ", m20=" + m20 +
                ", m21=" + m21 +
                ", m22=" + m22 +
                '}';
    }
}
