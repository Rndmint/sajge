package io.proto.spike.renderer.core;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class Vec3Test {

    @Test
    void defaultConstructor_createsZeroVector() {
        Vec3 v = new Vec3();
        assertEquals(0f, v.x);
        assertEquals(0f, v.y);
        assertEquals(0f, v.z);
    }

    @Test
    void parameterizedConstructor_setsComponents() {
        Vec3 v = new Vec3(1.5f, 2.5f, 3.5f);
        assertEquals(1.5f, v.x);
        assertEquals(2.5f, v.y);
        assertEquals(3.5f, v.z);
    }

    @Test
    void add_modifiesVectorInPlace() {
        Vec3 a = new Vec3(1, 2, 3);
        Vec3 b = new Vec3(4, 5, 6);

        a.add(b);

        assertEquals(5f, a.x);
        assertEquals(7f, a.y);
        assertEquals(9f, a.z);
    }

    @Test
    void sub_modifiesVectorInPlace() {
        Vec3 a = new Vec3(5, 7, 9);
        Vec3 b = new Vec3(4, 5, 6);

        a.sub(b);

        assertEquals(1f, a.x);
        assertEquals(2f, a.y);
        assertEquals(3f, a.z);
    }

    @Test
    void cross_returnsCorrectCrossProduct() {
        Vec3 a = new Vec3(1, 0, 0);
        Vec3 b = new Vec3(0, 1, 0);

        a.cross(b);

        assertEquals(0f, a.x);
        assertEquals(0f, a.y);
        assertEquals(1f, a.z);
    }

    @Test
    void dot_returnsCorrectDotProduct() {
        Vec3 a = new Vec3(1, 2, 3);
        Vec3 b = new Vec3(4, 5, 6);

        float result = a.dot(b);

        assertEquals(32f, result);
    }

    @Test
    void normalize_handlesZeroVector() {
        Vec3 v = new Vec3(0, 0, 0);
        v.normalize();
        assertEquals(0f, v.x);
        assertEquals(0f, v.y);
        assertEquals(0f, v.z);
    }

    @Test
    void normalize_createsUnitVector() {
        Vec3 v = new Vec3(1, 2, 3);
        v.normalize();

        float length = (float) Math.sqrt(v.x * v.x + v.y * v.y + v.z * v.z);
        assertEquals(1f, length, 1e-6f);
    }

    @Test
    void instanceLerp_interpolatesCorrectly() {
        Vec3 a = new Vec3(1, 2, 3);
        Vec3 b = new Vec3(4, 5, 6);

        a.lerp(b, 0.5f);

        assertEquals(2.5f, a.x);
        assertEquals(3.5f, a.y);
        assertEquals(4.5f, a.z);
    }

    @Test
    void staticLerp_createsNewVector() {
        Vec3 a = new Vec3(1, 2, 3);
        Vec3 b = new Vec3(4, 5, 6);

        Vec3 result = Vec3.lerp(a, b, 0.5f);

        assertEquals(2.5f, result.x);
        assertEquals(3.5f, result.y);
        assertEquals(4.5f, result.z);
        assertEquals(1f, a.x);
    }
}
