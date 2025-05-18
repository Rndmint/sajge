package io.github.sajge.engine.ui;

import io.github.sajge.engine.renderer.Editor;
import io.github.sajge.engine.renderer.Engine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AppFrame {
    private Engine engine;
    private Editor editor;
    private RendererPanel rendererPanel;
    private CameraControlPanel cameraPanel;
    private ModelControlPanel modelPanel;

    public static void init() {
        SwingUtilities.invokeLater(() -> new AppFrame().buildAndShow());
    }

    private void buildAndShow() {
        engine = new Engine(800, 600);
        editor = new Editor(engine);

        SceneLoader.loadScene(engine);
        engine.render();

        rendererPanel = new RendererPanel(engine);
        cameraPanel   = new CameraControlPanel(editor, engine, rendererPanel);
        modelPanel    = new ModelControlPanel(editor, engine, rendererPanel);

        cameraPanel.setPreferredSize(new Dimension(280, -1));
        modelPanel .setPreferredSize(new Dimension(280, -1));

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Camera", cameraPanel);
        tabs.addTab("Model",  modelPanel);

        JSplitPane split = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                tabs,
                rendererPanel
        );
        split.setResizeWeight(0.0);
        split.setDividerLocation(280);
        split.setOneTouchExpandable(true);

        JFrame frame = new JFrame("3D Editor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(split, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        rendererPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                editor.selectAt(e.getX(), e.getY());
                rendererPanel.refresh();

                int mid = engine.getModelIdAt(e.getX(), e.getY());
                int tid = engine.getTriangleIdAt(e.getX(), e.getY());
                modelPanel.updateSelection(mid, tid);
            }
        });

        rendererPanel.refresh();
    }
}
