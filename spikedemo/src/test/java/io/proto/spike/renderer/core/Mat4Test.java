package io.proto.spike.renderer.core;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class Mat4Test {

    @Test
    void identity_createsIdentityMatrix() {
        Mat4 I = Mat4.identity();
        assertEquals(1f, I.m00);
        assertEquals(0f, I.m01);
        assertEquals(0f, I.m02);
        assertEquals(0f, I.m03);
        assertEquals(0f, I.m10);
        assertEquals(1f, I.m11);
        assertEquals(0f, I.m12);
        assertEquals(0f, I.m13);
        assertEquals(0f, I.m20);
        assertEquals(0f, I.m21);
        assertEquals(1f, I.m22);
        assertEquals(0f, I.m23);
        assertEquals(0f, I.m30);
        assertEquals(0f, I.m31);
        assertEquals(0f, I.m32);
        assertEquals(1f, I.m33);
    }

    @Test
    void translate_createsTranslationMatrix() {
        Mat4 T = Mat4.translate(2, 3, 4);
        assertEquals(1f, T.m00);
        assertEquals(0f, T.m01);
        assertEquals(0f, T.m02);
        assertEquals(2f, T.m03);
        assertEquals(0f, T.m10);
        assertEquals(1f, T.m11);
        assertEquals(0f, T.m12);
        assertEquals(3f, T.m13);
        assertEquals(0f, T.m20);
        assertEquals(0f, T.m21);
        assertEquals(1f, T.m22);
        assertEquals(4f, T.m23);
        assertEquals(0f, T.m30);
        assertEquals(0f, T.m31);
        assertEquals(0f, T.m32);
        assertEquals(1f, T.m33);
    }

    @Test
    void rotateX_createsRotationMatrix() {
        Mat4 R = Mat4.rotateX((float)Math.PI/2);
        assertEquals(1f, R.m00);
        assertEquals(0f, R.m01);
        assertEquals(0f, R.m02);
        assertEquals(0f, R.m03);
        assertEquals(0f, R.m10);
        assertEquals(0f, R.m11, 1e-6f);
        assertEquals(-1f, R.m12, 1e-6f);
        assertEquals(0f, R.m13);
        assertEquals(0f, R.m20);
        assertEquals(1f, R.m21, 1e-6f);
        assertEquals(0f, R.m22, 1e-6f);
        assertEquals(0f, R.m23);
        assertEquals(0f, R.m30);
        assertEquals(0f, R.m31);
        assertEquals(0f, R.m32);
        assertEquals(1f, R.m33);
    }

    @Test
    void scale_createsScalingMatrix() {
        Mat4 S = Mat4.scale(2, 3, 4);
        assertEquals(2f, S.m00);
        assertEquals(0f, S.m01);
        assertEquals(0f, S.m02);
        assertEquals(0f, S.m03);
        assertEquals(0f, S.m10);
        assertEquals(3f, S.m11);
        assertEquals(0f, S.m12);
        assertEquals(0f, S.m13);
        assertEquals(0f, S.m20);
        assertEquals(0f, S.m21);
        assertEquals(4f, S.m22);
        assertEquals(0f, S.m23);
        assertEquals(0f, S.m30);
        assertEquals(0f, S.m31);
        assertEquals(0f, S.m32);
        assertEquals(1f, S.m33);
    }

    @Test
    void mul_matricesMultipliesCorrectly() {
        Mat4 a = new Mat4();
        a.m00 = 1; a.m01 = 2; a.m02 = 3; a.m03 = 4;
        a.m10 = 5; a.m11 = 6; a.m12 = 7; a.m13 = 8;
        a.m20 = 9; a.m21 = 10; a.m22 = 11; a.m23 = 12;
        a.m30 = 13; a.m31 = 14; a.m32 = 15; a.m33 = 16;

        Mat4 b = new Mat4();
        b.m00 = 16; b.m01 = 15; b.m02 = 14; b.m03 = 13;
        b.m10 = 12; b.m11 = 11; b.m12 = 10; b.m13 = 9;
        b.m20 = 8; b.m21 = 7; b.m22 = 6; b.m23 = 5;
        b.m30 = 4; b.m31 = 3; b.m32 = 2; b.m33 = 1;

        Mat4 result = a.mul(b);

        assertEquals(80f, result.m00);
        assertEquals(70f, result.m01);
        assertEquals(60f, result.m02);
        assertEquals(50f, result.m03);
        assertEquals(240f, result.m10);
        assertEquals(214f, result.m11);
        assertEquals(188f, result.m12);
        assertEquals(162f, result.m13);
        assertEquals(400f, result.m20);
        assertEquals(358f, result.m21);
        assertEquals(316f, result.m22);
        assertEquals(274f, result.m23);
        assertEquals(560f, result.m30);
        assertEquals(502f, result.m31);
        assertEquals(444f, result.m32);
        assertEquals(386f, result.m33);
    }

    @Test
    void mul_vectorTransformsCorrectly() {
        Mat4 m = new Mat4();
        m.m00 = 1; m.m01 = 2; m.m02 = 3; m.m03 = 4;
        m.m10 = 5; m.m11 = 6; m.m12 = 7; m.m13 = 8;
        m.m20 = 9; m.m21 = 10; m.m22 = 11; m.m23 = 12;
        m.m30 = 13; m.m31 = 14; m.m32 = 15; m.m33 = 16;

        Vec4 v = new Vec4(1, 2, 3, 4);

        Vec4 result = m.mul(v);

        assertEquals(30f, result.x);
        assertEquals(70f, result.y);
        assertEquals(110f, result.z);
        assertEquals(150f, result.w);
    }

    @Test
    void transpose_swapsRowsAndColumns() {
        Mat4 m = new Mat4();
        m.m00 = 1; m.m01 = 2; m.m02 = 3; m.m03 = 4;
        m.m10 = 5; m.m11 = 6; m.m12 = 7; m.m13 = 8;
        m.m20 = 9; m.m21 = 10; m.m22 = 11; m.m23 = 12;
        m.m30 = 13; m.m31 = 14; m.m32 = 15; m.m33 = 16;

        Mat4 result = m.transpose();

        assertEquals(1f, result.m00);
        assertEquals(5f, result.m01);
        assertEquals(9f, result.m02);
        assertEquals(13f, result.m03);
        assertEquals(2f, result.m10);
        assertEquals(6f, result.m11);
        assertEquals(10f, result.m12);
        assertEquals(14f, result.m13);
        assertEquals(3f, result.m20);
        assertEquals(7f, result.m21);
        assertEquals(11f, result.m22);
        assertEquals(15f, result.m23);
        assertEquals(4f, result.m30);
        assertEquals(8f, result.m31);
        assertEquals(12f, result.m32);
        assertEquals(16f, result.m33);
    }

    @Test
    void invert_returnsCorrectInverse() {
        Mat4 m = new Mat4();
        m.m00 = 1; m.m01 = 0; m.m02 = 0; m.m03 = 1;
        m.m10 = 0; m.m11 = 2; m.m12 = 1; m.m13 = 2;
        m.m20 = 2; m.m21 = 1; m.m22 = 0; m.m23 = 1;
        m.m30 = 2; m.m31 = 0; m.m32 = 1; m.m33 = 4;

        Mat4 result = m.invert();

        assertEquals(-2f, result.m00);
        assertEquals(-0.5f, result.m01);
        assertEquals(1f, result.m02);
        assertEquals(0.5f, result.m03);
        assertEquals(1f, result.m10);
        assertEquals(0.5f, result.m11);
        assertEquals(0f, result.m12);
        assertEquals(-0.5f, result.m13);
        assertEquals(-8f, result.m20);
        assertEquals(-1f, result.m21);
        assertEquals(2f, result.m22);
        assertEquals(2f, result.m23);
        assertEquals(3f, result.m30);
        assertEquals(0.5f, result.m31);
        assertEquals(-1f, result.m32);
        assertEquals(-0.5f, result.m33);
    }

    @Test
    void invert_singularMatrixThrowsException() {
        Mat4 m = new Mat4();
        m.m00 = 1; m.m01 = 2; m.m02 = 3; m.m03 = 4;
        m.m10 = 5; m.m11 = 6; m.m12 = 7; m.m13 = 8;
        m.m20 = 9; m.m21 = 10; m.m22 = 11; m.m23 = 12;
        m.m30 = 13; m.m31 = 14; m.m32 = 15; m.m33 = 16;

        assertThrows(ArithmeticException.class, m::invert);
    }

    @Test
    void perspective_createsProjectionMatrix() {
        Mat4 P = Mat4.perspective((float)Math.PI/2, 2.0f, 0.1f, 100f);
        assertEquals(0.5f, P.m00, 1e-6f);
        assertEquals(0f, P.m01);
        assertEquals(0f, P.m02);
        assertEquals(0f, P.m03);
        assertEquals(0f, P.m10);
        assertEquals(1f, P.m11, 1e-6f);
        assertEquals(0f, P.m12);
        assertEquals(0f, P.m13);
        assertEquals(0f, P.m20);
        assertEquals(0f, P.m21);
        assertEquals(-1.002f, P.m22, 1e-3f);
        assertEquals(-0.2002f, P.m23, 1e-6f);
        assertEquals(0f, P.m30);
        assertEquals(0f, P.m31);
        assertEquals(-1f, P.m32);
        assertEquals(0f, P.m33);
    }
}
