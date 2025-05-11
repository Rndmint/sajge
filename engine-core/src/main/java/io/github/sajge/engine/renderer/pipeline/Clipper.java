package io.github.sajge.engine.renderer.pipeline;

import io.github.sajge.engine.renderer.core.Plane;
import io.github.sajge.logger.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Clipper {
    private static final Logger log = Logger.get(Clipper.class);

    private static final Plane[] PLANES = {
            new Plane( 1,  0,  0, 1),
            new Plane(-1,  0,  0, 1),
            new Plane( 0,  1,  0, 1),
            new Plane( 0, -1,  0, 1),
            new Plane( 0,  0,  1, 1),
            new Plane( 0,  0, -1, 1)
    };

    public List<ClipVertex> clipTriangle(
            ClipVertex v0,
            ClipVertex v1,
            ClipVertex v2
    ) {
        log.debug("Clipping triangle with vertices {}, {}, {}", v0, v1, v2);
        List<ClipVertex> verts = Arrays.asList(v0, v1, v2);

        for (Plane p : PLANES) {
            log.trace("Clipping against plane {}", p);
            verts = clipAgainst(verts, p);
            log.debug("Vertices after clipping: {}", verts.size());
            if (verts.isEmpty()) {
                log.info("Triangle fully clipped against plane {}", p);
                break;
            }
        }

        return verts;
    }

    private List<ClipVertex> clipAgainst(
            List<ClipVertex> in,
            Plane p
    ) {
        List<ClipVertex> out = new ArrayList<>();
        int n = in.size();

        for (int i = 0; i < n; i++) {
            ClipVertex A = in.get(i);
            ClipVertex B = in.get((i + 1) % n);

            float da = p.distance(A.getPos());
            float db = p.distance(B.getPos());
            boolean inA = da >= 0;
            boolean inB = db >= 0;

            log.trace("Edge from {} to {}: da={}, db={}", A, B, da, db);

            if (inA) {
                out.add(new ClipVertex(A.getPos(), A.getModelId(), A.getTriangleId()));
                log.trace("Kept vertex {}", A);
            }

            if (inA ^ inB) {
                var ip = p.intersect(A.getPos(), B.getPos());
                ClipVertex intersectVertex = new ClipVertex(ip, A.getModelId(), A.getTriangleId());
                out.add(intersectVertex);
                log.debug("Added intersection vertex {}", intersectVertex);
            }
        }

        return out;
    }
}
