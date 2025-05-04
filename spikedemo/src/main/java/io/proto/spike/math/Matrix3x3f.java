package io.proto.spike.math;

public class Matrix3x3f {
    public float m00, m01, m02;
    public float m10, m11, m12;
    public float m20, m21, m22;

    public Matrix3x3f() {
        this(0, 0, 0,
                0, 0, 0,
                0, 0, 0);
    }

    public Matrix3x3f(
            float m00, float m01, float m02,
            float m10, float m11, float m12,
            float m20, float m21, float m22
    ) {
        this.m00 = m00; this.m01 = m01; this.m02 = m02;
        this.m10 = m10; this.m11 = m11; this.m12 = m12;
        this.m20 = m20; this.m21 = m21; this.m22 = m22;
    }

    public void setIdentity() {
        m00 = m11 = m22 = 1;
        m01 = m02 = m10 = m12 = m20 = m21 = 0;
    }

    public Matrix3x3f add(Matrix3x3f o) {
        return new Matrix3x3f(
                m00 + o.m00, m01 + o.m01, m02 + o.m02,
                m10 + o.m10, m11 + o.m11, m12 + o.m12,
                m20 + o.m20, m21 + o.m21, m22 + o.m22
        );
    }

    public Matrix3x3f sub(Matrix3x3f o) {
        return new Matrix3x3f(
                m00 - o.m00, m01 - o.m01, m02 - o.m02,
                m10 - o.m10, m11 - o.m11, m12 - o.m12,
                m20 - o.m20, m21 - o.m21, m22 - o.m22
        );
    }

    public Matrix3x3f scale(float s) {
        return new Matrix3x3f(
                m00 * s, m01 * s, m02 * s,
                m10 * s, m11 * s, m12 * s,
                m20 * s, m21 * s, m22 * s
        );
    }

    public Matrix3x3f multiply(Matrix3x3f o) {
        return new Matrix3x3f(
                m00 * o.m00 + m01 * o.m10 + m02 * o.m20,
                m00 * o.m01 + m01 * o.m11 + m02 * o.m21,
                m00 * o.m02 + m01 * o.m12 + m02 * o.m22,

                m10 * o.m00 + m11 * o.m10 + m12 * o.m20,
                m10 * o.m01 + m11 * o.m11 + m12 * o.m21,
                m10 * o.m02 + m11 * o.m12 + m12 * o.m22,

                m20 * o.m00 + m21 * o.m10 + m22 * o.m20,
                m20 * o.m01 + m21 * o.m11 + m22 * o.m21,
                m20 * o.m02 + m21 * o.m12 + m22 * o.m22
        );
    }

    public Vector3f multiply(Vector3f v) {
        return new Vector3f(
                m00 * v.x + m01 * v.y + m02 * v.z,
                m10 * v.x + m11 * v.y + m12 * v.z,
                m20 * v.x + m21 * v.y + m22 * v.z
        );
    }

    public float determinant() {
        return m00 * (m11 * m22 - m12 * m21)
                - m01 * (m10 * m22 - m12 * m20)
                + m02 * (m10 * m21 - m11 * m20);
    }

    public Matrix3x3f transpose() {
        return new Matrix3x3f(
                m00, m10, m20,
                m01, m11, m21,
                m02, m12, m22
        );
    }

    public Matrix3x3f inverse() {
        float det = determinant();
        if (det == 0) return null;

        float invDet = 1.0f / det;

        return new Matrix3x3f(
                (m11 * m22 - m12 * m21) * invDet,
                -(m01 * m22 - m02 * m21) * invDet,
                (m01 * m12 - m02 * m11) * invDet,

                -(m10 * m22 - m12 * m20) * invDet,
                (m00 * m22 - m02 * m20) * invDet,
                -(m00 * m12 - m02 * m10) * invDet,

                (m10 * m21 - m11 * m20) * invDet,
                -(m00 * m21 - m01 * m20) * invDet,
                (m00 * m11 - m01 * m10) * invDet
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Matrix3x3f)) return false;
        Matrix3x3f o = (Matrix3x3f) obj;
        return Float.compare(m00, o.m00) == 0 && Float.compare(m01, o.m01) == 0 && Float.compare(m02, o.m02) == 0 &&
                Float.compare(m10, o.m10) == 0 && Float.compare(m11, o.m11) == 0 && Float.compare(m12, o.m12) == 0 &&
                Float.compare(m20, o.m20) == 0 && Float.compare(m21, o.m21) == 0 && Float.compare(m22, o.m22) == 0;
    }

    @Override
    public String toString() {
        return String.format(
                "[[%f, %f, %f],\n [%f, %f, %f],\n [%f, %f, %f]]",
                m00, m01, m02,
                m10, m11, m12,
                m20, m21, m22
        ).replaceAll(",(?!\\s)", ".");
    }
}
