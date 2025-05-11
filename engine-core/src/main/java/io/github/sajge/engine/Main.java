package io.github.sajge.engine;

import io.github.sajge.engine.renderer.Editor;
import io.github.sajge.engine.renderer.Engine;
import io.github.sajge.engine.renderer.core.Vec3;
import io.github.sajge.engine.renderer.scene.Scene;
import io.github.sajge.logger.Logger;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Main {
    private static final Logger log = Logger.get(Main.class);

    public static void main(String[] args) {
        log.info("Application starting");
        SwingUtilities.invokeLater(() -> {
            try {
                Engine engine = new Engine(800, 600);
                log.info("Engine initialized with size {}x{}", engine.getWidth(), engine.getHeight());

                String json;
                try (InputStream in = Main.class.getClassLoader().getResourceAsStream("scene.json")) {
                    if (in == null) {
                        log.error("scene.json not found in resources");
                        throw new RuntimeException("scene.json not found in resources");
                    }
                    json = new Scanner(in, StandardCharsets.UTF_8.name())
                            .useDelimiter("\\A").next();
                }
                log.info("Loaded scene JSON (length={})", json.length());
                engine.loadSceneFromJson(json);
                log.info("Scene loaded: {}", engine.getScene());

                Editor editor = new Editor(engine);
                Scene.TriangleInfo initial = engine.getScene()
                        .findByTriangleId(
                                engine.getScene().getModels().get(0)
                                        .getMesh().getTriangles().get(0).getId()
                        ).orElseThrow(() -> new IllegalStateException("No triangles found"));
                editor.select(initial.getTriangleId());

                JFrame fbFrame = new JFrame("Framebuffer");
                fbFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                JPanel renderPanel = new JPanel() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        BufferedImage img = engine.getFrame();
                        g.drawImage(img, 0, 0, null);
                    }
                };
                renderPanel.setPreferredSize(new Dimension(engine.getWidth(), engine.getHeight()));
                fbFrame.add(renderPanel);
                fbFrame.pack();
                fbFrame.setLocation(100, 100);
                fbFrame.setVisible(true);
                log.info("Render window displayed");

                JFrame editorFrame = new JFrame("Editor");
                editorFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                JPanel controls = new JPanel();
                controls.setLayout(new BoxLayout(controls, BoxLayout.Y_AXIS));
                controls.setPreferredSize(new Dimension(300, engine.getHeight()));

                addSlider(controls, "Cam Pos X", -10, 10, 0, val -> {
                    Vec3 pos = engine.getScene().getCamera().getTransform().getPosition();
                    editor.setCameraTransform(new Vec3(val, pos.y, pos.z),
                            engine.getScene().getCamera().getTransform().getRotation());
                    log.debug("Camera X set to {}", val);
                });
                addSlider(controls, "Cam Pos Y", -10, 10, 0, val -> {
                    Vec3 pos = engine.getScene().getCamera().getTransform().getPosition();
                    editor.setCameraTransform(new Vec3(pos.x, val, pos.z),
                            engine.getScene().getCamera().getTransform().getRotation());
                    log.debug("Camera Y set to {}", val);
                });
                addSlider(controls, "Cam Pos Z", -20, 20, 5, val -> {
                    Vec3 pos = engine.getScene().getCamera().getTransform().getPosition();
                    editor.setCameraTransform(new Vec3(pos.x, pos.y, val),
                            engine.getScene().getCamera().getTransform().getRotation());
                    log.debug("Camera Z set to {}", val);
                });

                addSlider(controls, "Model Pos X", -10, 10, 0, val -> {
                    editor.translateSelected(val, 0, 0);
                    log.debug("Model X translated by {}", val);
                });
                addSlider(controls, "Model Pos Y", -10, 10, 0, val -> {
                    editor.translateSelected(0, val, 0);
                    log.debug("Model Y translated by {}", val);
                });
                addSlider(controls, "Model Pos Z", -20, 20, 0, val -> {
                    editor.translateSelected(0, 0, val);
                    log.debug("Model Z translated by {}", val);
                });

                editorFrame.add(new JScrollPane(controls));
                editorFrame.pack();
                editorFrame.setLocation(920, 100);
                editorFrame.setVisible(true);
                log.info("Editor window displayed");

                renderPanel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        int x = e.getX(), y = e.getY();
                        int idx = y * engine.getWidth() + x;
                        int id = engine.getIdBuffer().get(idx);
                        log.debug("Mouse clicked at ({},{}) -> triangleId={}", x, y, id);
                        if (id >= 0) {
                            editor.select(id);
                            log.info("Selected triangle: {}", id);
                        }
                    }
                });

                new Timer(16, ev -> {
                    engine.render();
                    renderPanel.repaint();
                    log.trace("Frame rendered");
                }).start();

            } catch (Exception ex) {
                log.error("Unexpected error", ex);
            }
        });
    }

    private static void addSlider(JPanel parent, String name,
                                  int min, int max, int init,
                                  java.util.function.Consumer<Float> consumer) {
        JLabel label = new JLabel(name + ": " + init);
        JSlider slider = new JSlider(min, max, init);
        slider.setMajorTickSpacing((max - min) / 5);
        slider.setPaintTicks(true);
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                float v = slider.getValue();
                label.setText(name + ": " + (int) v);
                consumer.accept(v);
            }
        });
        parent.add(label);
        parent.add(slider);
    }
}
