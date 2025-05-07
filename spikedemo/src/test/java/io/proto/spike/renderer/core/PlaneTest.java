package io.proto.spike.renderer.core;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlaneTest {

    @Test
    void distance_returnsCorrectSignedDistance() {
        Plane p = new Plane(0, 1, 0, -5); // Plane y=5
        Vec4 point = new Vec4(3, 10, 2, 1);

        float dist = p.distance(point);

        assertEquals(5f, dist);
    }

    @Test
    void distance_pointOnPlane_returnsZero() {
        Plane p = new Plane(1, 0, 0, -3); // Plane x=3
        Vec4 point = new Vec4(3, 5, 7, 1);

        float dist = p.distance(point);

        assertEquals(0f, dist);
    }

    @Test
    void intersect_returnsCorrectIntersectionPoint() {
        Plane p = new Plane(0, 1, 0, -5); // Plane y=5
        Vec4 a = new Vec4(1, 10, 2, 1);
        Vec4 b = new Vec4(1, 0, 2, 1);

        Vec4 intersection = p.intersect(a, b);

        assertEquals(1f, intersection.x);
        assertEquals(5f, intersection.y);
        assertEquals(2f, intersection.z);
        assertEquals(1f, intersection.w);
    }

    @Test
    void intersect_pointsOnSameSide_returnsValidPoint() {
        Plane p = new Plane(0, 0, 1, -10); // Plane z=10
        Vec4 a = new Vec4(0, 0, 5, 1);
        Vec4 b = new Vec4(0, 0, 15, 1);

        Vec4 intersection = p.intersect(a, b);

        assertEquals(0f, intersection.x);
        assertEquals(0f, intersection.y);
        assertEquals(10f, intersection.z);
        assertEquals(1f, intersection.w);
    }

    @Test
    void intersect_pointsOnOppositeSides_returnsValidPoint() {
        Plane p = new Plane(1, 0, 0, -2); // Plane x=2
        Vec4 a = new Vec4(0, 0, 0, 1);
        Vec4 b = new Vec4(4, 0, 0, 1);

        Vec4 intersection = p.intersect(a, b);

        assertEquals(2f, intersection.x);
        assertEquals(0f, intersection.y);
        assertEquals(0f, intersection.z);
        assertEquals(1f, intersection.w);
    }

    @Test
    void intersect_pointOnPlane_returnsThatPoint() {
        Plane p = new Plane(0, 1, 0, -3); // Plane y=3
        Vec4 a = new Vec4(1, 3, 1, 1);
        Vec4 b = new Vec4(2, 4, 2, 1);

        Vec4 intersection = p.intersect(a, b);

        assertEquals(1f, intersection.x);
        assertEquals(3f, intersection.y);
        assertEquals(1f, intersection.z);
        assertEquals(1f, intersection.w);
    }
}
