package io.proto.spike.renderer.core;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class Mat3Test {

    @Test
    void identity_createsIdentityMatrix() {
        Mat3 I = Mat3.identity();
        assertEquals(1f, I.m00);
        assertEquals(0f, I.m01);
        assertEquals(0f, I.m02);
        assertEquals(0f, I.m10);
        assertEquals(1f, I.m11);
        assertEquals(0f, I.m12);
        assertEquals(0f, I.m20);
        assertEquals(0f, I.m21);
        assertEquals(1f, I.m22);
    }

    @Test
    void mul_matricesMultipliesCorrectly() {
        Mat3 a = new Mat3();
        a.m00 = 1; a.m01 = 2; a.m02 = 3;
        a.m10 = 4; a.m11 = 5; a.m12 = 6;
        a.m20 = 7; a.m21 = 8; a.m22 = 9;

        Mat3 b = new Mat3();
        b.m00 = 9; b.m01 = 8; b.m02 = 7;
        b.m10 = 6; b.m11 = 5; b.m12 = 4;
        b.m20 = 3; b.m21 = 2; b.m22 = 1;

        Mat3 result = a.mul(b);

        assertEquals(30f, result.m00);
        assertEquals(24f, result.m01);
        assertEquals(18f, result.m02);
        assertEquals(84f, result.m10);
        assertEquals(69f, result.m11);
        assertEquals(54f, result.m12);
        assertEquals(138f, result.m20);
        assertEquals(114f, result.m21);
        assertEquals(90f, result.m22);
    }

    @Test
    void mul_vectorTransformsCorrectly() {
        Mat3 m = new Mat3();
        m.m00 = 1; m.m01 = 2; m.m02 = 3;
        m.m10 = 4; m.m11 = 5; m.m12 = 6;
        m.m20 = 7; m.m21 = 8; m.m22 = 9;

        Vec3 v = new Vec3(1, 2, 3);

        Vec3 result = m.mul(v);

        assertEquals(14f, result.x);
        assertEquals(32f, result.y);
        assertEquals(50f, result.z);
    }

    @Test
    void transpose_swapsRowsAndColumns() {
        Mat3 m = new Mat3();
        m.m00 = 1; m.m01 = 2; m.m02 = 3;
        m.m10 = 4; m.m11 = 5; m.m12 = 6;
        m.m20 = 7; m.m21 = 8; m.m22 = 9;

        Mat3 result = m.transpose();

        assertEquals(1f, result.m00);
        assertEquals(4f, result.m01);
        assertEquals(7f, result.m02);
        assertEquals(2f, result.m10);
        assertEquals(5f, result.m11);
        assertEquals(8f, result.m12);
        assertEquals(3f, result.m20);
        assertEquals(6f, result.m21);
        assertEquals(9f, result.m22);
    }

    @Test
    void invert_returnsCorrectInverse() {
        Mat3 m = new Mat3();
        m.m00 = 1; m.m01 = 2; m.m02 = 3;
        m.m10 = 0; m.m11 = 1; m.m12 = 4;
        m.m20 = 5; m.m21 = 6; m.m22 = 0;

        Mat3 result = m.invert();

        assertEquals(-24f, result.m00);
        assertEquals(18f, result.m01);
        assertEquals(5f, result.m02);
        assertEquals(20f, result.m10);
        assertEquals(-15f, result.m11);
        assertEquals(-4f, result.m12);
        assertEquals(-5f, result.m20);
        assertEquals(4f, result.m21);
        assertEquals(1f, result.m22);
    }

    @Test
    void invert_singularMatrixThrowsException() {
        Mat3 m = new Mat3();
        m.m00 = 1; m.m01 = 2; m.m02 = 3;
        m.m10 = 4; m.m11 = 5; m.m12 = 6;
        m.m20 = 7; m.m21 = 8; m.m22 = 9;

        assertThrows(ArithmeticException.class, m::invert);
    }
}
