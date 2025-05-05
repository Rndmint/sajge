package io.proto.spike.math;

public class Matrix3x3f {
    public final float[][] data;

    public Matrix3x3f() {
        this.data = new float[3][3];
    }

    public Matrix3x3f(
            float m00, float m01, float m02,
            float m10, float m11, float m12,
            float m20, float m21, float m22
    ) {
        this.data = new float[][]{
                {m00, m01, m02},
                {m10, m11, m12},
                {m20, m21, m22}
        };
    }

    public Matrix3x3f(Matrix3x3f other) {
        this.data = new float[3][3];
        for (int i = 0; i < 3; i++) {
            System.arraycopy(other.data[i], 0, this.data[i], 0, 3);
        }
    }

    public Matrix3x3f setIdentity() {
        Matrix3x3f result = new Matrix3x3f();
        for (int i = 0; i < 3; i++) {
            result.data[i][i] = 1;
        }
        return result;
    }

    public Matrix3x3f add(Matrix3x3f o) {
        Matrix3x3f result = new Matrix3x3f();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                result.data[i][j] = this.data[i][j] + o.data[i][j];
            }
        }
        return result;
    }

    public Matrix3x3f sub(Matrix3x3f o) {
        Matrix3x3f result = new Matrix3x3f();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                result.data[i][j] = this.data[i][j] - o.data[i][j];
            }
        }
        return result;
    }

    public Matrix3x3f scale(float s) {
        Matrix3x3f result = new Matrix3x3f();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                result.data[i][j] = this.data[i][j] * s;
            }
        }
        return result;
    }

    public Matrix3x3f multiply(Matrix3x3f o) {
        Matrix3x3f result = new Matrix3x3f();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                float sum = 0;
                for (int k = 0; k < 3; k++) {
                    sum += this.data[i][k] * o.data[k][j];
                }
                result.data[i][j] = sum;
            }
        }
        return result;
    }

    public Vector3f multiply(Vector3f v) {
        float[] result = new float[3];
        for (int i = 0; i < 3; i++) {
            float sum = 0;
            for (int j = 0; j < 3; j++) {
                sum += this.data[i][j] * v.data[j];
            }
            result[i] = sum;
        }
        return new Vector3f(result[0], result[1], result[2]);
    }

    public float determinant() {
        float a = data[0][0], b = data[0][1], c = data[0][2];
        float d = data[1][0], e = data[1][1], f = data[1][2];
        float g = data[2][0], h = data[2][1], i = data[2][2];
        return a * (e * i - f * h)
                - b * (d * i - f * g)
                + c * (d * h - e * g);
    }

    public Matrix3x3f transpose() {
        Matrix3x3f result = new Matrix3x3f();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                result.data[i][j] = this.data[j][i];
            }
        }
        return result;
    }

    public Matrix3x3f inverse() {
        float det = determinant();
        if (det == 0) return null;
        float[][] m = data;
        float invDet = 1.0f / det;

        Matrix3x3f result = new Matrix3x3f();
        result.data[0][0] = (m[1][1] * m[2][2] - m[1][2] * m[2][1]) * invDet;
        result.data[0][1] = (m[0][2] * m[2][1] - m[0][1] * m[2][2]) * invDet;
        result.data[0][2] = (m[0][1] * m[1][2] - m[0][2] * m[1][1]) * invDet;
        result.data[1][0] = (m[1][2] * m[2][0] - m[1][0] * m[2][2]) * invDet;
        result.data[1][1] = (m[0][0] * m[2][2] - m[0][2] * m[2][0]) * invDet;
        result.data[1][2] = (m[0][2] * m[1][0] - m[0][0] * m[1][2]) * invDet;
        result.data[2][0] = (m[1][0] * m[2][1] - m[1][1] * m[2][0]) * invDet;
        result.data[2][1] = (m[0][1] * m[2][0] - m[0][0] * m[2][1]) * invDet;
        result.data[2][2] = (m[0][0] * m[1][1] - m[0][1] * m[1][0]) * invDet;

        return result;
    }

    @Override
    public String toString() {
        return "[" + data[0][0] + ", " + data[0][1] + ", " + data[0][2] + "; "
                + data[1][0] + ", " + data[1][1] + ", " + data[1][2] + "; "
                + data[2][0] + ", " + data[2][1] + ", " + data[2][2] + "]";
    }
}
