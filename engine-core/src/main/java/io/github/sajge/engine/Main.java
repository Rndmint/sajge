package io.github.sajge.engine;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        String initialJson = "{}";
        String json = initialJson;
        SwingUtilities.invokeLater(() -> {
            try {
                new SceneEditorApp(json);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
