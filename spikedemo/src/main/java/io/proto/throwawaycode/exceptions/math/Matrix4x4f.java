package io.proto.throwawaycode.exceptions.math;

import io.proto.throwawaycode.exceptions.SingularMatrixException;

import java.util.Arrays;

public class Matrix4x4f {
    public float[][] data;

    public Matrix4x4f() {
        this.data = new float[4][4];
    }

    public Matrix4x4f(float[][] array) {
        if (array == null || array.length != 4 || array[0].length != 4
                || array[1].length != 4 || array[2].length != 4 || array[3].length != 4) {
            throw new IllegalArgumentException("Array must be 4x4");
        }
        this.data = new float[4][4];
        for (int i = 0; i < 4; i++) {
            System.arraycopy(array[i], 0, this.data[i], 0, 4);
        }
    }

    public Matrix4x4f(
            float m00, float m01, float m02, float m03,
            float m10, float m11, float m12, float m13,
            float m20, float m21, float m22, float m23,
            float m30, float m31, float m32, float m33
    ) {
        this.data = new float[][]{
                {m00, m01, m02, m03},
                {m10, m11, m12, m13},
                {m20, m21, m22, m23},
                {m30, m31, m32, m33}
        };
    }

    public Matrix4x4f(Matrix4x4f other) {
        this(other.data);
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
        if (det == 0f) throw new SingularMatrixException("Matrix4x4f is not invertible: " + this);
        return cofactor().transpose().scale(1.0f / det);
    }

    public Matrix4x4f cofactor() {
        Matrix4x4f result = new Matrix4x4f();
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                float sign = ((row + col) % 2 == 0) ? 1f : -1f;
                result.data[row][col] = sign * cofactorComponent(row, col);
            }
        }
        return result;
    }

    public float determinant() {
        float det = 0f;
        for (int col = 0; col < 4; col++) {
            float sign = (col % 2 == 0) ? 1f : -1f;
            det += sign * data[0][col] * cofactorComponent(0, col);
        }
        return det;
    }

    private float cofactorComponent(int row, int col) {
        float[][] minor = new float[3][3];
        int mi = 0;
        for (int r = 0; r < 4; r++) {
            if (r == row) continue;
            int mj = 0;
            for (int c = 0; c < 4; c++) {
                if (c == col) continue;
                minor[mi][mj++] = data[r][c];
            }
            mi++;
        }
        return new Matrix3x3f(minor).determinant();
    }

    @Override
    public String toString() {
        return Arrays.deepToString(data);
    }

    public static Matrix4x4f identity() {
        return new Matrix4x4f(
                1,0,0,0,
                0,1,0,0,
                0,0,1,0,
                0,0,0,1);
    }
}
