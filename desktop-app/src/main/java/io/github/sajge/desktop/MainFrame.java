package io.github.sajge.desktop;

import com.formdev.flatlaf.FlatLightLaf;
import io.github.sajge.logger.Logger;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private static final Logger log = Logger.get(MainFrame.class);
    private static final Color BACKGROUND_COLOR = new Color(52, 73, 94);

    public MainFrame() {
        log.info("Initializing MainFrame");
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            log.error("Failed to set FlatLaf look and feel", e);
        }
        UIManager.put("Panel.background", BACKGROUND_COLOR);
        getContentPane().setBackground(BACKGROUND_COLOR);
        setTitle("Main Application Frame");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        log.info("MainFrame setup complete");
        setVisible(true);
    }

    public static void main(String[] args) {
        new MainFrame();
    }
}
