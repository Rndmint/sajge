package io.proto.spike.renderer.buffer;

public class DepthBuffer {
    public float[] depth;
    public int width, height;

    public DepthBuffer(int width, int height) {
    }

    public void clear() {
    }

    public boolean testAndSet(int x, int y, float z) {
        return false;
    }
}
