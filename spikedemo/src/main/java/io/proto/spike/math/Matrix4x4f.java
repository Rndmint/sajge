package io.proto.spike.math;

public class Matrix4x4f {
    public final float[][] data;

    public Matrix4x4f() {
        this.data = new float[4][4];
    }

    public Matrix4x4f(
            float m00, float m01, float m02, float m03,
            float m10, float m11, float m12, float m13,
            float m20, float m21, float m22, float m23,
            float m30, float m31, float m32, float m33
    ) {
        this.data = new float[][] {
                {m00, m01, m02, m03},
                {m10, m11, m12, m13},
                {m20, m21, m22, m23},
                {m30, m31, m32, m33}
        };
    }

    public Matrix4x4f(Matrix4x4f other) {
        this.data = new float[4][4];
        for (int i = 0; i < 4; i++) {
            System.arraycopy(other.data[i], 0, this.data[i], 0, 4);
        }
    }

    public Matrix4x4f setIdentity() {
        Matrix4x4f result = new Matrix4x4f();
        for (int i = 0; i < 4; i++) {
            result.data[i][i] = 1;
        }
        return result;
    }

    public Matrix4x4f add(Matrix4x4f o) {
        Matrix4x4f result = new Matrix4x4f();
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                result.data[i][j] = this.data[i][j] + o.data[i][j];
        return result;
    }

    public Matrix4x4f sub(Matrix4x4f o) {
        Matrix4x4f result = new Matrix4x4f();
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                result.data[i][j] = this.data[i][j] - o.data[i][j];
        return result;
    }

    public Matrix4x4f scale(float s) {
        Matrix4x4f result = new Matrix4x4f();
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                result.data[i][j] = this.data[i][j] * s;
        return result;
    }

    public Matrix4x4f multiply(Matrix4x4f o) {
        Matrix4x4f result = new Matrix4x4f();
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++) {
                float sum = 0;
                for (int k = 0; k < 4; k++)
                    sum += this.data[i][k] * o.data[k][j];
                result.data[i][j] = sum;
            }
        return result;
    }

    public Vector4f multiply(Vector4f v) {
        float[] result = new float[4];
        for (int i = 0; i < 4; i++) {
            float sum = 0;
            for (int j = 0; j < 4; j++) {
                sum += this.data[i][j] * v.data[j];
            }
            result[i] = sum;
        }
        return new Vector4f(result[0], result[1], result[2], result[3]);
    }

    public Matrix4x4f transpose() {
        Matrix4x4f result = new Matrix4x4f();
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                result.data[i][j] = this.data[j][i];
        return result;
    }

    public Matrix4x4f inverse() {
        float det = determinant();
        if (det == 0) return null;
        return cofactor().transpose().scale(1.0f / det);
    }

    public Matrix4x4f cofactor() {
        Matrix4x4f result = new Matrix4x4f();
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                float sign = ((row + col) % 2 == 0) ? 1 : -1;
                result.data[row][col] = sign * cofactorComponent(row, col);
            }
        }
        return result;
    }

    public float determinant() {
        float det = 0;
        for (int col = 0; col < 4; col++) {
            float sign = (col % 2 == 0) ? 1 : -1;
            det += sign * data[0][col] * cofactorComponent(0, col);
        }
        return det;
    }

    private float cofactorComponent(int row, int col) {
        float[] temp = new float[9];
        int index = 0;
        for (int r = 0; r < 4; r++) {
            if (r == row) continue;
            for (int c = 0; c < 4; c++) {
                if (c == col) continue;
                temp[index++] = data[r][c];
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < 4; i++) {
            if (i > 0) sb.append("; ");
            sb.append(data[i][0]).append(", ").append(data[i][1]).append(", ")
                    .append(data[i][2]).append(", ").append(data[i][3]);
        }
        return sb.append("]").toString();
    }
}
