package io.proto.spike.renderer.pipeline;

import io.proto.spike.renderer.core.Plane;
import io.proto.spike.renderer.core.Vec4;
import io.proto.spike.renderer.support.ClipVertex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Clipper {
    private static final Plane[] PLANES = {
            new Plane( 1,  0,  0, 1),  // Right plane
            new Plane(-1,  0,  0, 1),  // Left plane
            new Plane( 0,  1,  0, 1),  // Top plane
            new Plane( 0, -1,  0, 1),  // Bottom plane
            new Plane( 0,  0,  1, 1),  // Far plane
            new Plane( 0,  0, -1, 1)   // Near plane
    };

    public List<ClipVertex> clipTriangle(ClipVertex v0, ClipVertex v1, ClipVertex v2) {
        List<ClipVertex> verts = Arrays.asList(v0, v1, v2);

        for (Plane p : PLANES) {
            verts = clipAgainst(verts, p);
            if (verts.isEmpty()) break;
        }

        return verts;
    }

    private List<ClipVertex> clipAgainst(List<ClipVertex> in, Plane p) {
        List<ClipVertex> out = new ArrayList<>();
        int n = in.size();

        for (int i = 0; i < n; i++) {
            ClipVertex A = in.get(i);
            ClipVertex B = in.get((i + 1) % n);

            float da = p.distance(A.pos);
            float db = p.distance(B.pos);
            boolean insideA = da >= 0;
            boolean insideB = db >= 0;

            if (insideA) {
                out.add(new ClipVertex(new Vec4(A.pos.x, A.pos.y, A.pos.z, A.pos.w)));
            }

            if (insideA ^ insideB) {
                Vec4 ip = p.intersect(A.pos, B.pos);
                out.add(new ClipVertex(ip));
            }
        }

        return out;
    }
}
