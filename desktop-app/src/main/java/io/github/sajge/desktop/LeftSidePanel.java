package io.github.sajge.desktop;

import io.github.sajge.logger.Logger;

import javax.swing.*;
import java.awt.*;

public class LeftSidePanel extends JPanel {
    private static final Logger log = Logger.get(LeftSidePanel.class);

    private final JSplitPane splitPane;
    private final JButton toggleButton;
    private boolean isCollapsed = false;
    private final int COLLAPSED_WIDTH = 50;
    private final int EXPANDED_WIDTH = 250;
    private final JPanel sidebarContent;

    private final Object[][] buttonDefinitions = {
            {"Profile",   "/icons/placeholder.png", (Runnable) this::handleProfileClick},
            {"Projects",  "/icons/placeholder.png", (Runnable) this::handleProjectsClick},
            {"Renderer",  "/icons/placeholder.png", (Runnable) this::handleRendererClick},
            {"Settings",  "/icons/placeholder.png", (Runnable) this::handleSettingsClick}
    };

    public LeftSidePanel() {
        log.debug("LeftSidePanel constructor (expandedWidth={}, collapsedWidth={})", EXPANDED_WIDTH, COLLAPSED_WIDTH);

        JPanel emptyContent = new JPanel();
        emptyContent.setPreferredSize(new Dimension(0, 0));

        sidebarContent = new JPanel();
        sidebarContent.setLayout(new BoxLayout(sidebarContent, BoxLayout.Y_AXIS));

        createSidebarButtons();

        toggleButton = new JButton("<");
        toggleButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        toggleButton.setMaximumSize(new Dimension(Short.MAX_VALUE, 30));
        toggleButton.addActionListener(e -> toggleSidebar());

        JPanel sidebarContainer = new JPanel(new BorderLayout());
        sidebarContainer.add(toggleButton, BorderLayout.NORTH);
        sidebarContainer.add(sidebarContent, BorderLayout.CENTER);

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sidebarContainer, emptyContent);
        splitPane.setDividerSize(5);
        splitPane.setDividerLocation(EXPANDED_WIDTH);
        splitPane.setContinuousLayout(true);
        splitPane.setResizeWeight(0);

        setLayout(new BorderLayout());
        add(splitPane, BorderLayout.CENTER);

        log.info("LeftSidePanel initialized");
    }

    private void createSidebarButtons() {
        log.debug("Creating sidebar buttons");
        for (Object[] def : buttonDefinitions) {
            String text       = (String) def[0];
            String iconPath   = (String) def[1];
            Runnable action   = (Runnable) def[2];

            log.trace(" - button: {}", text);
            JButton button = new JButton(text, loadIcon(iconPath));
            button.setAlignmentX(Component.LEFT_ALIGNMENT);
            button.setMaximumSize(new Dimension(Short.MAX_VALUE, 35));
            button.setHorizontalAlignment(SwingConstants.LEFT);

            button.addActionListener(e -> {
                log.info("Button '{}' clicked", text);
                action.run();
            });

            button.putClientProperty("originalText", text);
            sidebarContent.add(button);
            sidebarContent.add(Box.createRigidArea(new Dimension(0, 5)));
        }
        sidebarContent.add(Box.createVerticalGlue());
    }

    private ImageIcon loadIcon(String path) {
        log.trace("Loading icon: {}", path);
        return new ImageIcon(getClass().getResource(path));
    }

    public void toggleSidebar() {
        isCollapsed = !isCollapsed;
        log.info("Toggling sidebar, now collapsed={}", isCollapsed);

        splitPane.setDividerLocation(isCollapsed ? COLLAPSED_WIDTH : EXPANDED_WIDTH);

        int totalWidth = (isCollapsed ? COLLAPSED_WIDTH : EXPANDED_WIDTH)
                + splitPane.getDividerSize();
        setPreferredSize(new Dimension(totalWidth, getHeight()));
        revalidate();
        repaint();

        toggleButton.setText(isCollapsed ? ">" : "<");
        if (isCollapsed) hideButtonText(); else showButtonText();
    }

    private void hideButtonText() {
        log.debug("Hiding sidebar button text");
        for (Component c : sidebarContent.getComponents()) {
            if (c instanceof JButton btn && btn != toggleButton) {
                btn.setText("");
                btn.setHorizontalAlignment(SwingConstants.CENTER);
                btn.setMaximumSize(new Dimension(COLLAPSED_WIDTH, btn.getMaximumSize().height));
                btn.setPreferredSize(new Dimension(COLLAPSED_WIDTH, btn.getPreferredSize().height));
            }
        }
        sidebarContent.revalidate();
    }

    private void showButtonText() {
        log.debug("Showing sidebar button text");
        for (Component c : sidebarContent.getComponents()) {
            if (c instanceof JButton btn && btn != toggleButton) {
                btn.setText((String)btn.getClientProperty("originalText"));
                btn.setHorizontalAlignment(SwingConstants.LEFT);
                btn.setMaximumSize(new Dimension(Short.MAX_VALUE, btn.getMaximumSize().height));
                btn.setPreferredSize(null);
            }
        }
        sidebarContent.revalidate();
    }

    private void handleProfileClick() {
        log.info("handleProfileClick()");
        // TODO
    }

    private void handleProjectsClick() {
        log.info("handleProjectsClick()");
        // TODO
    }

    private void handleRendererClick() {
        log.info("handleRendererClick()");
        // TODO
    }

    private void handleSettingsClick() {
        log.info("handleSettingsClick()");
        // TODO
    }

}
