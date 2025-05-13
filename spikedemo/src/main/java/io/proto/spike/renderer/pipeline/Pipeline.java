package io.proto.spike.renderer.pipeline;

import io.proto.spike.renderer.buffer.DepthBuffer;
import io.proto.spike.renderer.buffer.FrameBuffer;
import io.proto.spike.renderer.core.Mat4;
import io.proto.spike.renderer.core.Vec3;
import io.proto.spike.renderer.core.Vec4;
import io.proto.spike.renderer.mesh.Mesh;
import io.proto.spike.renderer.mesh.Triangle;
import io.proto.spike.renderer.scene.Model;
import io.proto.spike.renderer.scene.Scene;
import io.proto.spike.renderer.support.ClipVertex;
import io.proto.spike.renderer.support.ScreenVertex;

import java.awt.*;
import java.util.List;

public class Pipeline {
    private VertexProcessor vp;
    private Clipper clipper;
    private Projector projector;
    private ViewportTransformer vt;
    private Rasterizer rast;

    public Pipeline(VertexProcessor vp,
                    Clipper clipper,
                    Projector projector,
                    ViewportTransformer vt,
                    Rasterizer rast) {
        this.vp       = vp;
        this.clipper  = clipper;
        this.projector= projector;
        this.vt       = vt;
        this.rast     = rast;
    }

    public void renderScene(Scene scene, FrameBuffer fb, DepthBuffer db) {
        fb.clear(Color.BLACK);
        db.clear();

        Mat4 V  = scene.camera.getViewMatrix();
        Mat4 P  = scene.camera.getProjectionMatrix();
        Mat4 PV = P.mul(V);

        for (Model m : scene.models) {
            Mat4 M   = m.transform.toMatrix();
            Mat4 MVP = PV.mul(M);
            Mesh mesh = m.mesh;

            for (Triangle t : mesh.triangles) {
                Vec3 p0 = mesh.vertices.get(t.i0);
                Vec3 p1 = mesh.vertices.get(t.i1);
                Vec3 p2 = mesh.vertices.get(t.i2);

                Vec4 c0 = vp.transform(new Vec4(p0.x, p0.y, p0.z, 1), MVP);
                Vec4 c1 = vp.transform(new Vec4(p1.x, p1.y, p1.z, 1), MVP);
                Vec4 c2 = vp.transform(new Vec4(p2.x, p2.y, p2.z, 1), MVP);

                Vec3 ndc0 = projector.toNDC(c0);
                Vec3 ndc1 = projector.toNDC(c1);
                Vec3 ndc2 = projector.toNDC(c2);
                if (vp.backfaceCull(ndc0, ndc1, ndc2)) {
                    continue;
                }

                ClipVertex cv0 = new ClipVertex(c0);
                ClipVertex cv1 = new ClipVertex(c1);
                ClipVertex cv2 = new ClipVertex(c2);
                List<ClipVertex> clipped = clipper.clipTriangle(cv0, cv1, cv2);

                for (int i = 1; i + 1 < clipped.size(); i++) {
                    ClipVertex a = clipped.get(0);
                    ClipVertex b = clipped.get(i);
                    ClipVertex c = clipped.get(i + 1);

                    ScreenVertex s0 = vt.toScreen(a, fb.image.getWidth(), fb.image.getHeight());
                    ScreenVertex s1 = vt.toScreen(b, fb.image.getWidth(), fb.image.getHeight());
                    ScreenVertex s2 = vt.toScreen(c, fb.image.getWidth(), fb.image.getHeight());

                    rast.rasterizeTriangle(s0, s1, s2, m.material.color);
                }
            }
        }
    }
}
