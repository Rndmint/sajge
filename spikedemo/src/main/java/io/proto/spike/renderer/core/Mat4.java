package io.proto.spike.renderer.core;

public class Mat4 {
    public float m00, m01, m02, m03;
    public float m10, m11, m12, m13;
    public float m20, m21, m22, m23;
    public float m30, m31, m32, m33;

    public Mat4() {}

    public static Mat4 identity() {
        Mat4 I = new Mat4();
        I.m00 = 1;
        I.m11 = 1;
        I.m22 = 1;
        I.m33 = 1;
        return I;
    }

    public static Mat4 translate(float x, float y, float z) {
        Mat4 M = identity();
        M.m03 = x;
        M.m13 = y;
        M.m23 = z;
        return M;
    }

    public static Mat4 rotateX(float a) {
        float c = (float) Math.cos(a), s = (float) Math.sin(a);
        Mat4 M = identity();
        M.m11 = c;
        M.m12 = -s;
        M.m21 = s;
        M.m22 = c;
        return M;
    }

    public static Mat4 rotateY(float a) {
        float c = (float) Math.cos(a), s = (float) Math.sin(a);
        Mat4 M = identity();
        M.m00 = c;
        M.m02 = s;
        M.m20 = -s;
        M.m22 = c;
        return M;
    }

    public static Mat4 rotateZ(float a) {
        float c = (float) Math.cos(a), s = (float) Math.sin(a);
        Mat4 M = identity();
        M.m00 = c;
        M.m01 = -s;
        M.m10 = s;
        M.m11 = c;
        return M;
    }

    public static Mat4 scale(float x, float y, float z) {
        Mat4 M = new Mat4();
        M.m00 = x;
        M.m11 = y;
        M.m22 = z;
        M.m33 = 1;
        return M;
    }

    public static Mat4 perspective(float fovY, float aspect, float near, float far) {
        float f = 1.0f / (float) Math.tan(fovY / 2);
        Mat4 M = new Mat4();
        M.m00 = f / aspect;
        M.m11 = f;
        M.m22 = (far + near) / (near - far);
        M.m23 = (2 * far * near) / (near - far);
        M.m32 = -1;
        return M;
    }

