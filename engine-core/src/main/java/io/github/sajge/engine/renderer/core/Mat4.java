package io.github.sajge.engine.renderer.core;

import io.github.sajge.logger.Logger;

public class Mat4 {
    private static final Logger log = Logger.get(Mat4.class);

    public float m00, m01, m02, m03;
    public float m10, m11, m12, m13;
    public float m20, m21, m22, m23;
    public float m30, m31, m32, m33;

    public Mat4() {
    }

    public static Mat4 identity() {
        log.debug("Creating identity matrix");
        Mat4 I = new Mat4();
        I.m00 = 1;
        I.m11 = 1;
        I.m22 = 1;
        I.m33 = 1;
        return I;
    }

    public static Mat4 translate(float x, float y, float z) {
        log.debug("Creating translation matrix with x={}, y={}, z={}", x, y, z);
        Mat4 M = identity();
        M.m03 = x;
        M.m13 = y;
        M.m23 = z;
        return M;
    }

    public static Mat4 rotateX(float a) {
        log.debug("Creating rotation matrix around X-axis with angle={}", a);
        float c = (float) Math.cos(a);
        float s = (float) Math.sin(a);
        Mat4 M = identity();
        M.m11 = c;
        M.m12 = -s;
        M.m21 = s;
        M.m22 = c;
        return M;
    }

    public static Mat4 rotateY(float a) {
        log.debug("Creating rotation matrix around Y-axis with angle={}", a);
        float c = (float) Math.cos(a);
        float s = (float) Math.sin(a);
        Mat4 M = identity();
        M.m00 = c;
        M.m02 = s;
        M.m20 = -s;
        M.m22 = c;
        return M;
    }

    public static Mat4 rotateZ(float a) {
        log.debug("Creating rotation matrix around Z-axis with angle={}", a);
        float c = (float) Math.cos(a);
        float s = (float) Math.sin(a);
        Mat4 M = identity();
        M.m00 = c;
        M.m01 = -s;
        M.m10 = s;
        M.m11 = c;
        return M;
    }

    public static Mat4 scale(float x, float y, float z) {
        log.debug("Creating scale matrix with x={}, y={}, z={}", x, y, z);
        Mat4 M = new Mat4();
        M.m00 = x;
        M.m11 = y;
        M.m22 = z;
        M.m33 = 1;
        return M;
    }

    public static Mat4 perspective(float fovY, float aspect, float near, float far) {
        log.debug("Creating perspective matrix with fovY={}, aspect={}, near={}, far={}", fovY, aspect, near, far);
        float f = 1f / (float) Math.tan(fovY / 2);
        Mat4 M = new Mat4();
        M.m00 = f / aspect;
        M.m11 = f;
        M.m22 = (far + near) / (near - far);
        M.m23 = (2 * far * near) / (near - far);
        M.m32 = -1;
        return M;
    }

    public Mat4 mul(Mat4 o) {
        log.trace("Multiplying two matrices");
        float[] a = {m00, m01, m02, m03,
                m10, m11, m12, m13,
                m20, m21, m22, m23,
                m30, m31, m32, m33};
        float[] b = {o.m00, o.m01, o.m02, o.m03,
                o.m10, o.m11, o.m12, o.m13,
                o.m20, o.m21, o.m22, o.m23,
                o.m30, o.m31, o.m32, o.m33};
        float[] c = new float[16];

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                float sum = 0;
                for (int k = 0; k < 4; k++) {
                    sum += a[i * 4 + k] * b[k * 4 + j];
                }
                c[i * 4 + j] = sum;
            }
        }

        Mat4 r = new Mat4();
        r.m00 = c[0];  r.m01 = c[1];  r.m02 = c[2];  r.m03 = c[3];
        r.m10 = c[4];  r.m11 = c[5];  r.m12 = c[6];  r.m13 = c[7];
        r.m20 = c[8];  r.m21 = c[9];  r.m22 = c[10]; r.m23 = c[11];
        r.m30 = c[12]; r.m31 = c[13]; r.m32 = c[14]; r.m33 = c[15];

        log.trace("Matrix multiplication result: [{}..]", r);
        return r;
    }

    public Vec4 mul(Vec4 v) {
        log.trace("Transforming Vec4 {} by matrix", v);
        Vec4 result = new Vec4(
                m00 * v.x + m01 * v.y + m02 * v.z + m03 * v.w,
                m10 * v.x + m11 * v.y + m12 * v.z + m13 * v.w,
                m20 * v.x + m21 * v.y + m22 * v.z + m23 * v.w,
                m30 * v.x + m31 * v.y + m32 * v.z + m33 * v.w
        );
        log.trace("Result Vec4 {}", result);
        return result;
    }

    @Override
    public String toString() {
        return String.format(
                "Mat4[\n" +
                        "  [%.3f, %.3f, %.3f, %.3f],\n" +
                        "  [%.3f, %.3f, %.3f, %.3f],\n" +
                        "  [%.3f, %.3f, %.3f, %.3f],\n" +
                        "  [%.3f, %.3f, %.3f, %.3f]\n" +
                        "]",
                m00, m01, m02, m03,
                m10, m11, m12, m13,
                m20, m21, m22, m23,
                m30, m31, m32, m33
        );
    }
}
