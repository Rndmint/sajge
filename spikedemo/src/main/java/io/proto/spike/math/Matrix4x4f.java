package io.proto.spike.math;

public class Matrix4x4f {
    public float m00, m01, m02, m03;
    public float m10, m11, m12, m13;
    public float m20, m21, m22, m23;
    public float m30, m31, m32, m33;

    public Matrix4x4f() {
        this(0, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0);
    }

    public Matrix4x4f(
            float m00, float m01, float m02, float m03,
            float m10, float m11, float m12, float m13,
            float m20, float m21, float m22, float m23,
            float m30, float m31, float m32, float m33
    ) {
        this.m00 = m00; this.m01 = m01; this.m02 = m02; this.m03 = m03;
        this.m10 = m10; this.m11 = m11; this.m12 = m12; this.m13 = m13;
        this.m20 = m20; this.m21 = m21; this.m22 = m22; this.m23 = m23;
        this.m30 = m30; this.m31 = m31; this.m32 = m32; this.m33 = m33;
    }

    public void setIdentity() {
        m00 = m11 = m22 = m33 = 1;
        m01 = m02 = m03 = 0;
        m10 = m12 = m13 = 0;
        m20 = m21 = m23 = 0;
        m30 = m31 = m32 = 0;
    }

    public Matrix4x4f add(Matrix4x4f o) {
        return new Matrix4x4f(
                m00 + o.m00, m01 + o.m01, m02 + o.m02, m03 + o.m03,
                m10 + o.m10, m11 + o.m11, m12 + o.m12, m13 + o.m13,
                m20 + o.m20, m21 + o.m21, m22 + o.m22, m23 + o.m23,
                m30 + o.m30, m31 + o.m31, m32 + o.m32, m33 + o.m33
        );
    }

    public Matrix4x4f sub(Matrix4x4f o) {
        return new Matrix4x4f(
                m00 - o.m00, m01 - o.m01, m02 - o.m02, m03 - o.m03,
                m10 - o.m10, m11 - o.m11, m12 - o.m12, m13 - o.m13,
                m20 - o.m20, m21 - o.m21, m22 - o.m22, m23 - o.m23,
                m30 - o.m30, m31 - o.m31, m32 - o.m32, m33 - o.m33
        );
    }

    public Matrix4x4f scale(float s) {
        return new Matrix4x4f(
                m00 * s, m01 * s, m02 * s, m03 * s,
                m10 * s, m11 * s, m12 * s, m13 * s,
                m20 * s, m21 * s, m22 * s, m23 * s,
                m30 * s, m31 * s, m32 * s, m33 * s
        );
    }

    public Matrix4x4f multiply(Matrix4x4f o) {
        return new Matrix4x4f(
                m00 * o.m00 + m01 * o.m10 + m02 * o.m20 + m03 * o.m30,
                m00 * o.m01 + m01 * o.m11 + m02 * o.m21 + m03 * o.m31,
                m00 * o.m02 + m01 * o.m12 + m02 * o.m22 + m03 * o.m32,
                m00 * o.m03 + m01 * o.m13 + m02 * o.m23 + m03 * o.m33,

                m10 * o.m00 + m11 * o.m10 + m12 * o.m20 + m13 * o.m30,
                m10 * o.m01 + m11 * o.m11 + m12 * o.m21 + m13 * o.m31,
                m10 * o.m02 + m11 * o.m12 + m12 * o.m22 + m13 * o.m32,
                m10 * o.m03 + m11 * o.m13 + m12 * o.m23 + m13 * o.m33,

                m20 * o.m00 + m21 * o.m10 + m22 * o.m20 + m23 * o.m30,
                m20 * o.m01 + m21 * o.m11 + m22 * o.m21 + m23 * o.m31,
                m20 * o.m02 + m21 * o.m12 + m22 * o.m22 + m23 * o.m32,
                m20 * o.m03 + m21 * o.m13 + m22 * o.m23 + m23 * o.m33,

                m30 * o.m00 + m31 * o.m10 + m32 * o.m20 + m33 * o.m30,
                m30 * o.m01 + m31 * o.m11 + m32 * o.m21 + m33 * o.m31,
                m30 * o.m02 + m31 * o.m12 + m32 * o.m22 + m33 * o.m32,
                m30 * o.m03 + m31 * o.m13 + m32 * o.m23 + m33 * o.m33
        );
    }

    public Vector4f multiply(Vector4f v) {
        return new Vector4f(
                m00 * v.x + m01 * v.y + m02 * v.z + m03 * v.w,
                m10 * v.x + m11 * v.y + m12 * v.z + m13 * v.w,
                m20 * v.x + m21 * v.y + m22 * v.z + m23 * v.w,
                m30 * v.x + m31 * v.y + m32 * v.z + m33 * v.w
        );
    }

