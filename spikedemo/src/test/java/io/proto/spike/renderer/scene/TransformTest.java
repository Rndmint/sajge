package io.proto.spike.renderer.scene;

import io.proto.spike.renderer.core.Mat4;
import io.proto.spike.renderer.core.Vec3;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TransformTest {

    @Test
    void defaultTransform_createsIdentityMatrix() {
        Transform t = new Transform();
        Mat4 m = t.toMatrix();

        assertEquals(1f, m.m00);
        assertEquals(0f, m.m01);
        assertEquals(0f, m.m02);
        assertEquals(0f, m.m03);
        assertEquals(0f, m.m10);
        assertEquals(1f, m.m11);
        assertEquals(0f, m.m12);
        assertEquals(0f, m.m13);
        assertEquals(0f, m.m20);
        assertEquals(0f, m.m21);
        assertEquals(1f, m.m22);
        assertEquals(0f, m.m23);
        assertEquals(0f, m.m30);
        assertEquals(0f, m.m31);
        assertEquals(0f, m.m32);
        assertEquals(1f, m.m33);
    }

    @Test
    void translation_affectsMatrixCorrectly() {
        Transform t = new Transform();
        t.position = new Vec3(2, 3, 4);
        Mat4 m = t.toMatrix();

        assertEquals(1f, m.m00);
        assertEquals(0f, m.m01);
        assertEquals(0f, m.m02);
        assertEquals(2f, m.m03);
        assertEquals(0f, m.m10);
        assertEquals(1f, m.m11);
        assertEquals(0f, m.m12);
        assertEquals(3f, m.m13);
        assertEquals(0f, m.m20);
        assertEquals(0f, m.m21);
        assertEquals(1f, m.m22);
        assertEquals(4f, m.m23);
    }

    @Test
    void rotationX_affectsMatrixCorrectly() {
        Transform t = new Transform();
        t.rotation = new Vec3((float)Math.PI/2, 0, 0);
        Mat4 m = t.toMatrix();

        assertEquals(1f, m.m00);
        assertEquals(0f, m.m01);
        assertEquals(0f, m.m02);
        assertEquals(0f, m.m03);
        assertEquals(0f, m.m10, 1e-6f);
        assertEquals(0f, m.m11, 1e-6f);
        assertEquals(-1f, m.m12, 1e-6f);
        assertEquals(0f, m.m13);
        assertEquals(0f, m.m20, 1e-6f);
        assertEquals(1f, m.m21, 1e-6f);
        assertEquals(0f, m.m22, 1e-6f);
        assertEquals(0f, m.m23);
    }

    @Test
    void rotationY_affectsMatrixCorrectly() {
        Transform t = new Transform();
        t.rotation = new Vec3(0, (float)Math.PI/2, 0);
        Mat4 m = t.toMatrix();

        assertEquals(0f, m.m00, 1e-6f);
        assertEquals(0f, m.m01);
        assertEquals(1f, m.m02, 1e-6f);
        assertEquals(0f, m.m03);
        assertEquals(0f, m.m10);
        assertEquals(1f, m.m11);
        assertEquals(0f, m.m12);
        assertEquals(0f, m.m13);
        assertEquals(-1f, m.m20, 1e-6f);
        assertEquals(0f, m.m21);
        assertEquals(0f, m.m22, 1e-6f);
        assertEquals(0f, m.m23);
    }

    @Test
    void scale_affectsMatrixCorrectly() {
        Transform t = new Transform();
        t.scale = new Vec3(2, 3, 4);
        Mat4 m = t.toMatrix();

        assertEquals(2f, m.m00);
        assertEquals(0f, m.m01);
        assertEquals(0f, m.m02);
        assertEquals(0f, m.m03);
        assertEquals(0f, m.m10);
        assertEquals(3f, m.m11);
        assertEquals(0f, m.m12);
        assertEquals(0f, m.m13);
        assertEquals(0f, m.m20);
        assertEquals(0f, m.m21);
        assertEquals(4f, m.m22);
        assertEquals(0f, m.m23);
    }

    @Test
    void combinedTransform_appliesCorrectOrder() {
        Transform t = new Transform();
        t.position = new Vec3(1, 2, 3);
        t.rotation = new Vec3(0, (float)Math.PI/2, 0);
        t.scale = new Vec3(2, 2, 2);

        Mat4 m = t.toMatrix();

        assertEquals(0f, m.m00, 1e-6f);
        assertEquals(0f, m.m01);
        assertEquals(2f, m.m02, 1e-6f);
        assertEquals(1f, m.m03);
        assertEquals(0f, m.m10);
        assertEquals(2f, m.m11);
        assertEquals(0f, m.m12);
        assertEquals(2f, m.m13);
        assertEquals(-2f, m.m20, 1e-6f);
        assertEquals(0f, m.m21);
        assertEquals(0f, m.m22, 1e-6f);
        assertEquals(3f, m.m23);
    }
}
