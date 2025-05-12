package io.github.sajge.engine;

import io.github.sajge.engine.renderer.Editor;
import io.github.sajge.engine.renderer.Engine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                InputStream in = Main.class.getClassLoader().getResourceAsStream("scene.json");
                if (in == null) {
                    throw new IllegalStateException("scene.json not found in resources");
                }
                String json = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))
                        .lines().collect(Collectors.joining("\n"));

                Engine engine = new Engine(800, 600);
                engine.loadSceneFromJson(json);
                engine.render();
                Editor editor = new Editor(engine);

                createAndShowGUI(engine, editor, json);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private static void createAndShowGUI(Engine engine, Editor editor, String originalJson) {
        JFrame frame = new JFrame("Renderer Editor Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        RenderPanel renderPanel = new RenderPanel(engine);
        renderPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                editor.selectAt(e.getX(), e.getY());
                renderPanel.repaint();
            }
        });
        frame.add(renderPanel, BorderLayout.CENTER);

        JToolBar toolbar = new JToolBar();

        JButton btnTxPos = new JButton("Tx+X");
        btnTxPos.addActionListener(e -> {
            editor.translateSelected(0.1f, 0f, 0f);
            renderPanel.repaint();
        });
        toolbar.add(btnTxPos);

        JButton btnTxNeg = new JButton("Tx-X");
        btnTxNeg.addActionListener(e -> {
            editor.translateSelected(-0.1f, 0f, 0f);
            renderPanel.repaint();
        });
        toolbar.add(btnTxNeg);

        JButton btnRyPos = new JButton("Ry+Y");
        btnRyPos.addActionListener(e -> {
            editor.rotateSelected(0f, (float)Math.toRadians(10), 0f);
            renderPanel.repaint();
        });
        toolbar.add(btnRyPos);

        JButton btnRyNeg = new JButton("Ry-Y");
        btnRyNeg.addActionListener(e -> {
            editor.rotateSelected(0f, (float)Math.toRadians(-10), 0f);
            renderPanel.repaint();
        });
        toolbar.add(btnRyNeg);

        JButton btnScaleUp = new JButton("Scale+10%\n");
        btnScaleUp.addActionListener(e -> {
            editor.scaleSelected(1.1f, 1.1f, 1.1f);
            renderPanel.repaint();
        });
        toolbar.add(btnScaleUp);

        JButton btnScaleDown = new JButton("Scale-10%");
        btnScaleDown.addActionListener(e -> {
            editor.scaleSelected(0.9f, 0.9f, 0.9f);
            renderPanel.repaint();
        });
        toolbar.add(btnScaleDown);

        JButton btnColor = new JButton("Color");
        btnColor.addActionListener(e -> {
            Color c = JColorChooser.showDialog(frame, "Choose Face Color", Color.WHITE);
            if (c != null) {
                editor.setFaceColor(c);
                renderPanel.repaint();
            }
        });
        toolbar.add(btnColor);

        JButton btnRemove = new JButton("Remove");
        btnRemove.addActionListener(e -> {
            editor.removeSelectedObject();
            renderPanel.repaint();
        });
        toolbar.add(btnRemove);

        JButton btnReload = new JButton("Reload");
        btnReload.addActionListener(e -> {
            try {
                engine.loadSceneFromJson(originalJson);
                editor.clearSelection();
                engine.render();
                renderPanel.repaint();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        toolbar.add(btnReload);

        JButton btnSave = new JButton("Save");
        btnSave.addActionListener(e -> {
            try {
                String outJson = engine.saveSceneToJson();
                System.out.println("Saved Scene JSON:\n" + outJson);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        toolbar.add(btnSave);

        frame.add(toolbar, BorderLayout.NORTH);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    static class RenderPanel extends JPanel {
        private final Engine engine;

        public RenderPanel(Engine engine) {
            this.engine = engine;
            setPreferredSize(new Dimension(engine.getWidth(), engine.getHeight()));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            BufferedImage img = engine.getFrame();
            if (img != null) {
                g.drawImage(img, 0, 0, null);
            }
        }
    }
}
