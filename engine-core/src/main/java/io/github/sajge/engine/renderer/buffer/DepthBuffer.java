package io.github.sajge.engine.renderer.buffer;

import java.util.Arrays;
import io.github.sajge.logger.Logger;

public class DepthBuffer {
    private static final Logger log = Logger.get(DepthBuffer.class);

    private final float[] depth;
    private final int width;
    private final int height;

    public DepthBuffer(int width, int height) {
        log.info("Initializing DepthBuffer with width={} and height={}", width, height);
        this.width = width;
        this.height = height;
        this.depth = new float[width * height];
    }

    public void clear() {
        log.debug("Clearing depth buffer: filling {} elements with POSITIVE_INFINITY", depth.length);
        Arrays.fill(depth, Float.POSITIVE_INFINITY);
    }

    public boolean testAndSet(int x, int y, float z) {
        int idx = y * width + x;
        log.trace("Testing depth at (x={}, y={}) [index={}] against z={}", x, y, idx, z);

        if (z < depth[idx]) {
            log.debug("Depth test passed (old={}, new={}), updating index={}", depth[idx], z, idx);
            depth[idx] = z;
            return true;
        }

        log.trace("Depth test failed (existing={} <= new={}) at index={}", depth[idx], z, idx);
        return false;
    }
}
