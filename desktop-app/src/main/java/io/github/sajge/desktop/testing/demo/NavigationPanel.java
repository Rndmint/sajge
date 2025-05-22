package io.github.sajge.desktop.testing.demo;

import io.github.sajge.logger.Logger;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class NavigationPanel extends JPanel {
    private static final Logger log = Logger.get(NavigationPanel.class);
    private final Map<String, JToggleButton> navButtons = new HashMap<>();
    private final ActionListener navListener;
    private boolean expanded = true;

    public NavigationPanel(ActionListener navListener) {
        super(new BorderLayout());
        this.navListener = navListener;
        setBackground(UIManager.getColor("Panel.background"));
        setPreferredSize(new Dimension(Constants.EXPANDED_WIDTH, getHeight()));

        initNavButtons();
        initToggleButton();
    }

    private void initNavButtons() {
        JPanel wrapper = new JPanel();
        wrapper.setOpaque(false);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        ButtonGroup group = new ButtonGroup();

        for (String name : Constants.VIEW_NAMES) {
            ImageIcon icon = IconLoader.load("icons/flat/" + name.toLowerCase() + ".png");
            JToggleButton btn = new JToggleButton(name, icon);
            btn.setAlignmentX(Component.LEFT_ALIGNMENT);
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setIconTextGap(8);
            btn.setBorder(new EmptyBorder(8, 12, 8, 12));
            btn.setFocusPainted(false);
            btn.setBackground(UIManager.getColor("Button.background"));
            btn.addActionListener(e ->
                    navListener.actionPerformed(
                            new ActionEvent(this, ActionEvent.ACTION_PERFORMED, name)
                    )
            );
            group.add(btn);
            wrapper.add(btn);
            wrapper.add(Box.createVerticalStrut(5));
            navButtons.put(name, btn);
        }
        wrapper.add(Box.createVerticalGlue());
        add(wrapper, BorderLayout.NORTH);

        navButtons.get(Constants.VIEW_NAMES[0]).setSelected(true);
    }

    private void initToggleButton() {
        ImageIcon toggleIcon = IconLoader.load("icons/flat/menu.png");
        JButton toggle = new JButton(toggleIcon);
        toggle.setToolTipText("Toggle navigation");
        toggle.setBorderPainted(false);
        toggle.setFocusPainted(false);
        toggle.setContentAreaFilled(false);
        toggle.addActionListener(this::toggleSidePanel);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        bottom.setOpaque(false);
        bottom.add(toggle);
        add(bottom, BorderLayout.SOUTH);
    }

    private void toggleSidePanel(ActionEvent e) {
        expanded = !expanded;
        log.debug("Toggling side panel: expanded={}", expanded);
        int w = expanded
                ? Constants.EXPANDED_WIDTH
                : Constants.COLLAPSED_WIDTH;
        setPreferredSize(new Dimension(w, getHeight()));
        navButtons.forEach((name, btn) -> {
            btn.setText(expanded ? name : "");
            btn.setHorizontalAlignment(
                    expanded ? SwingConstants.LEFT : SwingConstants.CENTER
            );
        });
        revalidate();
        repaint();
    }
}
