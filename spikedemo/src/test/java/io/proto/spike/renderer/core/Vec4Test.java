package io.proto.spike.renderer.core;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class Vec4Test {

    @Test
    void defaultConstructor_createsZeroVector() {
        Vec4 v = new Vec4();
        assertEquals(0f, v.x);
        assertEquals(0f, v.y);
        assertEquals(0f, v.z);
        assertEquals(0f, v.w);
    }

    @Test
    void parameterizedConstructor_setsComponents() {
        Vec4 v = new Vec4(1.5f, 2.5f, 3.5f, 4.5f);
        assertEquals(1.5f, v.x);
        assertEquals(2.5f, v.y);
        assertEquals(3.5f, v.z);
        assertEquals(4.5f, v.w);
    }

    @Test
    void add_modifiesVectorInPlace() {
        Vec4 a = new Vec4(1, 2, 3, 4);
        Vec4 b = new Vec4(5, 6, 7, 8);

        a.add(b);

        assertEquals(6f, a.x);
        assertEquals(8f, a.y);
        assertEquals(10f, a.z);
        assertEquals(12f, a.w);
    }

    @Test
    void sub_modifiesVectorInPlace() {
        Vec4 a = new Vec4(5, 6, 7, 8);
        Vec4 b = new Vec4(1, 2, 3, 4);

        a.sub(b);

        assertEquals(4f, a.x);
        assertEquals(4f, a.y);
        assertEquals(4f, a.z);
        assertEquals(4f, a.w);
    }

    @Test
    void dot_returnsCorrectDotProduct() {
        Vec4 a = new Vec4(1, 2, 3, 4);
        Vec4 b = new Vec4(5, 6, 7, 8);

        float result = a.dot(b);

        assertEquals(70f, result);
    }
}
