package io.github.sajge.engine.renderer.core;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.sajge.logger.Logger;

@JsonSerialize(using = Vec3Serializer.class)
@JsonFormat(shape = JsonFormat.Shape.ARRAY)
public class Vec4 {
    private static final Logger log = Logger.get(Vec4.class);

    public float x;
    public float y;
    public float z;
    public float w;

    public Vec4() {
        this(0, 0, 0, 0);
        log.debug("Created Vec4 default (0, 0, 0, 0)");
    }

    public Vec4(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
        log.debug("Created Vec4 x={}, y={}, z={}, w={}", x, y, z, w);
    }

    public float dot(Vec4 o) {
        log.trace("Calculating dot product of {} and {}", this, o);
        float result = x * o.x + y * o.y + z * o.z + w * o.w;
        log.debug("Dot product result = {}", result);
        return result;
    }

    @Override
    public String toString() {
        return "Vec4(" + x + ", " + y + ", " + z + ", " + w + ")";
    }
}
