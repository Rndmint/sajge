package io.proto.spike.renderer.util;

public class MathUtils {
    public static float edge(int x0, int y0, int x1, int y1, int x, int y) {
        return (x - x0) * (y1 - y0) - (y - y0) * (x1 - x0);
    }

    public static float[] barycentric(int x0, int y0, int x1, int y1,
                                      int x2, int y2, int x, int y) {
        float area = edge(x0, y0, x1, y1, x2, y2);
        if (area == 0) return null;

        return new float[] {
                edge(x1, y1, x2, y2, x, y) / area,
                edge(x2, y2, x0, y0, x, y) / area,
                edge(x0, y0, x1, y1, x, y) / area
        };
    }

    public static float lerp(float a, float b, float t) {
        return a * (1 - t) + b * t;
    }
}
