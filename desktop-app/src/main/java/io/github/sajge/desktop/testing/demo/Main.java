package io.github.sajge.desktop.testing.demo;

import com.formdev.flatlaf.FlatLightLaf;
import io.github.sajge.logger.Logger;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    private static final Logger log = Logger.get(Main.class);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(new FlatLightLaf());
                log.info("FlatLightLaf set as look and feel.");
            } catch (Exception ex) {
                log.error("Failed to set look and feel.", ex);
            }
            new AppFrame().setVisible(true);
        });
    }
}
