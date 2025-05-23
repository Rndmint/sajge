package io.github.sajge.engine;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sajge.engine.renderer.Engine;
import io.github.sajge.engine.renderer.Editor;
import io.github.sajge.engine.renderer.scene.Model;
import io.github.sajge.engine.renderer.scene.Transform;
import io.github.sajge.engine.renderer.core.Vec3;
import io.github.sajge.logger.Logger;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SceneEditorApp {
    private static final Logger log = Logger.get(SceneEditorApp.class);
    private static final Map<String, String> primitiveJsons = new LinkedHashMap<>();
    static {
        List<String> names = List.of("cube", "pyramid", "tetrahedron", "octahedron");
        for (String name : names) {
            String path = "/primitives/" + name + ".json";
            try (InputStream in = SceneEditorApp.class.getResourceAsStream(path)) {
                if (in == null) continue;
                String json = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))
                        .lines().collect(Collectors.joining("\n"));
                primitiveJsons.put(name, json);
            } catch (Exception e) {
                log.error("Error loading primitive {}", name, e);
            }
        }
    }

    private final Engine engine;
    private final Editor editor;
    private final ObjectMapper mapper = new ObjectMapper();
    private final JFrame frame;
    private final RenderPanel renderPanel;
    private String lastSavedSceneJson;

    private final JSpinner camPosX = new JSpinner(new SpinnerNumberModel(0.0, -1000.0, 1000.0, 0.1));
    private final JSpinner camPosY = new JSpinner(new SpinnerNumberModel(0.0, -1000.0, 1000.0, 0.1));
    private final JSpinner camPosZ = new JSpinner(new SpinnerNumberModel(5.0, -1000.0, 1000.0, 0.1));
    private final JSpinner camRotX = new JSpinner(new SpinnerNumberModel(0.0, -360.0, 360.0, 1.0));
    private final JSpinner camRotY = new JSpinner(new SpinnerNumberModel(0.0, -360.0, 360.0, 1.0));
    private final JSpinner camRotZ = new JSpinner(new SpinnerNumberModel(0.0, -360.0, 360.0, 1.0));
    private final JSpinner fovSpinner = new JSpinner(new SpinnerNumberModel(1.0471976, 0.1, Math.PI, 0.01));
    private final JSpinner aspectSpinner = new JSpinner(new SpinnerNumberModel(1.3333334, 0.1, 10.0, 0.01));
    private final JSpinner nearSpinner = new JSpinner(new SpinnerNumberModel(0.1, 0.01, 100.0, 0.01));
    private final JSpinner farSpinner = new JSpinner(new SpinnerNumberModel(100.0, 0.1, 1000.0, 1.0));
    private final JButton saveButton = new JButton("Save Scene");

    private final JLabel selectionInfo = new JLabel("Model: -  Face: -");
    private final JSpinner posX = new JSpinner(new SpinnerNumberModel(0.0, -1000.0, 1000.0, 0.1));
    private final JSpinner posY = new JSpinner(new SpinnerNumberModel(0.0, -1000.0, 1000.0, 0.1));
    private final JSpinner posZ = new JSpinner(new SpinnerNumberModel(0.0, -1000.0, 1000.0, 0.1));
    private final JSpinner rotX = new JSpinner(new SpinnerNumberModel(0.0, -360.0, 360.0, 1.0));
    private final JSpinner rotY = new JSpinner(new SpinnerNumberModel(0.0, -360.0, 360.0, 1.0));
    private final JSpinner rotZ = new JSpinner(new SpinnerNumberModel(0.0, -360.0, 360.0, 1.0));
    private final JSpinner scaleX = new JSpinner(new SpinnerNumberModel(1.0, 0.01, 100.0, 0.1));
    private final JSpinner scaleY = new JSpinner(new SpinnerNumberModel(1.0, 0.01, 100.0, 0.1));
    private final JSpinner scaleZ = new JSpinner(new SpinnerNumberModel(1.0, 0.01, 100.0, 0.1));
    private final JButton chooseColor = new JButton("Choose Face Color");
    private final JButton applyColor = new JButton("Apply Face Color");
    private final JButton removeModel = new JButton("Remove Selected");
    private final JButton clearSelectionButton = new JButton("Clear Selection");
    private final JButton resetFieldsButton = new JButton("Reset Fields");
    private Color selectedFaceColor = Color.WHITE;

    private final JList<String> primitiveList = new JList<>();
    private final JButton loadPrimitiveButton = new JButton("Load Primitive");

    private boolean ignoreModelSpinner = false;

    public SceneEditorApp(String initialSceneJson) throws Exception {
        engine = new Engine(800, 600);
        if (initialSceneJson == null || initialSceneJson.trim().equals("{}")) {
            engine.loadSceneFromJson("{ \"camera\": { \"id\":0, \"transform\": { \"position\":[0,0,5], \"rotation\":[0,0,0], \"scale\":[1,1,1] }, \"fovY\":1.0471976, \"aspect\":1.3333334, \"near\":0.1, \"far\":100.0 }, \"objects\":[] }");
        } else {
            engine.loadSceneFromJson(initialSceneJson);
        }
        engine.render();
        editor = new Editor(engine);

        frame = new JFrame("Scene Editor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        renderPanel = new RenderPanel(engine);
        renderPanel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                onViewportClick(e.getX(), e.getY());
            }
        });

        frame.add(buildCameraPanel(), BorderLayout.WEST);
        frame.add(renderPanel, BorderLayout.CENTER);
        frame.add(buildModelPanel(), BorderLayout.EAST);
        frame.add(buildPrimitivePanel(), BorderLayout.SOUTH);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        refreshCameraControls();
        setModelControlsEnabled(false);
    }

    private JPanel buildCameraPanel() {
        JPanel p = new JPanel(new GridLayout(0, 2, 5, 5));
        p.setBorder(BorderFactory.createTitledBorder("Camera"));
        p.add(new JLabel("Pos X:")); p.add(camPosX);
        p.add(new JLabel("Pos Y:")); p.add(camPosY);
        p.add(new JLabel("Pos Z:")); p.add(camPosZ);
        p.add(new JLabel("Rot X:")); p.add(camRotX);
        p.add(new JLabel("Rot Y:")); p.add(camRotY);
        p.add(new JLabel("Rot Z:")); p.add(camRotZ);
        p.add(new JLabel("FOV:"));   p.add(fovSpinner);
        p.add(new JLabel("Aspect:"));p.add(aspectSpinner);
        p.add(new JLabel("Near:"));  p.add(nearSpinner);
        p.add(new JLabel("Far:"));   p.add(farSpinner);
        p.add(saveButton);
        ChangeListener camTransformListener = e -> {
            editor.setCameraTransform(
                    new Vec3(((Double)camPosX.getValue()).floatValue(),
                            ((Double)camPosY.getValue()).floatValue(),
                            ((Double)camPosZ.getValue()).floatValue()),
                    new Vec3((float)Math.toRadians((Double)camRotX.getValue()),
                            (float)Math.toRadians((Double)camRotY.getValue()),
                            (float)Math.toRadians((Double)camRotZ.getValue()))
            );
            engine.render();
            renderPanel.repaint();
        };
        camPosX.addChangeListener(camTransformListener);
        camPosY.addChangeListener(camTransformListener);
        camPosZ.addChangeListener(camTransformListener);
        camRotX.addChangeListener(camTransformListener);
        camRotY.addChangeListener(camTransformListener);
        camRotZ.addChangeListener(camTransformListener);
        ChangeListener camParamsListener = e -> {
            editor.setCameraParams(
                    ((Double)fovSpinner.getValue()).floatValue(),
                    ((Double)aspectSpinner.getValue()).floatValue(),
                    ((Double)nearSpinner.getValue()).floatValue(),
                    ((Double)farSpinner.getValue()).floatValue()
            );
            engine.render();
            renderPanel.repaint();
        };
        fovSpinner.addChangeListener(camParamsListener);
        aspectSpinner.addChangeListener(camParamsListener);
        nearSpinner.addChangeListener(camParamsListener);
        farSpinner.addChangeListener(camParamsListener);
        saveButton.addActionListener(e -> {
            try {
                lastSavedSceneJson = engine.saveSceneToJson();
            } catch (Exception ex) {
                log.error("Save failed", ex);
            }
        });
        return p;
    }

    private JPanel buildModelPanel() {
        JPanel p = new JPanel(new GridLayout(0, 2, 5, 5));
        p.setBorder(BorderFactory.createTitledBorder("Model"));
        p.add(selectionInfo); p.add(new JLabel());
        p.add(new JLabel("Pos X:")); p.add(posX);
        p.add(new JLabel("Pos Y:")); p.add(posY);
        p.add(new JLabel("Pos Z:")); p.add(posZ);
        p.add(new JLabel("Rot X:")); p.add(rotX);
        p.add(new JLabel("Rot Y:")); p.add(rotY);
        p.add(new JLabel("Rot Z:")); p.add(rotZ);
        p.add(new JLabel("Scale X:")); p.add(scaleX);
        p.add(new JLabel("Scale Y:")); p.add(scaleY);
        p.add(new JLabel("Scale Z:")); p.add(scaleZ);
        p.add(chooseColor); p.add(applyColor);
        p.add(removeModel); p.add(clearSelectionButton);
        p.add(resetFieldsButton); p.add(new JLabel());
        ChangeListener modelListener = e -> {
            if (ignoreModelSpinner) return;
            Model m = editor.getSelectedModel();
            if (m == null) return;
            Transform t = m.getTransform();
            t.getPosition().x = ((Double)posX.getValue()).floatValue();
            t.getPosition().y = ((Double)posY.getValue()).floatValue();
            t.getPosition().z = ((Double)posZ.getValue()).floatValue();
            t.getRotation().x = (float)Math.toRadians((Double)rotX.getValue());
            t.getRotation().y = (float)Math.toRadians((Double)rotY.getValue());
            t.getRotation().z = (float)Math.toRadians((Double)rotZ.getValue());
            t.getScale().x = ((Double)scaleX.getValue()).floatValue();
            t.getScale().y = ((Double)scaleY.getValue()).floatValue();
            t.getScale().z = ((Double)scaleZ.getValue()).floatValue();
            engine.render();
            renderPanel.repaint();
        };
        posX.addChangeListener(modelListener);
        posY.addChangeListener(modelListener);
        posZ.addChangeListener(modelListener);
        rotX.addChangeListener(modelListener);
        rotY.addChangeListener(modelListener);
        rotZ.addChangeListener(modelListener);
        scaleX.addChangeListener(modelListener);
        scaleY.addChangeListener(modelListener);
        scaleZ.addChangeListener(modelListener);
        chooseColor.addActionListener(e -> {
            Color c = JColorChooser.showDialog(frame, "Choose Face Color", selectedFaceColor);
            if (c != null) selectedFaceColor = c;
        });
        applyColor.addActionListener(e -> {
            editor.setFaceColor(selectedFaceColor);
            engine.render();
            renderPanel.repaint();
        });
        removeModel.addActionListener(e -> {
            editor.removeSelectedObject();
            engine.render();
            renderPanel.repaint();
            setModelControlsEnabled(false);
        });
        clearSelectionButton.addActionListener(e -> {
            editor.clearSelection();
            selectionInfo.setText("Model: -  Face: -");
            setModelControlsEnabled(false);
        });
        resetFieldsButton.addActionListener(e -> {
            posX.setValue(0.0);
            posY.setValue(0.0);
            posZ.setValue(0.0);
            rotX.setValue(0.0);
            rotY.setValue(0.0);
            rotZ.setValue(0.0);
            scaleX.setValue(1.0);
            scaleY.setValue(1.0);
            scaleZ.setValue(1.0);
        });
        return p;
    }

    private JPanel buildPrimitivePanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.setBorder(BorderFactory.createTitledBorder("Primitives"));
        primitiveList.setListData(primitiveJsons.keySet().toArray(new String[0]));
        p.add(new JScrollPane(primitiveList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
        p.add(loadPrimitiveButton);
        loadPrimitiveButton.addActionListener(e -> {
            String name = primitiveList.getSelectedValue();
            if (name == null) return;
            try {
                Vec3 cam = engine.getScene().getCamera().getTransform().getPosition();
                Model m = mapper.readValue(primitiveJsons.get(name), Model.class);
                editor.addObject(m);
                m.getTransform().setPosition(new Vec3((float)cam.x, (float)cam.y, (float)(cam.z)));
                engine.render();
                renderPanel.repaint();
            } catch (Exception ex) {
                log.error("Error loading primitive {}", name, ex);
            }
        });
        return p;
    }

    private void onViewportClick(int x, int y) {
        editor.selectAt(x, y);
        engine.render();
        renderPanel.repaint();
        int mid = engine.getModelIdAt(x, y);
        int fid = engine.getTriangleIdAt(x, y);
        selectionInfo.setText("Model: " + mid + "  Face: " + fid);
        Model m = editor.getSelectedModel();
        if (m != null) {
            setModelControlsEnabled(true);
            ignoreModelSpinner = true;
            Transform t = m.getTransform();
            posX.setValue((double)t.getPosition().x);
            posY.setValue((double)t.getPosition().y);
            posZ.setValue((double)t.getPosition().z);
            rotX.setValue(Math.toDegrees(t.getRotation().x));
            rotY.setValue(Math.toDegrees(t.getRotation().y));
            rotZ.setValue(Math.toDegrees(t.getRotation().z));
            scaleX.setValue((double)t.getScale().x);
            scaleY.setValue((double)t.getScale().y);
            scaleZ.setValue((double)t.getScale().z);
            selectedFaceColor = engine.getScene().getModels().get(mid).getMesh().getTriangle(fid).getColor();
            ignoreModelSpinner = false;
        } else {
            setModelControlsEnabled(false);
        }
    }

    private void setModelControlsEnabled(boolean enabled) {
        posX.setEnabled(enabled);
        posY.setEnabled(enabled);
        posZ.setEnabled(enabled);
        rotX.setEnabled(enabled);
        rotY.setEnabled(enabled);
        rotZ.setEnabled(enabled);
        scaleX.setEnabled(enabled);
        scaleY.setEnabled(enabled);
        scaleZ.setEnabled(enabled);
        chooseColor.setEnabled(enabled);
        applyColor.setEnabled(enabled);
        removeModel.setEnabled(enabled);
        clearSelectionButton.setEnabled(enabled);
        resetFieldsButton.setEnabled(enabled);
    }

    private void refreshCameraControls() {
        Transform t = engine.getScene().getCamera().getTransform();
        camPosX.setValue((double)t.getPosition().x);
        camPosY.setValue((double)t.getPosition().y);
        camPosZ.setValue((double)t.getPosition().z);
        camRotX.setValue(Math.toDegrees(t.getRotation().x));
        camRotY.setValue(Math.toDegrees(t.getRotation().y));
        camRotZ.setValue(Math.toDegrees(t.getRotation().z));
        fovSpinner.setValue((double)engine.getScene().getCamera().getFovY());
        aspectSpinner.setValue((double)engine.getScene().getCamera().getAspect());
        nearSpinner.setValue((double)engine.getScene().getCamera().getNear());
        farSpinner.setValue((double)engine.getScene().getCamera().getFar());
    }

    public String getLastSavedSceneJson() {
        return lastSavedSceneJson;
    }

    public JsonNode getSceneJsonNode() throws Exception {
        return mapper.readTree(engine.saveSceneToJson());
    }
}
