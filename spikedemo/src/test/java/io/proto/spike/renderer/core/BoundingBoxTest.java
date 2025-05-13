package io.proto.spike.renderer.core;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BoundingBoxTest {

    @Test
    void contains_pointInside_returnsTrue() {
        BoundingBox box = new BoundingBox(new Vec3(0, 0, 0), new Vec3(10, 10, 10));
        Vec3 point = new Vec3(5, 5, 5);
        assertTrue(box.contains(point));
    }

    @Test
    void contains_pointOutside_returnsFalse() {
        BoundingBox box = new BoundingBox(new Vec3(0, 0, 0), new Vec3(10, 10, 10));
        Vec3 point = new Vec3(15, 5, 5);
        assertFalse(box.contains(point));
    }

    @Test
    void contains_pointOnBoundary_returnsTrue() {
        BoundingBox box = new BoundingBox(new Vec3(0, 0, 0), new Vec3(10, 10, 10));
        Vec3 point = new Vec3(10, 5, 5);
        assertTrue(box.contains(point));
    }

    @Test
    void intersects_boxesOverlap_returnsTrue() {
        BoundingBox a = new BoundingBox(new Vec3(0, 0, 0), new Vec3(10, 10, 10));
        BoundingBox b = new BoundingBox(new Vec3(5, 5, 5), new Vec3(15, 15, 15));
        assertTrue(a.intersects(b));
    }

    @Test
    void intersects_boxesSeparate_returnsFalse() {
        BoundingBox a = new BoundingBox(new Vec3(0, 0, 0), new Vec3(10, 10, 10));
        BoundingBox b = new BoundingBox(new Vec3(11, 11, 11), new Vec3(20, 20, 20));
        assertFalse(a.intersects(b));
    }

    @Test
    void intersects_boxesTouch_returnsTrue() {
        BoundingBox a = new BoundingBox(new Vec3(0, 0, 0), new Vec3(10, 10, 10));
        BoundingBox b = new BoundingBox(new Vec3(10, 10, 10), new Vec3(20, 20, 20));
        assertTrue(a.intersects(b));
    }

    @Test
    void intersects_oneInsideAnother_returnsTrue() {
        BoundingBox outer = new BoundingBox(new Vec3(0, 0, 0), new Vec3(10, 10, 10));
        BoundingBox inner = new BoundingBox(new Vec3(2, 2, 2), new Vec3(8, 8, 8));
        assertTrue(outer.intersects(inner));
    }
}
