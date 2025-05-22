package io.github.sajge.desktop.testing.demo;

import io.github.sajge.logger.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class AppFrame extends JFrame {
    private static final Logger log = Logger.get(AppFrame.class);
    private final NavigationPanel navigation;
    private final MainContentPanel mainContent;
    private final ControlPanel controlPanel;

    public AppFrame() {
        super("FlatLaf Swing App");
        log.info("Starting SwingApp UI");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(900, 650);
        setLocationRelativeTo(null);

        navigation = new NavigationPanel(this::onNavigate);
        mainContent = new MainContentPanel();
        controlPanel = new ControlPanel();

        add(navigation, BorderLayout.WEST);
        add(mainContent, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.EAST);
        onNavigate(new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
                Constants.VIEW_NAMES[0]));

        setVisible(true);
        log.info("SwingApp UI visible");
    }

    private void onNavigate(ActionEvent e) {
        String view = e.getActionCommand();
        log.debug("Switching view to '{}'.", view);
        mainContent.showView(view);
        controlPanel.showControls(view);
    }
}
