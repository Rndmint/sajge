package io.proto.spike.renderer.util;

import io.proto.spike.renderer.core.Plane;
import io.proto.spike.renderer.core.Vec4;

public class ClippingUtils {
    public static Vec4 intersectPlane(Vec4 a, Vec4 b, Plane plane) {
        return plane.intersect(a, b);
    }
}
