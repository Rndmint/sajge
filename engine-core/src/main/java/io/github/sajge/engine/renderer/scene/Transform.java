package io.github.sajge.engine.renderer.scene;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.sajge.engine.renderer.core.Mat4;
import io.github.sajge.engine.renderer.core.Vec3;
import io.github.sajge.logger.Logger;

public class Transform {
    private static final Logger log = Logger.get(Transform.class);

    private Vec3 position;
    private Vec3 rotation;
    private Vec3 scale;

    public Transform() {
        this.position = new Vec3(0, 0, 0);
        this.rotation = new Vec3(0, 0, 0);
        this.scale = new Vec3(1, 1, 1);
        log.debug("Created default Transform with position={}, rotation={}, scale={}", position, rotation, scale);
    }

    @JsonCreator
    public Transform(
            @JsonProperty("position") Vec3 position,
            @JsonProperty("rotation") Vec3 rotation,
            @JsonProperty("scale") Vec3 scale
    ) {
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
        log.debug("Created Transform with position={}, rotation={}, scale={}", position, rotation, scale);
    }

    @JsonProperty("position")
    public Vec3 getPosition() {
        log.trace("getPosition() => {}", position);
        return position;
    }

    @JsonProperty("position")
    public void setPosition(Vec3 position) {
        log.trace("setPosition({})", position);
        this.position = position;
    }

    @JsonProperty("rotation")
    public Vec3 getRotation() {
        log.trace("getRotation() => {}", rotation);
        return rotation;
    }

    @JsonProperty("rotation")
    public void setRotation(Vec3 rotation) {
        log.trace("setRotation({})", rotation);
        this.rotation = rotation;
    }

    @JsonProperty("scale")
    public Vec3 getScale() {
        log.trace("getScale() => {}", scale);
        return scale;
    }

    @JsonProperty("scale")
    public void setScale(Vec3 scale) {
        log.trace("setScale({})", scale);
        this.scale = scale;
    }

    public Mat4 toMatrix() {
        log.debug("Computing transformation matrix for Transform position={}, rotation={}, scale={}", position, rotation, scale);
        Mat4 matrix = Mat4.translate(position.x, position.y, position.z)
                .mul(Mat4.rotateX(rotation.x))
                .mul(Mat4.rotateY(rotation.y))
                .mul(Mat4.rotateZ(rotation.z))
                .mul(Mat4.scale(scale.x, scale.y, scale.z));
        log.trace("Transformation matrix computed: {}", matrix);
        return matrix;
    }

    public void translate(float dx, float dy, float dz) {
        log.debug("Translating Transform by dx={}, dy={}, dz={}", dx, dy, dz);
        position.x += dx;
        position.y += dy;
        position.z += dz;
        log.trace("New position: {}", position);
    }

    public void rotate(float rx, float ry, float rz) {
        log.debug("Rotating Transform by rx={}, ry={}, rz={}", rx, ry, rz);
        rotation.x += rx;
        rotation.y += ry;
        rotation.z += rz;
        log.trace("New rotation: {}", rotation);
    }

    public void scale(float sx, float sy, float sz) {
        log.debug("Scaling Transform by sx={}, sy={}, sz={}", sx, sy, sz);
        scale.x *= sx;
        scale.y *= sy;
        scale.z *= sz;
        log.trace("New scale: {}", scale);
    }
}
