package io.proto.spike.renderer.support;

public class ScreenVertex {
    public int x, y;
    public float invW, zOverW;

    public ScreenVertex(int x, int y, float invW, float zOverW) {
        this.x      = x;
        this.y      = y;
        this.invW   = invW;
        this.zOverW = zOverW;
    }
}
