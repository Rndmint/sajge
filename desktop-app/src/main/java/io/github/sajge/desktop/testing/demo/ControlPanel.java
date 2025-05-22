package io.github.sajge.desktop.testing.demo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ControlPanel extends JPanel {
    private final CardLayout cardLayout = new CardLayout();

    public ControlPanel() {
        super(new BorderLayout());
        JPanel cards = new JPanel(cardLayout);
        cards.setBorder(new EmptyBorder(10, 10, 10, 10));
        cards.setPreferredSize(new Dimension(180, getHeight()));

        for (String name : Constants.VIEW_NAMES) {
            JPanel ctrl = new JPanel();
            ctrl.setLayout(new BoxLayout(ctrl, BoxLayout.Y_AXIS));
            ctrl.setBorder(new EmptyBorder(10, 10, 10, 10));
            ctrl.add(new JLabel(name + " Controls"));
            ctrl.add(Box.createVerticalGlue());
            cards.add(ctrl, name);
        }
        add(cards, BorderLayout.CENTER);
    }

    public void showControls(String name) {
        cardLayout.show(((JPanel) getComponent(0)), name);
    }
}

