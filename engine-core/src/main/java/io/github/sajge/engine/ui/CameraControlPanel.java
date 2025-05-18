package io.github.sajge.engine.ui;

import io.github.sajge.engine.renderer.Editor;
import io.github.sajge.engine.renderer.Engine;
import io.github.sajge.engine.renderer.scene.Camera;
import io.github.sajge.engine.renderer.scene.Transform;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class CameraControlPanel extends JPanel {
    private final Editor editor;
    private final RendererPanel rendererPanel;

    public CameraControlPanel(
            Editor editor, Engine engine, RendererPanel rendererPanel
    ) {
        this.editor = editor;
        this.rendererPanel = rendererPanel;
        setLayout(new GridLayout(0, 2, 5, 5));

        Camera cam = engine.getScene().getCamera();
        Transform t = cam.getTransform();

        add(new JLabel("Pos X"));
        add(makeSpinner(
                t.getPosition().x,
                v -> {
                    editor.setCameraPositionX(v);
                    rendererPanel.refresh();
                }
        ));
        add(new JLabel("Pos Y"));
        add(makeSpinner(
                t.getPosition().y,
                v -> {
                    editor.setCameraPositionY(v);
                    rendererPanel.refresh();
                }
        ));
        add(new JLabel("Pos Z"));
        add(makeSpinner(
                t.getPosition().z,
                v -> {
                    editor.setCameraPositionZ(v);
                    rendererPanel.refresh();
                }
        ));

        add(new JLabel("Rot X"));
        add(makeSpinner(
                t.getRotation().x,
                v -> {
                    editor.setCameraRotationX(v);
                    rendererPanel.refresh();
                }
        ));
        add(new JLabel("Rot Y"));
        add(makeSpinner(
                t.getRotation().y,
                v -> {
                    editor.setCameraRotationY(v);
                    rendererPanel.refresh();
                }
        ));
        add(new JLabel("Rot Z"));
        add(makeSpinner(
                t.getRotation().z,
                v -> {
                    editor.setCameraRotationZ(v);
                    rendererPanel.refresh();
                }
        ));

        add(new JLabel("FOV Y"));
        add(makeSpinner(
                cam.getFovY(),
                v -> {
                    editor.setCameraParams(v, cam.getAspect(), cam.getNear(), cam.getFar());
                    rendererPanel.refresh();
                }
        ));
        add(new JLabel("Aspect"));
        add(makeSpinner(
                cam.getAspect(),
                v -> {
                    editor.setCameraParams(cam.getFovY(), v, cam.getNear(), cam.getFar());
                    rendererPanel.refresh();
                }
        ));
        add(new JLabel("Near"));
        add(makeSpinner(
                cam.getNear(),
                v -> {
                    editor.setCameraParams(cam.getFovY(), cam.getAspect(), v, cam.getFar());
                    rendererPanel.refresh();
                }
        ));
        add(new JLabel("Far"));
        add(makeSpinner(
                cam.getFar(),
                v -> {
                    editor.setCameraParams(cam.getFovY(), cam.getAspect(), cam.getNear(), v);
                    rendererPanel.refresh();
                }
        ));
    }

    private JSpinner makeSpinner(float initial, Consumer<Float> onChange) {
        SpinnerNumberModel model = new SpinnerNumberModel(
                (double)initial, -Double.MAX_VALUE, Double.MAX_VALUE, 0.1
        );
        JSpinner spinner = new JSpinner(model);
        Dimension d = spinner.getPreferredSize();
        d.width = 100;
        spinner.setPreferredSize(d);

        spinner.addChangeListener(evt -> {
            float v = ((Double)spinner.getValue()).floatValue();
            onChange.accept(v);
        });
        return spinner;
    }
}
