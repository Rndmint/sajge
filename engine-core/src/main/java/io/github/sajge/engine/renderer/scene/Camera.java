package io.github.sajge.engine.renderer.scene;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.sajge.engine.renderer.core.Mat4;
import io.github.sajge.logger.Logger;

public class Camera {
    private static final Logger log = Logger.get(Camera.class);

    private int id;
    private Transform transform;
    private float fovY;
    private float aspect;
    private float near;
    private float far;

    public Camera() {
        log.debug("Created default Camera");
    }

    @JsonCreator
    public Camera(
            @JsonProperty("id") int id,
            @JsonProperty("transform") Transform transform,
            @JsonProperty("fovY") float fovY,
            @JsonProperty("aspect") float aspect,
            @JsonProperty("near") float near,
            @JsonProperty("far") float far
    ) {
        log.debug("Creating Camera with id={}, fovY={}, aspect={}, near={}, far={}", id, fovY, aspect, near, far);
        this.id = id;
        this.transform = transform;
        this.fovY = fovY;
        this.aspect = aspect;
        this.near = near;
        this.far = far;
    }

    @JsonProperty("id")
    public int getId() {
        log.trace("getId() => {}", id);
        return id;
    }

    @JsonProperty("id")
    public void setId(int id) {
        log.trace("setId({})", id);
        this.id = id;
    }

    @JsonProperty("transform")
    public Transform getTransform() {
        log.trace("getTransform() => {}", transform);
        return transform;
    }

    @JsonProperty("transform")
    public void setTransform(Transform transform) {
        log.trace("setTransform({})", transform);
        this.transform = transform;
    }

    @JsonProperty("fovY")
    public float getFovY() {
        log.trace("getFovY() => {}", fovY);
        return fovY;
    }

    @JsonProperty("fovY")
    public void setFovY(float fovY) {
        log.trace("setFovY({})", fovY);
        this.fovY = fovY;
    }

    @JsonProperty("aspect")
    public float getAspect() {
        log.trace("getAspect() => {}", aspect);
        return aspect;
    }

    @JsonProperty("aspect")
    public void setAspect(float aspect) {
        log.trace("setAspect({})", aspect);
        this.aspect = aspect;
    }

    @JsonProperty("near")
    public float getNear() {
        log.trace("getNear() => {}", near);
        return near;
    }

    @JsonProperty("near")
    public void setNear(float near) {
        log.trace("setNear({})", near);
        this.near = near;
    }

    @JsonProperty("far")
    public float getFar() {
        log.trace("getFar() => {}", far);
        return far;
    }

    @JsonProperty("far")
    public void setFar(float far) {
        log.trace("setFar({})", far);
        this.far = far;
    }

    @JsonIgnore
    public Mat4 getViewMatrix() {
        log.debug("Computing view matrix for Camera id={}", id);
        Transform t = transform;
        Mat4 rotZ = Mat4.rotateZ(-t.getRotation().z);
        Mat4 rotY = Mat4.rotateY(-t.getRotation().y);
        Mat4 rotX = Mat4.rotateX(-t.getRotation().x);
        Mat4 trans = Mat4.translate(
                -t.getPosition().x,
                -t.getPosition().y,
                -t.getPosition().z
        );
        Mat4 view = rotZ.mul(rotY).mul(rotX).mul(trans);
        log.trace("View matrix computed: {}", view);
        return view;
    }

    @JsonIgnore
    public Mat4 getProjectionMatrix() {
        log.debug("Computing projection matrix with fovY={}, aspect={}, near={}, far={}", fovY, aspect, near, far);
        Mat4 proj = Mat4.perspective(fovY, aspect, near, far);
        log.trace("Projection matrix computed: {}", proj);
        return proj;
    }
}
