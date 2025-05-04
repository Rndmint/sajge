package io.proto.spike.math;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class Matrix3x3fTest {

    @Test
    void testDefaultConstructor() {
        Matrix3x3f matrix = new Matrix3x3f();
        assertEquals(0.0f, matrix.m00);
        assertEquals(0.0f, matrix.m01);
        assertEquals(0.0f, matrix.m02);
        assertEquals(0.0f, matrix.m10);
        assertEquals(0.0f, matrix.m11);
        assertEquals(0.0f, matrix.m12);
        assertEquals(0.0f, matrix.m20);
        assertEquals(0.0f, matrix.m21);
        assertEquals(0.0f, matrix.m22);
    }

    @Test
    void testParameterizedConstructor() {
        Matrix3x3f matrix = new Matrix3x3f(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
        assertEquals(1.0f, matrix.m00);
        assertEquals(2.0f, matrix.m01);
        assertEquals(3.0f, matrix.m02);
        assertEquals(4.0f, matrix.m10);
        assertEquals(5.0f, matrix.m11);
        assertEquals(6.0f, matrix.m12);
        assertEquals(7.0f, matrix.m20);
        assertEquals(8.0f, matrix.m21);
        assertEquals(9.0f, matrix.m22);
    }

    @Test
    void testSetIdentity() {
        Matrix3x3f matrix = new Matrix3x3f();
        matrix.setIdentity();
        assertEquals(1.0f, matrix.m00);
        assertEquals(1.0f, matrix.m11);
        assertEquals(1.0f, matrix.m22);
        assertEquals(0.0f, matrix.m01);
        assertEquals(0.0f, matrix.m02);
        assertEquals(0.0f, matrix.m10);
        assertEquals(0.0f, matrix.m12);
        assertEquals(0.0f, matrix.m20);
        assertEquals(0.0f, matrix.m21);
    }

    @Test
    void testAdd() {
        Matrix3x3f m1 = new Matrix3x3f(1, 2, 3, 4, 5, 6, 7, 8, 9);
        Matrix3x3f m2 = new Matrix3x3f(9, 8, 7, 6, 5, 4, 3, 2, 1);
        Matrix3x3f result = m1.add(m2);
        assertEquals(new Matrix3x3f(10, 10, 10, 10, 10, 10, 10, 10, 10), result);
    }

    @Test
    void testSubtract() {
        Matrix3x3f m1 = new Matrix3x3f(5, 6, 7, 8, 9, 10, 11, 12, 13);
        Matrix3x3f m2 = new Matrix3x3f(1, 2, 3, 4, 5, 6, 7, 8, 9);
        Matrix3x3f result = m1.sub(m2);
        assertEquals(new Matrix3x3f(4, 4, 4, 4, 4, 4, 4, 4, 4), result);
    }

    @Test
    void testScale() {
        Matrix3x3f matrix = new Matrix3x3f(1, 2, 3, 4, 5, 6, 7, 8, 9);
        Matrix3x3f scaledMatrix = matrix.scale(2.0f);
        assertEquals(new Matrix3x3f(2, 4, 6, 8, 10, 12, 14, 16, 18), scaledMatrix);
    }

    @Test
    void testMultiply() {
        Matrix3x3f m1 = new Matrix3x3f(1, 2, 3, 4, 5, 6, 7, 8, 9);
        Matrix3x3f m2 = new Matrix3x3f(9, 8, 7, 6, 5, 4, 3, 2, 1);
        Matrix3x3f result = m1.multiply(m2);
        assertEquals(new Matrix3x3f(30, 24, 18, 84, 69, 54, 138, 114, 90), result);
    }

    @Test
    void testMultiplyWithVector() {
        Matrix3x3f matrix = new Matrix3x3f(1, 2, 3, 4, 5, 6, 7, 8, 9);
        Vector3f vector = new Vector3f(1, 2, 3);
        Vector3f result = matrix.multiply(vector);
        assertEquals(new Vector3f(14, 32, 50), result);
    }

    @Test
    void testDeterminant() {
        Matrix3x3f matrix = new Matrix3x3f(1, 2, 3, 4, 5, 6, 7, 8, 9);
        assertEquals(0.0f, matrix.determinant());
    }

    @Test
    void testTranspose() {
        Matrix3x3f matrix = new Matrix3x3f(1, 2, 3, 4, 5, 6, 7, 8, 9);
        Matrix3x3f transposed = matrix.transpose();
        assertEquals(new Matrix3x3f(1, 4, 7, 2, 5, 8, 3, 6, 9), transposed);
    }

    @Test
    void testInverse() {
        Matrix3x3f matrix = new Matrix3x3f(1, 2, 3, 0, 1, 4, 5, 6, 0);
        Matrix3x3f inverse = matrix.inverse();
        assertEquals(new Matrix3x3f(-24f, 18f, 5f, 20f, -15f, -4f, -5f, 4f, 1f), inverse);
    }

    @Test
    void testInverseSingular() {
        Matrix3x3f matrix = new Matrix3x3f(1, 2, 3, 4, 5, 6, 7, 8, 9);
        assertNull(matrix.inverse());
    }

    @Test
    void testEquals() {
        Matrix3x3f m1 = new Matrix3x3f(1, 2, 3, 4, 5, 6, 7, 8, 9);
        Matrix3x3f m2 = new Matrix3x3f(1, 2, 3, 4, 5, 6, 7, 8, 9);
        Matrix3x3f m3 = new Matrix3x3f(9, 8, 7, 6, 5, 4, 3, 2, 1);

        assertEquals(m1, m2);
        assertNotEquals(m1, m3);
        assertNotEquals(m1, null);
        assertNotEquals(m1, "some string");
    }

    @Test
    void testToString() {
        Matrix3x3f matrix = new Matrix3x3f(1, 2, 3, 4, 5, 6, 7, 8, 9);
        assertEquals("[[1.000000, 2.000000, 3.000000],\n [4.000000, 5.000000, 6.000000],\n [7.000000, 8.000000, 9.000000]]", matrix.toString());
    }
}
