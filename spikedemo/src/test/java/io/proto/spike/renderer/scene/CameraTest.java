package io.proto.spike.renderer.scene;

import io.proto.spike.renderer.core.Mat4;
import io.proto.spike.renderer.core.Vec3;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CameraTest {
    private static final float EPSILON = 1e-6f;

    @Test
    void defaultCamera_createsValidProjectionMatrix() {
        float fovY = (float)Math.PI/2;
        float aspect = 1.0f;
        float near = 0.1f;
        float far = 100f;
        Camera cam = new Camera(new Transform(), fovY, aspect, near, far);
        Mat4 proj = cam.getProjectionMatrix();

        float f = 1.0f / (float)Math.tan(fovY/2);

        assertEquals(f/aspect, proj.m00, EPSILON);
        assertEquals(0f, proj.m01);
        assertEquals(0f, proj.m02);
        assertEquals(0f, proj.m03);

        assertEquals(0f, proj.m10);
        assertEquals(f, proj.m11, EPSILON);
        assertEquals(0f, proj.m12);
        assertEquals(0f, proj.m13);

        assertEquals(0f, proj.m20);
        assertEquals(0f, proj.m21);
        assertEquals((far+near)/(near-far), proj.m22, EPSILON);
        assertEquals((2*far*near)/(near-far), proj.m23, EPSILON);

        assertEquals(0f, proj.m30);
        assertEquals(0f, proj.m31);
        assertEquals(-1f, proj.m32);
        assertEquals(0f, proj.m33);
    }

    @Test
    void projectionMatrix_changesWithParameters() {
        float fovY = (float)Math.PI/4;
        float aspect = 2.0f;
        float near = 0.5f;
        float far = 50f;
        Camera cam = new Camera(new Transform(), fovY, aspect, near, far);
        Mat4 proj = cam.getProjectionMatrix();

        float f = 1.0f / (float)Math.tan(fovY/2);
        float expected22 = (far + near)/(near - far);
        float expected23 = (2 * far * near)/(near - far);

        assertEquals(f/aspect, proj.m00, EPSILON);
        assertEquals(0f, proj.m01);
        assertEquals(0f, proj.m02);
        assertEquals(0f, proj.m03);

        assertEquals(0f, proj.m10);
        assertEquals(f, proj.m11, EPSILON);
        assertEquals(0f, proj.m12);
        assertEquals(0f, proj.m13);

        assertEquals(0f, proj.m20);
        assertEquals(0f, proj.m21);
        assertEquals(expected22, proj.m22, EPSILON);
        assertEquals(expected23, proj.m23, EPSILON);

        assertEquals(0f, proj.m30);
        assertEquals(0f, proj.m31);
        assertEquals(-1f, proj.m32);
        assertEquals(0f, proj.m33);
    }

    @Test
    void viewMatrix_translationOnly() {
        Transform t = new Transform();
        t.position = new Vec3(5, 3, 10);
        Camera cam = new Camera(t, 0, 0, 0, 0);

        Mat4 view = cam.getViewMatrix();

        assertEquals(1f, view.m00);
        assertEquals(0f, view.m01);
        assertEquals(0f, view.m02);
        assertEquals(-5f, view.m03);
        assertEquals(0f, view.m10);
        assertEquals(1f, view.m11);
        assertEquals(0f, view.m12);
        assertEquals(-3f, view.m13);
        assertEquals(0f, view.m20);
        assertEquals(0f, view.m21);
        assertEquals(1f, view.m22);
        assertEquals(-10f, view.m23);
    }

    @Test
    void viewMatrix_rotationOnly() {
        Transform t = new Transform();
        t.rotation = new Vec3((float)Math.PI/2, 0, 0);
        Camera cam = new Camera(t, 0, 0, 0, 0);

        Mat4 view = cam.getViewMatrix();

        assertEquals(1f, view.m00);
        assertEquals(0f, view.m01);
        assertEquals(0f, view.m02);
        assertEquals(0f, view.m03);
        assertEquals(0f, view.m10, EPSILON);
        assertEquals(0f, view.m11, EPSILON);
        assertEquals(1f, view.m12, EPSILON);
        assertEquals(0f, view.m13);
        assertEquals(0f, view.m20, EPSILON);
        assertEquals(-1f, view.m21, EPSILON);
        assertEquals(0f, view.m22, EPSILON);
        assertEquals(0f, view.m23);
    }

    @Test
    void viewMatrix_combinedTransform() {
        Transform t = new Transform();
        t.position = new Vec3(5, 3, 10);
        t.rotation = new Vec3(0, (float)Math.PI/2, 0);
        Camera cam = new Camera(t, 0, 0, 0, 0);

        Mat4 view = cam.getViewMatrix();

        assertEquals(0f, view.m00, EPSILON);
        assertEquals(0f, view.m01);
        assertEquals(-1f, view.m02, EPSILON);
        assertEquals(10f, view.m03, EPSILON);
        assertEquals(0f, view.m10);
        assertEquals(1f, view.m11);
        assertEquals(0f, view.m12);
        assertEquals(-3f, view.m13);
        assertEquals(1f, view.m20, EPSILON);
        assertEquals(0f, view.m21);
        assertEquals(0f, view.m22, EPSILON);
        assertEquals(-5f, view.m23, EPSILON);
    }
}
