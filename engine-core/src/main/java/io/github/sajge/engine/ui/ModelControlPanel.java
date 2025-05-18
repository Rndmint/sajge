package io.github.sajge.engine.ui;

import io.github.sajge.engine.renderer.Editor;
import io.github.sajge.engine.renderer.Engine;
import io.github.sajge.engine.renderer.scene.Model;
import io.github.sajge.engine.renderer.scene.Scene;
import io.github.sajge.engine.renderer.scene.Transform;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public class ModelControlPanel extends JPanel {
    private final Editor editor;
    private final Engine engine;
    private final RendererPanel rendererPanel;

    private JComboBox<Integer> modelSelector;
    private JSpinner transX, transY, transZ;
    private JSpinner rotX,    rotY,    rotZ;
    private JSpinner scaleX,  scaleY,  scaleZ;
    private JLabel    faceLabel;

    public ModelControlPanel(
            Editor editor, Engine engine, RendererPanel rendererPanel
    ) {
        this.editor = editor;
        this.engine = engine;
        this.rendererPanel = rendererPanel;

        setLayout(new GridLayout(0,2,5,5));

        buildModelSelector();
        buildTransformSpinners();
        buildFaceColorer();
    }

    private void buildModelSelector() {
        Scene scene = engine.getScene();
        List<Model> models = scene.getModels();
        Integer[] ids = models.stream()
                .map(Model::getId)
                .toArray(Integer[]::new);

        add(new JLabel("Model"));
        modelSelector = new JComboBox<>(ids);
        modelSelector.setSelectedIndex(0);
        add(modelSelector);

        modelSelector.addActionListener(evt -> {
            int mid = (Integer)modelSelector.getSelectedItem();
            editor.select(mid,0);
            updateSpinnersForModel(mid);
            rendererPanel.refresh();
        });

        int initialId = ids[0];
        editor.select(initialId, 0);
    }

    private void buildTransformSpinners() {
        transX = makeSpinner(v -> applyTranslation(v, 'X'));
        transY = makeSpinner(v -> applyTranslation(v, 'Y'));
        transZ = makeSpinner(v -> applyTranslation(v, 'Z'));

        add(new JLabel("Trans X")); add(transX);
        add(new JLabel("Trans Y")); add(transY);
        add(new JLabel("Trans Z")); add(transZ);

        rotX = makeSpinner(v -> applyRotation(v, 'X'));
        rotY = makeSpinner(v -> applyRotation(v, 'Y'));
        rotZ = makeSpinner(v -> applyRotation(v, 'Z'));

        add(new JLabel("Rot X")); add(rotX);
        add(new JLabel("Rot Y")); add(rotY);
        add(new JLabel("Rot Z")); add(rotZ);

        scaleX = makeSpinner(v -> applyScale(v, 'X'));
        scaleY = makeSpinner(v -> applyScale(v, 'Y'));
        scaleZ = makeSpinner(v -> applyScale(v, 'Z'));

        add(new JLabel("Scale X")); add(scaleX);
        add(new JLabel("Scale Y")); add(scaleY);
        add(new JLabel("Scale Z")); add(scaleZ);

        updateSpinnersForModel((Integer)modelSelector.getSelectedItem());
    }

    private void buildFaceColorer() {
        faceLabel = new JLabel("Face: –");
        add(faceLabel);

        JButton colorBtn = new JButton("Color Face");
        add(colorBtn);

        colorBtn.addActionListener(evt -> {
            Color c = JColorChooser.showDialog(
                    this, "Choose Face Color", Color.WHITE
            );
            if (c != null) {
                editor.setFaceColor(c);
                rendererPanel.refresh();
            }
        });
    }

    public void updateSelection(int modelId, int triangleId) {
        modelSelector.setSelectedItem(modelId);
        faceLabel.setText("Face: " + triangleId);
        updateSpinnersForModel(modelId);
    }

    private void applyTranslation(float v, char axis) {
        switch(axis) {
            case 'X': editor.translateSelectedX(v); break;
            case 'Y': editor.translateSelectedY(v); break;
            default:  editor.translateSelectedZ(v); break;
        }
        rendererPanel.refresh();
    }

    private void applyRotation(float v, char axis) {
        switch(axis) {
            case 'X': editor.rotateSelectedX(v); break;
            case 'Y': editor.rotateSelectedY(v); break;
            default:  editor.rotateSelectedZ(v); break;
        }
        rendererPanel.refresh();
    }

    private void applyScale(float v, char axis) {
        switch(axis) {
            case 'X': editor.scaleSelectedX(v); break;
            case 'Y': editor.scaleSelectedY(v); break;
            default:  editor.scaleSelectedZ(v); break;
        }
        rendererPanel.refresh();
    }

    private void updateSpinnersForModel(int modelId) {
        Transform t = engine.getScene()
                .getModels()
                .stream()
                .filter(m -> m.getId() == modelId)
                .findFirst()
                .map(Model::getTransform)
                .orElseThrow();

        transX.setValue((double)t.getPosition().x);
        transY.setValue((double)t.getPosition().y);
        transZ.setValue((double)t.getPosition().z);

        rotX .setValue((double)t.getRotation().x);
        rotY .setValue((double)t.getRotation().y);
        rotZ .setValue((double)t.getRotation().z);

        scaleX.setValue((double)t.getScale().x);
        scaleY.setValue((double)t.getScale().y);
        scaleZ.setValue((double)t.getScale().z);
    }

    private JSpinner makeSpinner(Consumer<Float> onChange) {
        SpinnerNumberModel model = new SpinnerNumberModel(
                0.0, -Double.MAX_VALUE, Double.MAX_VALUE, 0.1
        );
        JSpinner spinner = new JSpinner(model);
        Dimension d = spinner.getPreferredSize();
        d.width = 80;
        spinner.setPreferredSize(d);

        spinner.addChangeListener(evt -> {
            float v = ((Double)spinner.getValue()).floatValue();
            onChange.accept(v);
        });
        return spinner;
    }
}