    public Mat4 mul(Mat4 o) {
        Mat4 r = new Mat4();
        float[] a = {m00, m01, m02, m03, m10, m11, m12, m13,
                m20, m21, m22, m23, m30, m31, m32, m33};
        float[] b = {o.m00, o.m01, o.m02, o.m03, o.m10, o.m11, o.m12, o.m13,
                o.m20, o.m21, o.m22, o.m23, o.m30, o.m31, o.m32, o.m33};
        float[] c = new float[16];

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                c[i * 4 + j] = 0;
                for (int k = 0; k < 4; k++) {
                    c[i * 4 + j] += a[i * 4 + k] * b[k * 4 + j];
                }
            }
        }

        r.m00 = c[0];  r.m01 = c[1];  r.m02 = c[2];  r.m03 = c[3];
        r.m10 = c[4];  r.m11 = c[5];  r.m12 = c[6];  r.m13 = c[7];
        r.m20 = c[8];  r.m21 = c[9];  r.m22 = c[10]; r.m23 = c[11];
        r.m30 = c[12]; r.m31 = c[13]; r.m32 = c[14]; r.m33 = c[15];
        return r;
    }

    public Vec4 mul(Vec4 v) {
        return new Vec4(
                m00 * v.x + m01 * v.y + m02 * v.z + m03 * v.w,
                m10 * v.x + m11 * v.y + m12 * v.z + m13 * v.w,
                m20 * v.x + m21 * v.y + m22 * v.z + m23 * v.w,
                m30 * v.x + m31 * v.y + m32 * v.z + m33 * v.w
        );
    }

    public Mat4 transpose() {
        Mat4 t = new Mat4();
        t.m00 = m00; t.m01 = m10; t.m02 = m20; t.m03 = m30;
        t.m10 = m01; t.m11 = m11; t.m12 = m21; t.m13 = m31;
        t.m20 = m02; t.m21 = m12; t.m22 = m22; t.m23 = m32;
        t.m30 = m03; t.m31 = m13; t.m32 = m23; t.m33 = m33;
        return t;
    }

    public Mat4 invert() {
        float[] m = {m00, m01, m02, m03, m10, m11, m12, m13,
                m20, m21, m22, m23, m30, m31, m32, m33};
        float[] inv = new float[16];

        inv[0] = m[5] * m[10] * m[15] - m[5] * m[11] * m[14] - m[9] * m[6] * m[15]
                + m[9] * m[7] * m[14] + m[13] * m[6] * m[11] - m[13] * m[7] * m[10];

        inv[4] = -m[4] * m[10] * m[15] + m[4] * m[11] * m[14] + m[8] * m[6] * m[15]
                - m[8] * m[7] * m[14] - m[12] * m[6] * m[11] + m[12] * m[7] * m[10];

        inv[8] = m[4] * m[9] * m[15] - m[4] * m[11] * m[13] - m[8] * m[5] * m[15]
                + m[8] * m[7] * m[13] + m[12] * m[5] * m[11] - m[12] * m[7] * m[9];

        inv[12] = -m[4] * m[9] * m[14] + m[4] * m[10] * m[13] + m[8] * m[5] * m[14]
                - m[8] * m[6] * m[13] - m[12] * m[5] * m[10] + m[12] * m[6] * m[9];

        inv[1] = -m[1] * m[10] * m[15] + m[1] * m[11] * m[14] + m[9] * m[2] * m[15]
                - m[9] * m[3] * m[14] - m[13] * m[2] * m[11] + m[13] * m[3] * m[10];

        inv[5] = m[0] * m[10] * m[15] - m[0] * m[11] * m[14] - m[8] * m[2] * m[15]
                + m[8] * m[3] * m[14] + m[12] * m[2] * m[11] - m[12] * m[3] * m[10];

        inv[9] = -m[0] * m[9] * m[15] + m[0] * m[11] * m[13] + m[8] * m[1] * m[15]
                - m[8] * m[3] * m[13] - m[12] * m[1] * m[11] + m[12] * m[3] * m[9];

        inv[13] = m[0] * m[9] * m[14] - m[0] * m[10] * m[13] - m[8] * m[1] * m[14]
                + m[8] * m[2] * m[13] + m[12] * m[1] * m[10] - m[12] * m[2] * m[9];

        inv[2] = m[1] * m[6] * m[15] - m[1] * m[7] * m[14] - m[5] * m[2] * m[15]
                + m[5] * m[3] * m[14] + m[13] * m[2] * m[7] - m[13] * m[3] * m[6];

        inv[6] = -m[0] * m[6] * m[15] + m[0] * m[7] * m[14] + m[4] * m[2] * m[15]
                - m[4] * m[3] * m[14] - m[12] * m[2] * m[7] + m[12] * m[3] * m[6];

        inv[10] = m[0] * m[5] * m[15] - m[0] * m[7] * m[13] - m[4] * m[1] * m[15]
                + m[4] * m[3] * m[13] + m[12] * m[1] * m[7] - m[12] * m[3] * m[5];

        inv[14] = -m[0] * m[5] * m[14] + m[0] * m[6] * m[13] + m[4] * m[1] * m[14]
                - m[4] * m[2] * m[13] - m[12] * m[1] * m[6] + m[12] * m[2] * m[5];

        inv[3] = -m[1] * m[6] * m[11] + m[1] * m[7] * m[10] + m[5] * m[2] * m[11]
                - m[5] * m[3] * m[10] - m[9] * m[2] * m[7] + m[9] * m[3] * m[6];

        inv[7] = m[0] * m[6] * m[11] - m[0] * m[7] * m[10] - m[4] * m[2] * m[11]
                + m[4] * m[3] * m[10] + m[8] * m[2] * m[7] - m[8] * m[3] * m[6];

        inv[11] = -m[0] * m[5] * m[11] + m[0] * m[7] * m[9] + m[4] * m[1] * m[11]
                - m[4] * m[3] * m[9] - m[8] * m[1] * m[7] + m[8] * m[3] * m[5];

        inv[15] = m[0] * m[5] * m[10] - m[0] * m[6] * m[9] - m[4] * m[1] * m[10]
                + m[4] * m[2] * m[9] + m[8] * m[1] * m[6] - m[8] * m[2] * m[5];

        float det = m[0] * inv[0] + m[1] * inv[4] + m[2] * inv[8] + m[3] * inv[12];
        if (det == 0) throw new ArithmeticException("Mat4 is singular");

        det = 1 / det;
        Mat4 r = new Mat4();
        float[] in = inv;

        r.m00 = in[0] * det; r.m01 = in[1] * det; r.m02 = in[2] * det; r.m03 = in[3] * det;
        r.m10 = in[4] * det; r.m11 = in[5] * det; r.m12 = in[6] * det; r.m13 = in[7] * det;
        r.m20 = in[8] * det; r.m21 = in[9] * det; r.m22 = in[10] * det; r.m23 = in[11] * det;
        r.m30 = in[12] * det; r.m31 = in[13] * det; r.m32 = in[14] * det; r.m33 = in[15] * det;

        return r;
    }

    @Override
    public String toString() {
        return "Mat4{" +
                "m00=" + m00 +
                ", m01=" + m01 +
                ", m02=" + m02 +
                ", m03=" + m03 +
                ", m10=" + m10 +
                ", m11=" + m11 +
                ", m12=" + m12 +
                ", m13=" + m13 +
                ", m20=" + m20 +
                ", m21=" + m21 +
                ", m22=" + m22 +
                ", m23=" + m23 +
                ", m30=" + m30 +
                ", m31=" + m31 +
                ", m32=" + m32 +
                ", m33=" + m33 +
                '}';
    }
}
