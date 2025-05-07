package io.proto.spike.renderer.buffer;

import java.util.Arrays;

public class DepthBuffer {
    public float[] depth;
    public int width, height;

    public DepthBuffer(int w, int h) {
        width = w;
        height = h;
        depth = new float[w * h];
        clear();
    }

    public void clear() {
        Arrays.fill(depth, Float.POSITIVE_INFINITY);
    }

    public boolean testAndSet(int x, int y, float z) {
        int i = y * width + x;
        if (z < depth[i]) {
            depth[i] = z;
            return true;
        }
        return false;
    }
}
