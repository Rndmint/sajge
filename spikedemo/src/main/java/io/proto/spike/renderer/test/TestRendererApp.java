package io.proto.spike.renderer.test;

import io.proto.spike.renderer.engine.Engine;
import io.proto.spike.renderer.mesh.Mesh;
import io.proto.spike.renderer.mesh.Triangle;
import io.proto.spike.renderer.core.Vec3;
import io.proto.spike.renderer.scene.Material;
import io.proto.spike.renderer.scene.Model;
import io.proto.spike.renderer.scene.Scene;
import io.proto.spike.renderer.scene.Camera;
import io.proto.spike.renderer.scene.Transform;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.List;

public class TestRendererApp {
    public static void main(String[] args) {
        Engine engine = new Engine(800, 600);
        Scene scene = new Scene(new Camera(new Transform(), (float)Math.toRadians(60), 800f/600f, 0.1f, 100f));

        Mesh cube = createCubeMesh();
        Transform cubeTransform = new Transform();
        Material cubeMat = new Material(Color.ORANGE);
        Model cubeModel = new Model(cube, cubeTransform, cubeMat);
        scene.addModel(cubeModel);
        engine.scenes.add(scene);

        JFrame frame = new JFrame("Test Renderer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel renderPanel = new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(800, 600);
            }
        };
        frame.add(renderPanel, BorderLayout.CENTER);
        engine.start();
        frame.pack();

        JPanel controls = new JPanel();
        controls.setLayout(new BoxLayout(controls, BoxLayout.Y_AXIS));
        controls.setPreferredSize(new Dimension(200, 600));

        addSlider(controls, "Cam Pos X", -10, 10, 0, val -> scene.getCamera().transform.position.x = val);
        addSlider(controls, "Cam Pos Y", -10, 10, 0, val -> scene.getCamera().transform.position.y = val);
        addSlider(controls, "Cam Pos Z", -20, 20, 5, val -> scene.getCamera().transform.position.z = val);
        addSlider(controls, "Cam Rot X", -180, 180, 0, deg -> scene.getCamera().transform.rotation.x = (float)Math.toRadians(deg));
        addSlider(controls, "Cam Rot Y", -180, 180, 0, deg -> scene.getCamera().transform.rotation.y = (float)Math.toRadians(deg));
        addSlider(controls, "Cam Rot Z", -180, 180, 0, deg -> scene.getCamera().transform.rotation.z = (float)Math.toRadians(deg));

        addSlider(controls, "Cube Pos X", -10, 10, 0, val -> cubeTransform.position.x = val);
        addSlider(controls, "Cube Pos Y", -10, 10, 0, val -> cubeTransform.position.y = val);
        addSlider(controls, "Cube Pos Z", -20, 20, 0, val -> cubeTransform.position.z = val);
        addSlider(controls, "Cube Rot X", 0, 360, 0, deg -> cubeTransform.rotation.x = (float)Math.toRadians(deg));
        addSlider(controls, "Cube Rot Y", 0, 360, 0, deg -> cubeTransform.rotation.y = (float)Math.toRadians(deg));
        addSlider(controls, "Cube Rot Z", 0, 360, 0, deg -> cubeTransform.rotation.z = (float)Math.toRadians(deg));
        addSlider(controls, "Cube Scale X", 1, 200, 100, pct -> cubeTransform.scale.x = pct / 100f);
        addSlider(controls, "Cube Scale Y", 1, 200, 100, pct -> cubeTransform.scale.y = pct / 100f);
        addSlider(controls, "Cube Scale Z", 1, 200, 100, pct -> cubeTransform.scale.z = pct / 100f);

        frame.add(new JScrollPane(controls), BorderLayout.EAST);
        frame.setVisible(true);
    }

    private static void addSlider(JPanel parent, String name, int min, int max, int init, FloatConsumer consumer) {
        JLabel label = new JLabel(name + ": " + init);
        JSlider slider = new JSlider(min, max, init);
        slider.setMajorTickSpacing((max-min)/5);
        slider.setPaintTicks(true);
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int val = slider.getValue();
                label.setText(name + ": " + val);
                consumer.accept((float)val);
            }
        });
        parent.add(label);
        parent.add(slider);
    }

    private static Mesh createCubeMesh() {
        Mesh m = new Mesh();
        Vec3[] v = new Vec3[] {
                new Vec3(-1, -1, -1), new Vec3(1, -1, -1), new Vec3(1, 1, -1), new Vec3(-1, 1, -1),
                new Vec3(-1, -1, 1),  new Vec3(1, -1, 1),  new Vec3(1, 1, 1),  new Vec3(-1, 1, 1)
        };
        for (Vec3 vert : v) m.vertices.add(vert);
        int[][] idx = {
                {0,1,2},{0,2,3}, // back
                {4,6,5},{4,7,6}, // front
                {0,4,5},{0,5,1}, // bottom
                {3,2,6},{3,6,7}, // top
                {1,5,6},{1,6,2}, // right
                {0,3,7},{0,7,4}  // left
        };
        for (int[] f : idx) {
            Vec3 p0 = m.vertices.get(f[0]);
            Vec3 p1 = m.vertices.get(f[1]);
            Vec3 p2 = m.vertices.get(f[2]);
            Vec3 n = new Vec3(
                    (p1.y-p0.y)*(p2.z-p0.z) - (p1.z-p0.z)*(p2.y-p0.y),
                    (p1.z-p0.z)*(p2.x-p0.x) - (p1.x-p0.x)*(p2.z-p0.z),
                    (p1.x-p0.x)*(p2.y-p0.y) - (p1.y-p0.y)*(p2.x-p0.x)
            ).normalize();
            m.triangles.add(new Triangle(f[0], f[1], f[2], n, Color.LIGHT_GRAY));
        }
        return m;
    }

    private interface FloatConsumer { void accept(float v); }
}