    public Matrix4x4f transpose() {
        return new Matrix4x4f(
                m00, m10, m20, m30,
                m01, m11, m21, m31,
                m02, m12, m22, m32,
                m03, m13, m23, m33
        );
    }

    public float determinant() {
        return m00 * cofactorComponent(0, 0)
                - m01 * cofactorComponent(0, 1)
                + m02 * cofactorComponent(0, 2)
                - m03 * cofactorComponent(0, 3);
    }

    private float cofactorComponent(int row, int col) {
        float[] temp = new float[9];
        int index = 0;
        for (int r = 0; r < 4; r++) {
            if (r == row) continue;
            for (int c = 0; c < 4; c++) {
                if (c == col) continue;
                temp[index++] = get(r, c);
            }
        }
        return determinant3x3(
                temp[0], temp[1], temp[2],
                temp[3], temp[4], temp[5],
                temp[6], temp[7], temp[8]
        );
    }

    private float determinant3x3(float a00, float a01, float a02,
                                 float a10, float a11, float a12,
                                 float a20, float a21, float a22) {
        return a00 * (a11 * a22 - a12 * a21)
                - a01 * (a10 * a22 - a12 * a20)
                + a02 * (a10 * a21 - a11 * a20);
    }

    public Matrix4x4f cofactor() {
        Matrix4x4f result = new Matrix4x4f();
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                float sign = ((row + col) % 2 == 0) ? 1 : -1;
                result.set(row, col, sign * cofactorComponent(row, col));
            }
        }
        return result;
    }

    public Matrix4x4f inverse() {
        float det = determinant();
        if (det == 0) return null;
        return cofactor().transpose().scale(1.0f / det);
    }

    public float get(int row, int col) {
        return switch (row) {
            case 0 -> switch (col) {
                case 0 -> m00;
                case 1 -> m01;
                case 2 -> m02;
                case 3 -> m03;
                default -> throw new IndexOutOfBoundsException();
            };
            case 1 -> switch (col) {
                case 0 -> m10;
                case 1 -> m11;
                case 2 -> m12;
                case 3 -> m13;
                default -> throw new IndexOutOfBoundsException();
            };
            case 2 -> switch (col) {
                case 0 -> m20;
                case 1 -> m21;
                case 2 -> m22;
                case 3 -> m23;
                default -> throw new IndexOutOfBoundsException();
            };
            case 3 -> switch (col) {
                case 0 -> m30;
                case 1 -> m31;
                case 2 -> m32;
                case 3 -> m33;
                default -> throw new IndexOutOfBoundsException();
            };
            default -> throw new IndexOutOfBoundsException();
        };
    }

    public void set(int row, int col, float value) {
        switch (row) {
            case 0: switch (col) {
                case 0 -> m00 = value; case 1 -> m01 = value;
                case 2 -> m02 = value; case 3 -> m03 = value;
                default -> throw new IndexOutOfBoundsException();
            };
            return;
            case 1: switch (col) {
                case 0 -> m10 = value; case 1 -> m11 = value;
                case 2 -> m12 = value; case 3 -> m13 = value;
                default -> throw new IndexOutOfBoundsException();
            };
            return;
            case 2: switch (col) {
                case 0 -> m20 = value; case 1 -> m21 = value;
                case 2 -> m22 = value; case 3 -> m23 = value;
                default -> throw new IndexOutOfBoundsException();
            };
            return;
            case 3: switch (col) {
                case 0 -> m30 = value; case 1 -> m31 = value;
                case 2 -> m32 = value; case 3 -> m33 = value;
                default -> throw new IndexOutOfBoundsException();
            };
            return;
            default: throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Matrix4x4f)) return false;
        Matrix4x4f o = (Matrix4x4f) obj;
        return Float.compare(m00, o.m00) == 0 && Float.compare(m01, o.m01) == 0 &&
                Float.compare(m02, o.m02) == 0 && Float.compare(m03, o.m03) == 0 &&
                Float.compare(m10, o.m10) == 0 && Float.compare(m11, o.m11) == 0 &&
                Float.compare(m12, o.m12) == 0 && Float.compare(m13, o.m13) == 0 &&
                Float.compare(m20, o.m20) == 0 && Float.compare(m21, o.m21) == 0 &&
                Float.compare(m22, o.m22) == 0 && Float.compare(m23, o.m23) == 0 &&
                Float.compare(m30, o.m30) == 0 && Float.compare(m31, o.m31) == 0 &&
                Float.compare(m32, o.m32) == 0 && Float.compare(m33, o.m33) == 0;
    }

    @Override
    public String toString() {
        return String.format(
                "[[%f, %f, %f, %f],\n [%f, %f, %f, %f],\n [%f, %f, %f, %f],\n [%f, %f, %f, %f]]",
                m00, m01, m02, m03,
                m10, m11, m12, m13,
                m20, m21, m22, m23,
                m30, m31, m32, m33
        ).replaceAll(",(?!\\s)", ".");
    }
}
