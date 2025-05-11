package io.github.sajge.engine.renderer.util;

import io.github.sajge.logger.Logger;

public class MathUtils {
    private static final Logger log = Logger.get(MathUtils.class);

    public static float edge(
            int x0, int y0,
            int x1, int y1,
            int x, int y
    ) {
        float result = (x - x0) * (y1 - y0) - (y - y0) * (x1 - x0);
        log.trace("edge({}, {}, {}, {}, {}, {}) = {}", x0, y0, x1, y1, x, y, result);
        return result;
    }
}
