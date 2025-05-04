package io.proto.spike.math;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class Matrix4x4fTest {

    @Test
    void testDefaultConstructor() {
        Matrix4x4f matrix = new Matrix4x4f();
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                assertEquals(0.0f, matrix.get(row, col));
            }
        }
    }

    @Test
    void testParameterizedConstructor() {
        Matrix4x4f matrix = new Matrix4x4f(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);
        assertEquals(1.0f, matrix.m00);
        assertEquals(2.0f, matrix.m01);
        assertEquals(3.0f, matrix.m02);
        assertEquals(4.0f, matrix.m03);
        assertEquals(5.0f, matrix.m10);
        assertEquals(6.0f, matrix.m11);
        assertEquals(7.0f, matrix.m12);
        assertEquals(8.0f, matrix.m13);
        assertEquals(9.0f, matrix.m20);
        assertEquals(10.0f, matrix.m21);
        assertEquals(11.0f, matrix.m22);
        assertEquals(12.0f, matrix.m23);
        assertEquals(13.0f, matrix.m30);
        assertEquals(14.0f, matrix.m31);
        assertEquals(15.0f, matrix.m32);
        assertEquals(16.0f, matrix.m33);
    }

    @Test
    void testSetIdentity() {
        Matrix4x4f matrix = new Matrix4x4f();
        matrix.setIdentity();
        assertEquals(1.0f, matrix.m00);
        assertEquals(1.0f, matrix.m11);
        assertEquals(1.0f, matrix.m22);
        assertEquals(1.0f, matrix.m33);
        assertEquals(0.0f, matrix.m01);
        assertEquals(0.0f, matrix.m02);
        assertEquals(0.0f, matrix.m03);
        assertEquals(0.0f, matrix.m10);
        assertEquals(0.0f, matrix.m12);
        assertEquals(0.0f, matrix.m13);
        assertEquals(0.0f, matrix.m20);
        assertEquals(0.0f, matrix.m21);
        assertEquals(0.0f, matrix.m23);
        assertEquals(0.0f, matrix.m30);
        assertEquals(0.0f, matrix.m31);
        assertEquals(0.0f, matrix.m32);
    }

    @Test
    void testAdd() {
        Matrix4x4f m1 = new Matrix4x4f(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);
        Matrix4x4f m2 = new Matrix4x4f(16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1);
        Matrix4x4f result = m1.add(m2);
        assertEquals(new Matrix4x4f(17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17), result);
    }

    @Test
    void testSubtract() {
        Matrix4x4f m1 = new Matrix4x4f(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);
        Matrix4x4f m2 = new Matrix4x4f(16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1);
        Matrix4x4f result = m1.sub(m2);
        assertEquals(new Matrix4x4f(-15, -13, -11, -9, -7, -5, -3, -1, 1, 3, 5, 7, 9, 11, 13, 15), result);
    }

    @Test
    void testScale() {
        Matrix4x4f matrix = new Matrix4x4f(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);
        Matrix4x4f scaledMatrix = matrix.scale(2.0f);
        assertEquals(new Matrix4x4f(2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32), scaledMatrix);
    }

    @Test
    void testMultiply() {
        Matrix4x4f m1 = new Matrix4x4f(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);
        Matrix4x4f m2 = new Matrix4x4f(16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1);
        Matrix4x4f result = m1.multiply(m2);
        assertEquals(new Matrix4x4f(80, 70, 60, 50, 240, 214, 188, 162, 400, 358, 316, 274, 560, 502, 444, 386), result);
    }

    @Test
    void testMultiplyWithVector() {
        Matrix4x4f matrix = new Matrix4x4f(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);
        Vector4f vector = new Vector4f(1, 2, 3, 4);
        Vector4f result = matrix.multiply(vector);
        assertEquals(new Vector4f(30, 70, 110, 150), result);
    }

    @Test
    void testTranspose() {
        Matrix4x4f matrix = new Matrix4x4f(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);
        Matrix4x4f transposed = matrix.transpose();
        assertEquals(new Matrix4x4f(1, 5, 9, 13, 2, 6, 10, 14, 3, 7, 11, 15, 4, 8, 12, 16), transposed);
    }

    @Test
    void testDeterminant() {
        Matrix4x4f matrix = new Matrix4x4f(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);
        assertEquals(0.0f, matrix.determinant());
    }

    @Test
    void testInverse() {
        Matrix4x4f matrix = new Matrix4x4f(1, 2, 3, 4, 0, 5, 6, 7, 0, 0, 8, 9, 0, 0, 0, 10);
        Matrix4x4f inverse = matrix.inverse();
        assertNotNull(inverse);
    }

    @Test
    void testInverseSingular() {
        Matrix4x4f matrix = new Matrix4x4f(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);
        assertNull(matrix.inverse());
    }

    @Test
    void testEquals() {
        Matrix4x4f m1 = new Matrix4x4f(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);
        Matrix4x4f m2 = new Matrix4x4f(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);
        Matrix4x4f m3 = new Matrix4x4f(16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1);

        assertEquals(m1, m2);
        assertNotEquals(m1, m3);
        assertNotEquals(m1, null);
        assertNotEquals(m1, "some string");
    }

    @Test
    void testToString() {
        Matrix4x4f matrix = new Matrix4x4f(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);
        assertEquals("[[1.000000, 2.000000, 3.000000, 4.000000],\n [5.000000, 6.000000, 7.000000, 8.000000],\n [9.000000, 10.000000, 11.000000, 12.000000],\n [13.000000, 14.000000, 15.000000, 16.000000]]", matrix.toString());
    }
}
