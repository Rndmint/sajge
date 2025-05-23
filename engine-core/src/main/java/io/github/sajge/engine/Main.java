package io.github.sajge.engine;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new SceneEditorApp("{}");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
