package io.proto.spike.math;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class Vector4fTest {

    @Test
    void testDefaultConstructor() {
        Vector4f vec = new Vector4f();
        assertEquals(0.0f, vec.x);
        assertEquals(0.0f, vec.y);
        assertEquals(0.0f, vec.z);
        assertEquals(0.0f, vec.w);
    }

    @Test
    void testParameterizedConstructor() {
        Vector4f vec = new Vector4f(3.5f, -2.1f, 4.5f, 6.7f);
        assertEquals(3.5f, vec.x);
        assertEquals(-2.1f, vec.y);
        assertEquals(4.5f, vec.z);
        assertEquals(6.7f, vec.w);
    }

    @Test
    void testCopyConstructor() {
        Vector4f original = new Vector4f(1.2f, 3.4f, -5.6f, 7.8f);
        Vector4f copy = new Vector4f(original);
        assertEquals(original, copy);
        assertNotSame(original, copy);
    }

    @Test
    void testAdd() {
        Vector4f v1 = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);
        Vector4f v2 = new Vector4f(4.0f, 5.0f, 6.0f, 7.0f);
        Vector4f result = v1.add(v2);
        assertEquals(new Vector4f(5.0f, 7.0f, 9.0f, 11.0f), result);
    }

    @Test
    void testSubtract() {
        Vector4f v1 = new Vector4f(5.0f, 7.0f, 9.0f, 11.0f);
        Vector4f v2 = new Vector4f(3.0f, 2.0f, 1.0f, 0.0f);
        Vector4f result = v1.sub(v2);
        assertEquals(new Vector4f(2.0f, 5.0f, 8.0f, 11.0f), result);
    }

    @Test
    void testScale() {
        Vector4f vec = new Vector4f(2.0f, -3.0f, 4.0f, -5.0f);
        Vector4f scaled = vec.scale(2.5f);
        assertEquals(new Vector4f(5.0f, -7.5f, 10.0f, -12.5f), scaled);
    }

    @Test
    void testDotProduct() {
        Vector4f v1 = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);
        Vector4f v2 = new Vector4f(4.0f, -5.0f, 6.0f, -7.0f);
        float dot = v1.dot(v2);
        assertEquals(-16.0f, dot);
    }

    @Test
    void testLength() {
        Vector4f vec = new Vector4f(3.0f, 4.0f, 0.0f, 0.0f);
        assertEquals(5.0f, vec.length());
    }

    @Test
    void testNormalize() {
        Vector4f vec = new Vector4f(3.0f, 4.0f, 0.0f, 0.0f);
        Vector4f normalized = vec.normalize();
        assertEquals(new Vector4f(0.6f, 0.8f, 0.0f, 0.0f), normalized);
    }

    @Test
    void testNormalizeZeroLength() {
        Vector4f vec = new Vector4f(0.0f, 0.0f, 0.0f, 0.0f);
        Vector4f normalized = vec.normalize();
        assertEquals(new Vector4f(0.0f, 0.0f, 0.0f, 0.0f), normalized);
    }

    @Test
    void testNegate() {
        Vector4f vec = new Vector4f(1.0f, -2.0f, 3.0f, -4.0f);
        assertEquals(new Vector4f(-1.0f, 2.0f, -3.0f, 4.0f), vec.negate());
    }

    @Test
    void testDistance() {
        Vector4f v1 = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
        Vector4f v2 = new Vector4f(4.0f, 5.0f, 6.0f, 7.0f);
        assertEquals(9.274f, v1.distance(v2), 0.001);
    }

    @Test
    void testEquals() {
        Vector4f v1 = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);
        Vector4f v2 = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);
        Vector4f v3 = new Vector4f(1.0f, 2.1f, 3.0f, 4.0f);

        assertEquals(v1, v2);
        assertNotEquals(v1, v3);
        assertNotEquals(null, v1);
        assertNotEquals("kraht", v1);
    }

    @Test
    void testToString() {
        Vector4f vec = new Vector4f(3.0f, -1.0f, 2.0f, 4.0f);
        assertEquals("(3.0, -1.0, 2.0, 4.0)", vec.toString());
    }
}
