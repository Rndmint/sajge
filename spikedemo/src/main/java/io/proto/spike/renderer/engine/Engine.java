package io.proto.spike.renderer.engine;

import io.proto.spike.renderer.buffer.DepthBuffer;
import io.proto.spike.renderer.buffer.FrameBuffer;
import io.proto.spike.renderer.pipeline.*;
import io.proto.spike.renderer.scene.Scene;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Engine {
    public List<Scene> scenes = new ArrayList<>();
    public FrameBuffer fb;
    public DepthBuffer db;
    private JFrame window;
    private RenderPanel panel;

    public Engine(int w, int h) {
        fb = new FrameBuffer(w, h);
        db = new DepthBuffer(w, h);
    }

    public void start() {
        window = new JFrame("Software Renderer");
        panel = new RenderPanel();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.add(panel);
        window.pack();
        window.setVisible(true);
        Timer timer = new Timer(16, e -> {
            renderFrame();
            panel.repaint();
        });
        timer.start();
    }

    private void renderFrame() {
        Pipeline p = new Pipeline(
                new VertexProcessor(),
                new Clipper(),
                new Projector(),
                new ViewportTransformer(),
                new Rasterizer(fb, db)
        );
        for (Scene s : scenes) {
            s.render(p, fb, db);
        }
    }

    private class RenderPanel extends JPanel {
        public RenderPanel() {
            setPreferredSize(new Dimension(fb.image.getWidth(), fb.image.getHeight()));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(fb.image, 0, 0, null);
        }
    }

    public static void main(String[] args) {
        new Engine(300, 200).start();
    }

}
