package io.github.sajge.desktop;

import com.formdev.flatlaf.FlatLightLaf;
import io.github.sajge.logger.Logger;

import javax.swing.*;

public class Main {
    private static final Logger log = Logger.get(Main.class);

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel( new FlatLightLaf() );
            log.info("Applied FlatLaf Light theme");
        } catch (Exception ex) {
            log.error("Failed to initialize FlatLaf", ex);
        }

        SwingUtilities.invokeLater(() -> {
            log.info("SwingUtilities.invokeLater: launching AppContext");
            AppContext context = new AppContext();
            JFrame mainFrame = context.getMainFrame();
            log.debug("MainFrame obtained, making visible");
            mainFrame.setVisible(true);
        });

    }

}
