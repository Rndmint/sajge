package io.github.sajge.desktop;

import io.github.sajge.desktop.placeholders.PlaceholderPanel;
import io.github.sajge.logger.Logger;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private static final Logger log = Logger.get(MainFrame.class);

    private final JPanel northPanel;
    private final JPanel southPanel;
    private final JPanel eastPanel;
    private final JPanel westPanel;
    private final JPanel centerPanel;

    public MainFrame() {
        log.debug("MainFrame constructor");
        northPanel  = new PlaceholderPanel();
        southPanel  = new PlaceholderPanel();
        eastPanel   = new PlaceholderPanel();
        westPanel   = new LeftSidePanel();
        centerPanel = new PlaceholderPanel();

        initUI();
    }

    private void initUI() {
        log.debug("Initializing MainFrame UI");
        setTitle("Sajge");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        log.info("MainFrame size={}x{}", getWidth(), getHeight());

        add(northPanel, BorderLayout.NORTH);
        add(southPanel, BorderLayout.SOUTH);
        add(eastPanel, BorderLayout.EAST);
        add(westPanel, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);

        log.debug("MainFrame UI components added");
    }
}
