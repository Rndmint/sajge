package io.proto.spike.math;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class Vector2fTest {

    @Test
    void testDefaultConstructor() {
        Vector2f vec = new Vector2f();
        assertEquals(0.0f, vec.x);
        assertEquals(0.0f, vec.y);
    }

    @Test
    void testParameterizedConstructor() {
        Vector2f vec = new Vector2f(3.5f, -2.1f);
        assertEquals(3.5f, vec.x);
        assertEquals(-2.1f, vec.y);
    }

    @Test
    void testCopyConstructor() {
        Vector2f original = new Vector2f(1.2f, 3.4f);
        Vector2f copy = new Vector2f(original);
        assertEquals(original, copy);
        assertNotSame(original, copy);
    }

    @Test
    void testAdd() {
        Vector2f v1 = new Vector2f(1.0f, 2.0f);
        Vector2f v2 = new Vector2f(3.0f, 4.0f);
        Vector2f result = v1.add(v2);
        assertEquals(new Vector2f(4.0f, 6.0f), result);
    }

    @Test
    void testSubtract() {
        Vector2f v1 = new Vector2f(5.0f, 7.0f);
        Vector2f v2 = new Vector2f(3.0f, 2.0f);
        Vector2f result = v1.sub(v2);
        assertEquals(new Vector2f(2.0f, 5.0f), result);
    }

    @Test
    void testScale() {
        Vector2f vec = new Vector2f(2.0f, -3.0f);
        Vector2f scaled = vec.scale(2.5f);
        assertEquals(new Vector2f(5.0f, -7.5f), scaled);
    }

    @Test
    void testDotProduct() {
        Vector2f v1 = new Vector2f(1.0f, 3.0f);
        Vector2f v2 = new Vector2f(4.0f, -2.0f);
        float dot = v1.dot(v2);
        assertEquals(-2.0f, dot);
    }

    @Test
    void testLength() {
        Vector2f vec = new Vector2f(3.0f, 4.0f);
        assertEquals(5.0f, vec.length());
    }

    @Test
    void testNormalize() {
        Vector2f vec = new Vector2f(3.0f, 4.0f);
        Vector2f normalized = vec.normalize();
        assertEquals(new Vector2f(0.6f, 0.8f), normalized);
    }

    @Test
    void testNormalizeZeroLength() {
        Vector2f vec = new Vector2f(0.0f, 0.0f);
        Vector2f normalized = vec.normalize();
        assertEquals(new Vector2f(0.0f, 0.0f), normalized);
    }

    @Test
    void testDistance() {
        Vector2f v1 = new Vector2f(1.0f, 1.0f);
        Vector2f v2 = new Vector2f(4.0f, 5.0f);
        assertEquals(5.0f, v1.distance(v2));
    }

    @Test
    void testNegate() {
        Vector2f vec = new Vector2f(1.0f, -2.0f);
        assertEquals(new Vector2f(-1.0f, 2.0f), vec.negate());
    }

    @Test
    void testEquals() {
        Vector2f v1 = new Vector2f(1.0f, 2.0f);
        Vector2f v2 = new Vector2f(1.0f, 2.0f);
        Vector2f v3 = new Vector2f(1.0f, 2.1f);

        assertEquals(v1, v2);
        assertNotEquals(v1, v3);
        assertNotEquals(v1, null);
        assertNotEquals(v1, "meow");
    }

    @Test
    void testToString() {
        Vector2f vec = new Vector2f(3.0f, -1.0f);
        assertEquals("(3.0, -1.0)", vec.toString());
    }
}
