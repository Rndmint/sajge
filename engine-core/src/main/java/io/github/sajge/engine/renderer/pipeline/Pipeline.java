package io.github.sajge.engine.renderer.pipeline;

import io.github.sajge.engine.renderer.buffer.DepthBuffer;
import io.github.sajge.engine.renderer.buffer.FrameBuffer;
import io.github.sajge.engine.renderer.core.Mat4;
import io.github.sajge.engine.renderer.core.Vec3;
import io.github.sajge.engine.renderer.core.Vec4;
import io.github.sajge.engine.renderer.core.Vec4;
import io.github.sajge.engine.renderer.scene.Mesh;
import io.github.sajge.engine.renderer.scene.Model;
import io.github.sajge.engine.renderer.scene.Scene;
import io.github.sajge.engine.renderer.scene.Triangle;
import io.github.sajge.logger.Logger;

import java.awt.Color;
import java.util.List;

public class Pipeline {
    private static final Logger log = Logger.get(Pipeline.class);

    private final FrameBuffer fb;
    private final DepthBuffer db;
    private final int[] idBuffer;
    private final VertexProcessor vp;
    private final Clipper clipper;
    private final Projector projector;
    private final ViewportTransformer vt;
    private final Rasterizer rast;

    public Pipeline(
            FrameBuffer fb,
            DepthBuffer db,
            int[] idBuffer
    ) {
        log.info("Initializing Pipeline");
        this.fb = fb;
        this.db = db;
        this.idBuffer = idBuffer;
        this.vp = new VertexProcessor();
        this.clipper = new Clipper();
        this.projector = new Projector();
        this.vt = new ViewportTransformer();
        this.rast = new Rasterizer(fb, db, idBuffer);
    }

    public void renderScene(Scene scene) {
        log.info("Starting scene render");
        fb.clear(Color.BLACK);
        db.clear();

        Mat4 V = scene.getCamera().getViewMatrix();
        Mat4 P = scene.getCamera().getProjectionMatrix();
        Mat4 PV = P.mul(V);
        log.debug("Computed PV matrix");

        for (Model m : scene.getModels()) {
            log.trace("Rendering model id={} with mesh vertices={} and triangles={}",
                    m.getId(), m.getMesh().getVertices().size(), m.getMesh().getTriangles().size());

            Mat4 M = m.getTransform().toMatrix();
            Mat4 MVP = PV.mul(M);
            Mesh mesh = m.getMesh();
            int modelId = m.getId();

            for (Triangle t : mesh.getTriangles()) {
                log.trace("Processing triangle id={} of model {}", t.getId(), modelId);
                Vec3 p0 = mesh.getVertices().get(t.getIndices()[0]);
                Vec3 p1 = mesh.getVertices().get(t.getIndices()[1]);
                Vec3 p2 = mesh.getVertices().get(t.getIndices()[2]);

                Vec4 c0 = vp.transform(new Vec4(p0.x, p0.y, p0.z, 1), MVP);
                Vec4 c1 = vp.transform(new Vec4(p1.x, p1.y, p1.z, 1), MVP);
                Vec4 c2 = vp.transform(new Vec4(p2.x, p2.y, p2.z, 1), MVP);

                Vec3 ndc0 = projector.toNDC(c0);
                Vec3 ndc1 = projector.toNDC(c1);
                Vec3 ndc2 = projector.toNDC(c2);
                if (vp.backfaceCull(ndc0, ndc1, ndc2)) {
                    log.trace("Triangle id={} culled by back-face culling", t.getId());
                    continue;
                }

                List<ClipVertex> clipped = clipper.clipTriangle(
                        new ClipVertex(c0, modelId, t.getId()),
                        new ClipVertex(c1, modelId, t.getId()),
                        new ClipVertex(c2, modelId, t.getId())
                );
                log.debug("Clipped to {} vertices for triangle id={}", clipped.size(), t.getId());

                int width = fb.getImage().getWidth();
                int height = fb.getImage().getHeight();
                for (int i = 1; i + 1 < clipped.size(); i++) {
                    ClipVertex a = clipped.get(0);
                    ClipVertex b = clipped.get(i);
                    ClipVertex c = clipped.get(i + 1);

                    var s0 = vt.toScreen(a, width, height);
                    var s1 = vt.toScreen(b, width, height);
                    var s2 = vt.toScreen(c, width, height);

                    log.trace("Rasterizing triangle at screen coords {}, {}, {}", s0, s1, s2);
                    rast.rasterizeTriangle(s0, s1, s2, m.getMaterial().getColor());
                }
            }
        }

        log.info("Scene render complete");
    }
}
