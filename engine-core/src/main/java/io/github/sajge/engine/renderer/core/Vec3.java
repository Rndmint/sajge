package io.github.sajge.engine.renderer.core;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.sajge.logger.Logger;

import java.util.List;

@JsonSerialize(using = Vec3Serializer.class)
@JsonFormat(shape = JsonFormat.Shape.ARRAY)
public class Vec3 {
    private static final Logger log = Logger.get(Vec3.class);

    public float x;
    public float y;
    public float z;

    @JsonCreator
    public Vec3(List<Number> coords) {
        this(
                coords.size() > 0 ? coords.get(0).floatValue() : 0f,
                coords.size() > 1 ? coords.get(1).floatValue() : 0f,
                coords.size() > 2 ? coords.get(2).floatValue() : 0f
        );
        log.debug("Deserialized Vec3 from array {} → ({}, {}, {})", coords, x, y, z);
    }

    public Vec3() {
        this(0, 0, 0);
        log.debug("Created Vec3 default (0,0,0)");
    }

    public Vec3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        log.debug("Created Vec3 with values x={}, y={}, z={}", x, y, z);
    }

    public Vec3 add(Vec3 o) {
        log.trace("Adding Vec3 {} to {}", o, this);
        this.x += o.x;
        this.y += o.y;
        this.z += o.z;
        log.debug("Result after add: {}", this);
        return this;
    }

    public Vec3 cross(Vec3 o) {
        log.trace("Computing cross product of {} and {}", this, o);
        float cx = this.y * o.z - this.z * o.y;
        float cy = this.z * o.x - this.x * o.z;
        float cz = this.x * o.y - this.y * o.x;
        this.x = cx;
        this.y = cy;
        this.z = cz;
        log.debug("Result after cross: {}", this);
        return this;
    }

    @Override
    public String toString() {
        return "Vec3(" + x + ", " + y + ", " + z + ")";
    }
}
