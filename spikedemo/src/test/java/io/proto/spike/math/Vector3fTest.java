package io.proto.spike.math;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class Vector3fTest {

    @Test
    void testDefaultConstructor() {
        Vector3f vec = new Vector3f();
        assertEquals(0.0f, vec.x);
        assertEquals(0.0f, vec.y);
        assertEquals(0.0f, vec.z);
    }

    @Test
    void testParameterizedConstructor() {
        Vector3f vec = new Vector3f(3.5f, -2.1f, 4.5f);
        assertEquals(3.5f, vec.x);
        assertEquals(-2.1f, vec.y);
        assertEquals(4.5f, vec.z);
    }

    @Test
    void testCopyConstructor() {
        Vector3f original = new Vector3f(1.2f, 3.4f, -5.6f);
        Vector3f copy = new Vector3f(original);
        assertEquals(original, copy);
        assertNotSame(original, copy);
    }

    @Test
    void testAdd() {
        Vector3f v1 = new Vector3f(1.0f, 2.0f, 3.0f);
        Vector3f v2 = new Vector3f(4.0f, 5.0f, 6.0f);
        Vector3f result = v1.add(v2);
        assertEquals(new Vector3f(5.0f, 7.0f, 9.0f), result);
    }

    @Test
    void testSubtract() {
        Vector3f v1 = new Vector3f(5.0f, 7.0f, 9.0f);
        Vector3f v2 = new Vector3f(3.0f, 2.0f, 1.0f);
        Vector3f result = v1.sub(v2);
        assertEquals(new Vector3f(2.0f, 5.0f, 8.0f), result);
    }

    @Test
    void testScale() {
        Vector3f vec = new Vector3f(2.0f, -3.0f, 4.0f);
        Vector3f scaled = vec.scale(2.5f);
        assertEquals(new Vector3f(5.0f, -7.5f, 10.0f), scaled);
    }

    @Test
    void testDotProduct() {
        Vector3f v1 = new Vector3f(1.0f, 2.0f, 3.0f);
        Vector3f v2 = new Vector3f(4.0f, -5.0f, 6.0f);
        float dot = v1.dot(v2);
        assertEquals(12.0f, dot);
    }

    @Test
    void testCrossProduct() {
        Vector3f v1 = new Vector3f(1.0f, 0.0f, 0.0f);
        Vector3f v2 = new Vector3f(0.0f, 1.0f, 0.0f);
        Vector3f cross = v1.cross(v2);
        assertEquals(new Vector3f(0.0f, 0.0f, 1.0f), cross);
    }

    @Test
    void testLength() {
        Vector3f vec = new Vector3f(3.0f, 4.0f, 0.0f);
        assertEquals(5.0f, vec.length());
    }

    @Test
    void testNormalize() {
        Vector3f vec = new Vector3f(3.0f, 4.0f, 0.0f);
        Vector3f normalized = vec.normalize();
        assertEquals(new Vector3f(0.6f, 0.8f, 0.0f), normalized);
    }

    @Test
    void testNormalizeZeroLength() {
        Vector3f vec = new Vector3f(0.0f, 0.0f, 0.0f);
        Vector3f normalized = vec.normalize();
        assertEquals(new Vector3f(0.0f, 0.0f, 0.0f), normalized);
    }

    @Test
    void testNegate() {
        Vector3f vec = new Vector3f(1.0f, -2.0f, 3.0f);
        assertEquals(new Vector3f(-1.0f, 2.0f, -3.0f), vec.negate());
    }

    @Test
    void testDistance() {
        Vector3f v1 = new Vector3f(1.0f, 1.0f, 1.0f);
        Vector3f v2 = new Vector3f(4.0f, 5.0f, 6.0f);
        assertEquals(7.071f, v1.distance(v2), 0.001);
    }

    @Test
    void testEquals() {
        Vector3f v1 = new Vector3f(1.0f, 2.0f, 3.0f);
        Vector3f v2 = new Vector3f(1.0f, 2.0f, 3.0f);
        Vector3f v3 = new Vector3f(1.0f, 2.1f, 3.0f);

        assertEquals(v1, v2);
        assertNotEquals(v1, v3);
        assertNotEquals(v1, null);
        assertNotEquals(v1, "miw");
    }

    @Test
    void testToString() {
        Vector3f vec = new Vector3f(3.0f, -1.0f, 2.0f);
        assertEquals("(3.0, -1.0, 2.0)", vec.toString());
    }
}
